package WebServer;

import WebServer.MonitorUDP.MonitorUDPServer;
import WebServer.TCP.TCPMain;

public class WebServer {
    public static void main(String[] args) {
        String host;

        if(args.length==0){
            host = "localhost";
        }else{
            host = args[0];
        }


        System.out.println("[WebServer] A iniciar Web Server com host " + host +" ...");

        System.out.println("[WebServer] A iniciar MonitorUDPServer..");
        new MonitorUDPServer(host).start();
        System.out.println("[WebServer] MonitorUDPServer iniciado.");
        

        System.out.println("[WebServer] WebServer iniciado.");
    }
}
