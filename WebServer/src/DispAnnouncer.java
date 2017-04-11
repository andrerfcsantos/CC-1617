import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 * Created by Andre on 11-04-2017.
 */
public class DispAnnouncer extends Thread{
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
            System.err.println("[MonitorUDP] Erro ao abrir socket de monitorização.");
        } catch (UnknownHostException e) {
            System.err.println("[MonitorUDP] Host não reconhecido.");
        }

    }

    @Override
    public void run() {
        ByteArrayOutputStream b_out = new ByteArrayOutputStream();
        PDU pdu;
        byte b_array[];

        try {
            ObjectOutputStream o_out = new ObjectOutputStream(b_out);
            pdu = new PDU(0,TipoPDU.DISPONIVEL);
            o_out.writeObject(pdu);
            o_out.flush();
            b_array = b_out.toByteArray();
            DatagramPacket pacote = new DatagramPacket(b_array, b_array.length, hostAdress, 5555);

            socket.send(pacote);
        } catch(IOException e){
            System.err.println("[MonitorUDP] Erro ao abrir ObjectOutputStream");
        }



        socket.close();

}
