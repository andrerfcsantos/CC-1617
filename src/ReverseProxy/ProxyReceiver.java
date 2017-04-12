package ReverseProxy;


import Utils.PDU;
import Utils.TipoPDU;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ProxyReceiver extends Thread{
    MonitorTable tabelaMonitorizacao;
    DatagramSocket socket;


    public ProxyReceiver(MonitorTable tabela){
        tabelaMonitorizacao = tabela;
        try {
            socket = new DatagramSocket(5555);
        } catch (SocketException e) {
            System.err.println("[ProbeRequester] Erro ao abrir socket de monitorização.");
        }
    }

    @Override
    public void run() {
        ByteArrayInputStream b_in;
        ObjectInputStream o_in;
        DatagramPacket pacote_in;
        PDU pdu_pedido;
        InetAddress endereco_origem;
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
                System.out.println("Pedido recebido:" + pdu_pedido.toString());

                switch (pdu_pedido.getTipo()){
                    case DISPONIVEL:
                        if(tabelaMonitorizacao.ipExists(endereco_origem)){
                            tabelaMonitorizacao.setLastAvailable(endereco_origem, pdu_pedido.getInstant());
                        }else{
                            tabelaMonitorizacao.addEntry(endereco_origem, pdu_pedido.getInstant());
                        }
                        break;
                    case PROB_REPLY:
                        break;
                    case PROB_REQUEST:
                        break;
                }

            } catch (IOException e) {
                System.err.println("[ProbeReplier] Erro ao abrir ObjectInputStream");
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("[ProbeReplier] Erro ao criar PDU do pacote");
                break;
            }
        }

        socket.close();
    }
}
