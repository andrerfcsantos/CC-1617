package WebServer;

public class WebServer {
    public static void main(String[] args) {
        System.out.println("[WebServer] A iniciar Web Server...");
        System.out.println("[WebServer] A iniciar MonitorUDP..");
        new MonitorUDP().start();
        System.out.println("[WebServer] MonitorUDP iniciado.");
        System.out.println("[WebServer] WebServer iniciado.");
    }
}
