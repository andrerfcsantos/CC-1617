import java.net.DatagramSocket;

public class WebServer {
    public static void main(String[] args) {
        new MonitorUDP().start();
    }
}
