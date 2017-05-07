package ReverseProxy.TCP;

import ReverseProxy.MonitorUDP.MonitorTable;
import ReverseProxy.MonitorUDP.MonitorTableEntry;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class TCPMain extends Thread {

    ServerSocket ss;
    MonitorTable tabelaMonitorizacao;

    public TCPMain (MonitorTable tabelaMonitorizacao){
        this.tabelaMonitorizacao = tabelaMonitorizacao;
    }


    @Override
    public void run() {
        try {
            Socket sockCliente;
            ss = new ServerSocket(80);

            while(true){
                sockCliente = ss.accept();

                tabelaMonitorizacao.lock();
                InetAddress ip = (InetAddress) tabelaMonitorizacao.tabela.keySet().toArray()[0];
                tabelaMonitorizacao.unlock();

                Socket sockWebServer = new Socket(ip,80);
                new TCPClientListener(sockCliente,sockWebServer).start();
                new TCPClientWriter(sockCliente,sockWebServer).start();
            }


        } catch (IOException e) {
            System.err.println("[TCPMain] Erro ao abrir Socket");
        }
    }
}
