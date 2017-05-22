package ReverseProxy.TCP;

import ReverseProxy.MonitorUDP.MonitorTableEntry;

import java.io.*;
import java.net.Socket;

public class TCPClientWriter extends Thread{

    public MonitorTableEntry entradaTabela;
    public Socket sockCliente;
    public Socket sockWebServer;
    public int READ_SIZE = 1024;


    public TCPClientWriter(MonitorTableEntry entradaTabela, Socket sockCliente, Socket sockWebServer){
        this.sockCliente = sockCliente;
        this.sockWebServer = sockWebServer;
        this.entradaTabela = entradaTabela;
    }

    @Override
    public void run() {
        byte buffer[] = new byte[READ_SIZE];
        int bLidos;

        try {
            OutputStream streamEscrita = sockCliente.getOutputStream();
            InputStream streamLeitura= sockWebServer.getInputStream();

            while ((bLidos = streamLeitura.read(buffer,0,READ_SIZE)) != -1) {
                System.out.println("[TCPClientWriter] Resposta de  " + bLidos  + " bytes lida." );
                streamEscrita.write(buffer,0,bLidos);
                System.out.println("[TCPClientWriter] Resposta enviada para cliente.");
                streamEscrita.flush();
            }

            entradaTabela.lock();
            entradaTabela.decNConexoes();
            entradaTabela.unlock();

            sockCliente.shutdownOutput();
            sockCliente.close();
            sockWebServer.close();

        } catch (IOException e) {
            System.err.println("[TCPClientWriter] Erro a abrir input stream");
        }


    }


}
