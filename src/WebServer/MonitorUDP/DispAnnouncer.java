package WebServer.MonitorUDP;

import Utils.*;
import WebServer.WebServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class DispAnnouncer extends Thread {
    private DatagramSocket socket;
    private InetAddress hostAddress;

    public DispAnnouncer(String hostName,DatagramSocket socket) {
        try {
            this.hostAddress = InetAddress.getByName(hostName);
            this.socket = socket;
        } catch (UnknownHostException e) {
            System.err.println("[DispAnnoucer] Host não reconhecido.");
        }

    }

    @Override
    public void run() {
        ByteArrayOutputStream b_out;
        ObjectOutputStream o_out;
        DatagramPacket pacote;
        PDU pdu;
        byte b_array[];

        while (true) {
            try {
                b_out = new ByteArrayOutputStream();
                o_out = new ObjectOutputStream(b_out);
                pdu = new PDU(0, TipoPDU.DISPONIVEL);
                o_out.writeObject(pdu);
                o_out.flush();
                b_array = b_out.toByteArray();
                pacote = new DatagramPacket(b_array, b_array.length, hostAddress, 5555);
                socket.send(pacote);
                System.out.println("[DispAnnoucer] Pacote enviado:" + pdu.toString());
                Thread.sleep(3000);
            } catch (IOException e) {
                System.err.println("[DispAnnoucer] Erro ao abrir ObjectOutputStream");
                break;
            } catch (InterruptedException e) {
                System.err.println("[DispAnnoucer] Erro no sleep");
                break;
            }
        }

        socket.close();
    }
}
