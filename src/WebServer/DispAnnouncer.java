package WebServer;

import Utils.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class DispAnnouncer extends Thread {
    private DatagramSocket socket;
    private InetAddress hostAdress;


    public DispAnnouncer() {
        this("localhost");
    }

    public DispAnnouncer(String hostName) {
        try {
            hostAdress = InetAddress.getByName(hostName);
            socket = new DatagramSocket(5555);
        } catch (SocketException e) {
            System.err.println("[DispAnnoucer] Erro ao abrir socket de monitorização.");
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
                pacote = new DatagramPacket(b_array, b_array.length, hostAdress, 5555);
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
