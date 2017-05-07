package ReverseProxy.TCP;

import ReverseProxy.MonitorUDP.MonitorTableEntry;

import java.io.*;
import java.net.Socket;

public class TCPClientListener extends Thread {

    public Socket sockCliente;
    public Socket sockWebServer;
    public MonitorTableEntry entradaTabela;
    public int READ_SIZE = 1024;


    public TCPClientListener(MonitorTableEntry entradaTabela, Socket sockCliente, Socket sockWebServer){
        this.sockCliente = sockCliente;
        this.sockWebServer = sockWebServer;
        this.entradaTabela = entradaTabela;
    }

    @Override
    public void run() {
        //byte buffer[] = new byte[READ_SIZE];
        //int bLidos;
        String str_pedido;
        try {
            OutputStream streamEscrita = sockWebServer.getOutputStream();
            InputStream streamLeitura= sockCliente.getInputStream();

            PrintWriter writer = new PrintWriter(streamEscrita);
            BufferedReader reader = new BufferedReader(new InputStreamReader(streamLeitura));

            while ((str_pedido = reader.readLine()) != null){
                System.out.println("[TCPClientListener] Pedido lido: " + str_pedido);
                writer.println(str_pedido);
                System.out.println("[TCPClientListener] Pedido enviado para WebServer.");
                writer.flush();
            }

            entradaTabela.lock();
            entradaTabela.decNConexoes();
            entradaTabela.unlock();

            writer.close();
            reader.close();
            streamEscrita.close();
            streamLeitura.close();
        } catch (IOException e) {
            System.err.println("[TCPClientListener] Erro a abrir input stream");
        }


    }
}
