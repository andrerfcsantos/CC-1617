package ReverseProxy;


public class ReverseProxy {

    public static MonitorTable tabelaMonitorizacao;


    public static void main(String[] args) {
        tabelaMonitorizacao = new MonitorTable();
        System.out.println("[ReverseProxy] A iniciar ReverseProxy...");
        System.out.println("[ReverseProxy] A iniciar ProxyReceiver...");
        new ProxyReceiver(tabelaMonitorizacao).start();
        System.out.println("[ReverseProxy] ProxyReceiver iniciado.");
        System.out.println("[ReverseProxy] ReverseProxy iniciado.");

    }
}
