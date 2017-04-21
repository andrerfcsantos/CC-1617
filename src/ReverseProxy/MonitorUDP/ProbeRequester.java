package ReverseProxy.MonitorUDP;


import Utils.PDU;
import Utils.TipoPDU;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;

public class ProbeRequester extends Thread {

    private DatagramSocket socket;
    private InetAddress hostAddress;
    private MonitorTableEntry entradaTabela;
    private int nSeq;
    private boolean exit;
    private MonitorTable tabelaMonitorizacao;

    public ProbeRequester(InetAddress hostAdd,
                          MonitorTable tabelaMonitorizacao,  MonitorTableEntry entradaTabela,
                          DatagramSocket socket){
            this.tabelaMonitorizacao = tabelaMonitorizacao;
            this.nSeq=0;
            this.hostAddress = hostAdd;
            this.socket = socket;
            this.exit = false;
            this.entradaTabela = entradaTabela;
    }

    @Override
    public void run() {
        ByteArrayOutputStream b_out;
        ObjectOutputStream o_out;
        DatagramPacket pacote_out;
        PDU pdu_pedido;
        PkgLossInfo pkgLossInfo;
        float percPkglost;
        Duration timeSinceLastAvailable;
        byte b_array[];

        while (!exit) {
            try {
                pdu_pedido = new PDU(nSeq, TipoPDU.PROB_REQUEST);

                b_out = new ByteArrayOutputStream();
                o_out = new ObjectOutputStream(b_out);

                o_out.writeObject(pdu_pedido);
                o_out.flush();

                b_array = b_out.toByteArray();
                pacote_out = new DatagramPacket(b_array, b_array.length, hostAddress, 5555);
                socket.send(pacote_out);

                entradaTabela.lock();
                try{
                    entradaTabela.setTimeLastSeqSent(pdu_pedido.getTimeSent());
                    entradaTabela.setLastSeqSent(pdu_pedido.getSeq());
                } finally{
                    entradaTabela.unlock();
                }
                System.out.println("[ProbeRequester] Pedido enviado: " + pdu_pedido.toString());
                nSeq++;
                Thread.sleep(5000);

                entradaTabela.lock();
                try{
                    if(entradaTabela.getLastSeqReceived()==(nSeq-1)){
                        entradaTabela.reportPkgStatus(PkgStatus.RECEIVED);
                    }else{
                        entradaTabela.reportPkgStatus(PkgStatus.LOST);
                    }

                    pkgLossInfo = entradaTabela.getPackagesLost();

                } finally {
                    entradaTabela.unlock();
                }

                percPkglost = (pkgLossInfo.getTotalPackages() != 0) ?
                        ((float) pkgLossInfo.getPkgCount() / pkgLossInfo.getTotalPackages()) : 0.0f;

                System.out.println("[ProbeRequester] Pacotes perdidos: " + pkgLossInfo.getPkgCount() +
                        "/" +
                        pkgLossInfo.getTotalPackages()+
                        " (" + percPkglost*100 +"%)");

                tabelaMonitorizacao.lock();
                timeSinceLastAvailable = Duration.between(entradaTabela.getLastAvailable(), Instant.now());
                if(timeSinceLastAvailable.getSeconds() > 120){
                    System.out.println("[ProbeRequester] A terminar ligação a " + hostAddress + " por inactividade.");
                    exit=true;
                    tabelaMonitorizacao.deleteEntry(hostAddress);
                }
                tabelaMonitorizacao.unlock();


            } catch (IOException e) {
                System.err.println("[ProbeRequester] Erro ao abrir ObjectOutputStream");
                break;
            } catch (InterruptedException e) {
                System.err.println("[ProbeRequester] Erro sleep");
                break;
            }
        }

        return;
    }


}
