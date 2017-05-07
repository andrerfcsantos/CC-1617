package ReverseProxy;


import ReverseProxy.MonitorUDP.MonitorTable;
import ReverseProxy.MonitorUDP.MonitorUDPProxy;
import ReverseProxy.TCP.TCPMain;

import java.util.concurrent.locks.ReentrantLock;

public class ReverseProxy {

    public static MonitorTable tabelaMonitorizacao;
    public static ReentrantLock lockTabela;

    public static void main(String[] args) {
        tabelaMonitorizacao = new MonitorTable();
        lockTabela = new ReentrantLock();

        System.out.println("[ReverseProxy] A iniciar ReverseProxy...");

        System.out.println("[ReverseProxy] A iniciar MonitorUDP...");
        new MonitorUDPProxy(tabelaMonitorizacao).start();
        System.out.println("[ReverseProxy] MonitorUDP iniciado.");

        System.out.println("[ReverseProxy] A iniciar TCPMain...");
        new TCPMain(tabelaMonitorizacao).start();
        System.out.println("[ReverseProxy] TCPMain iniciado.");

        System.out.println("[ReverseProxy] ReverseProxy iniciado.");

    }
}
