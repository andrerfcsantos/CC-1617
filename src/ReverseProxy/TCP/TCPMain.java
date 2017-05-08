package ReverseProxy.TCP;

import ReverseProxy.MonitorUDP.MonitorTable;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class TCPMain extends Thread {

    MonitorTable tabelaMonitorizacao;

    public TCPMain (MonitorTable tabelaMonitorizacao){
        this.tabelaMonitorizacao = tabelaMonitorizacao;
    }

    @Override
    public void run() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
            server.createContext("/", new ClientReaderHandler(tabelaMonitorizacao));
            server.start();
        } catch (IOException e) {
            System.err.println("[TCPMain] Erro http server");
        }
    }



}
