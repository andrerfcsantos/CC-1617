import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

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
    }
}
