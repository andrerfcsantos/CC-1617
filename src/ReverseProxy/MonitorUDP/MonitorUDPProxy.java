package ReverseProxy.MonitorUDP;

import java.util.concurrent.locks.ReentrantLock;

public class MonitorUDPProxy extends Thread{

    private MonitorTable tabelaMonitorizacao;
    private ReentrantLock lockTabela;

    public MonitorUDPProxy(MonitorTable tabela,ReentrantLock lockTabela){
        this.tabelaMonitorizacao=tabela;
        this.lockTabela = lockTabela;
    }

    @Override
    public void run() {
        System.out.println("[MonitorUDPProxy] A iniciar ProxyReceiver...");
        new ProxyReceiver(tabelaMonitorizacao,lockTabela).start();
        System.out.println("[MonitorUDPProxy] ProxyReceiver iniciado.");
    }
}
