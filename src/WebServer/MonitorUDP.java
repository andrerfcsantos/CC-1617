package WebServer;

public class MonitorUDP extends Thread{
    String hostName;

    MonitorUDP(){
        this.hostName = "localhost";
    }

    MonitorUDP(String hostName){
        this.hostName = hostName;
    }

    @Override
    public void run() {
        System.out.println("[MonitorUDP] A iniciar DispAnnouncer...");
        new DispAnnouncer(hostName).start();
        System.out.println("[MonitorUDP] DispAnnouncer iniciado.");
        System.out.println("[MonitorUDP] A iniciar ProbeReplier...");
        new ProbeReplier().start();
        System.out.println("[MonitorUDP] ProbeReplier iniciado.");
    }
}
