package ReverseProxy;


import ReverseProxy.MonitorUDP.MonitorTable;
import ReverseProxy.MonitorUDP.MonitorUDPProxy;

import java.util.concurrent.locks.ReentrantLock;

public class ReverseProxy {

    public static MonitorTable tabelaMonitorizacao;
    public static ReentrantLock lockTabela;

    public static void main(String[] args) {
        tabelaMonitorizacao = new MonitorTable();
        lockTabela = new ReentrantLock();
        System.out.println("[ReverseProxy] A iniciar MonitorUDP...");
        new MonitorUDPProxy(tabelaMonitorizacao,lockTabela).start();
        System.out.println("[ReverseProxy] MonitorUDP iniciado.");

    }
}
