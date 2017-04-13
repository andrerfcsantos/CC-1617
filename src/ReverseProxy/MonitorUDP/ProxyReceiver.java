package ReverseProxy.MonitorUDP;


import Utils.PDU;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;

public class ProxyReceiver extends Thread{
    private MonitorTable tabelaMonitorizacao;
    private DatagramSocket socket;
    private ReentrantLock lockTabela;


    public ProxyReceiver(MonitorTable tabela,ReentrantLock lockTabela,DatagramSocket socket){
        this.tabelaMonitorizacao = tabela;
        this.lockTabela = lockTabela;
        this.socket = socket;
    }

    @Override
    public void run() {
        ByteArrayInputStream b_in;
        ObjectInputStream o_in;
        DatagramPacket pacote_in;
        PDU pdu_pedido;
        InetAddress endereco_origem;
        MonitorTableEntry entradaTabela;
        byte buf_in[];

        while (true) {

            try {
                buf_in = new byte[1024];
                pacote_in = new DatagramPacket(buf_in, buf_in.length);
                socket.receive(pacote_in);
                endereco_origem = pacote_in.getAddress();
                b_in = new ByteArrayInputStream(buf_in,0,pacote_in.getLength());
                o_in = new ObjectInputStream(b_in);
                pdu_pedido = (PDU) o_in.readObject();
                System.out.println("[ProxyReceiver] Pedido recebido:" + pdu_pedido.toString());

                switch (pdu_pedido.getTipo()){
                    case DISPONIVEL:

                        lockTabela.lock();
                        try{
                            if(tabelaMonitorizacao.ipExists(endereco_origem)){
                                tabelaMonitorizacao.setLastAvailable(endereco_origem, pdu_pedido.getTimeSent());
                            }else{
                                System.out.println("[ProxyReceiver] Registo de novo servidor em:" + endereco_origem.toString());
                                entradaTabela = new MonitorTableEntry();
                                tabelaMonitorizacao.addEntry(endereco_origem, entradaTabela);
                                entradaTabela.setLastAvailable(pdu_pedido.getTimeSent());
                                new ProbeRequester(endereco_origem, entradaTabela, lockTabela, socket).start();
                            }
                        } finally{
                            lockTabela.unlock();
                        }

                        break;
                    case PROB_REPLY:
                        Duration rtt, averageRTT;
                        lockTabela.lock();
                        try{
                            MonitorTableEntry entry = tabelaMonitorizacao.getEntry(endereco_origem);
                            entry.setLastSeqReceived(pdu_pedido.getSeq());
                            entry.setLastAvailable(pdu_pedido.getTimeSent());
                            if(entry.getLastSeqReceived()==entry.getLastSeqSent()){
                                rtt=Duration.between(entry.getTimeLastSeqSent(), Instant.now());
                                entry.addRTTEntry(rtt);
                            }
                            averageRTT = entry.getAverageRTT();
                            System.out.println("[ProxyReceiver] RTT médio: " + averageRTT.toMillis() + " (ms) " +
                                                "com base em " + entry.getNEntriesRTT() + " pacotes.");
                        } finally {
                            lockTabela.unlock();
                        }

                        break;
                    case PROB_REQUEST:
                        break;
                }

            } catch (IOException e) {
                System.err.println("[ProxyReceiver] Erro ao abrir ObjectInputStream");
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("[ProxyReceiver] Erro ao criar PDU do pacote");
                break;
            }
        }

        socket.close();
    }
}
