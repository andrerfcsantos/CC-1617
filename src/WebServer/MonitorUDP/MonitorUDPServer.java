package WebServer.MonitorUDP;

public class MonitorUDPServer extends Thread{
    String hostName;

    public MonitorUDPServer(){
        this.hostName = "localhost";
    }

    public MonitorUDPServer(String hostName){
        this.hostName = hostName;
    }

    @Override
    public void run() {
        System.out.println("[MonitorUDPServer] A iniciar DispAnnouncer...");
        new DispAnnouncer(hostName).start();
        System.out.println("[MonitorUDPServer] DispAnnouncer iniciado.");
        System.out.println("[MonitorUDPServer] A iniciar ProbeReplier...");
        new ProbeReplier().start();
        System.out.println("[MonitorUDPServer] ProbeReplier iniciado.");
    }
}
