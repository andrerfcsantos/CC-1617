package ReverseProxy.TCP;

import ReverseProxy.MonitorUDP.MonitorTableEntry;

import java.io.*;
import java.net.Socket;

public class TCPClientListener extends Thread {

    public Socket sockCliente;
    public Socket sockWebServer;
    public int READ_SIZE = 1024;


    public TCPClientListener(Socket sockCliente, Socket sockWebServer){
        this.sockCliente = sockCliente;
        this.sockWebServer = sockWebServer;
    }

    @Override
    public void run() {
        byte buffer[] = new byte[READ_SIZE];
        int bLidos;
        try {
            OutputStream streamEscrita = sockWebServer.getOutputStream();
            InputStream streamLeitura= sockCliente.getInputStream();


            while ((bLidos = streamLeitura.read(buffer,0,READ_SIZE)) != -1){
                System.out.println("[TCPClientListener] Lido pedido de " + bLidos + " bytes." );
                streamEscrita.write(buffer,0,bLidos);
                System.out.println("[TCPClientListener] Pedido enviado para WebServer.");
                streamEscrita.flush();
            }



            sockCliente.shutdownInput();
            sockWebServer.shutdownOutput();

        } catch (IOException e) {
            System.err.println("[TCPClientListener] Erro a abrir input stream");
        }


    }
}
