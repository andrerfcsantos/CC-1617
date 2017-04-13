package WebServer.MonitorUDP;

import java.net.DatagramSocket;
import java.net.SocketException;

public class MonitorUDPServer extends Thread{
    private String hostName;
    private DatagramSocket socketUDP;

    public MonitorUDPServer(){
        this.hostName = "localhost";
    }

    public MonitorUDPServer(String hostName){
        this.hostName = hostName;
    }

    @Override
    public void run() {
        try {
            socketUDP = new DatagramSocket(5555);

            System.out.println("[MonitorUDPServer] A iniciar DispAnnouncer...");
            new DispAnnouncer(hostName,socketUDP).start();
            System.out.println("[MonitorUDPServer] DispAnnouncer iniciado.");
            System.out.println("[MonitorUDPServer] A iniciar ProbeReplier...");

            new ProbeReplier(socketUDP).start();
            System.out.println("[MonitorUDPServer] ProbeReplier iniciado.");
        } catch (SocketException e) {
            System.err.println("[MonitorUDPServer] Erro ao abrir socket UDP.");
        }


    }
}
