package ReverseProxy.TCP;

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
        //byte buffer[] = new byte[READ_SIZE];
        //int bLidos;
        String str_pedido;
        try {
            OutputStream streamEscrita = sockWebServer.getOutputStream();
            InputStream streamLeitura= sockCliente.getInputStream();

            PrintWriter writer = new PrintWriter(streamEscrita);
            BufferedReader reader = new BufferedReader(new InputStreamReader(streamLeitura));

            while ((str_pedido = reader.readLine()) != null){
                writer.println(str_pedido);
                writer.flush();
            }

            writer.close();
            reader.close();
            streamEscrita.close();
            streamLeitura.close();
        } catch (IOException e) {
            System.err.println("[TCPClientListener] Erro a abrir input stream");
        }


    }
}
