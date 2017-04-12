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
        new DispAnnouncer(hostName).start();
        new ProbeReplier().start();
    }
}
