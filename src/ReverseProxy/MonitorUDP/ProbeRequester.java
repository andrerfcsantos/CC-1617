package ReverseProxy.MonitorUDP;


import Utils.PDU;
import Utils.TipoPDU;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.locks.ReentrantLock;

public class ProbeRequester extends Thread {

    private DatagramSocket socket;
    private InetAddress hostAddress;
    private MonitorTableEntry entradaTabela;
    private int nSeq;
    private ReentrantLock lockTabela;

    public ProbeRequester(InetAddress hostAdd, MonitorTableEntry entradaTabela, ReentrantLock lockTabela){
        try {
            this.entradaTabela = entradaTabela;
            this.nSeq=0;
            this.hostAddress = hostAdd;
            this.lockTabela = lockTabela;
            socket = new DatagramSocket(5555);
        } catch (SocketException e) {
            System.err.println("[ProbeRequester] Erro ao abrir socket para respostas de probe.");
        }
    }

    @Override
    public void run() {
        ByteArrayOutputStream b_out;
        ObjectOutputStream o_out;
        DatagramPacket pacote_out;
        PDU pdu_pedido;
        byte b_array[];

        while (true) {

            try {
                pdu_pedido = new PDU(nSeq, TipoPDU.PROB_REPLY);

                b_out = new ByteArrayOutputStream();
                o_out = new ObjectOutputStream(b_out);

                o_out.writeObject(pdu_pedido);
                o_out.flush();

                b_array = b_out.toByteArray();
                pacote_out = new DatagramPacket(b_array, b_array.length, hostAddress, 5555);
                socket.send(pacote_out);

                lockTabela.lock();
                try{
                    entradaTabela.setTimeLastSeqSent(pdu_pedido.getTimeSent());
                    entradaTabela.setLastSeqSent(pdu_pedido.getSeq());
                    entradaTabela.incPackagesSent();
                } finally{
                    lockTabela.unlock();
                }
                System.out.println("[ProbeRequester] Pedido enviado: " + pdu_pedido.toString());
                nSeq++;
                Thread.sleep(3000);

                lockTabela.lock();
                try{
                    if(entradaTabela.getLastSeqReceived()!=(nSeq-1)){
                        entradaTabela.incPackagesLost();
                    }
                } finally {
                    lockTabela.unlock();
                }
                System.out.println("[ProbeRequester] Pacotes perdidos: " + entradaTabela.getPackagesLost() +
                                                                            "/" +
                                                                            entradaTabela.getPackagesSent());

            } catch (IOException e) {
                System.err.println("[ProbeRequester] Erro ao abrir ObjectOutputStream");
                break;
            } catch (InterruptedException e) {
                System.err.println("[ProbeRequester] Erro sleep");
                break;
            }
        }

        socket.close();
    }
}
