package ReverseProxy.MonitorUDP;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorUDPProxy extends Thread{

    private MonitorTable tabelaMonitorizacao;
    private DatagramSocket socketUDP;

    public MonitorUDPProxy(MonitorTable tabela){
        this.tabelaMonitorizacao=tabela;
    }

    @Override
    public void run() {
        try {
            socketUDP = new DatagramSocket(5555);
            System.out.println("[MonitorUDPProxy] A iniciar ProxyReceiver...");
            new ProxyReceiver(tabelaMonitorizacao,socketUDP).start();
            System.out.println("[MonitorUDPProxy] ProxyReceiver iniciado.");
        } catch (SocketException e) {
            System.err.println("[MonitorUDPProxy] Erro ao abrir socket UDP.");
        }


    }
}
