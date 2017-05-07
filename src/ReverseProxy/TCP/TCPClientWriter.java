package ReverseProxy.TCP;

import java.io.*;
import java.net.Socket;

public class TCPClientWriter extends Thread{

    public Socket sockCliente;
    public Socket sockWebServer;
    public int READ_SIZE = 1024;


    public TCPClientWriter(Socket sockCliente, Socket sockWebServer){
        this.sockCliente = sockCliente;
        this.sockWebServer = sockWebServer;
    }

    @Override
    public void run() {
        //byte buffer[] = new byte[READ_SIZE];
        //int bLidos;

        String str_resposta;
        try {
            OutputStream streamEscrita = sockCliente.getOutputStream();
            InputStream streamLeitura= sockWebServer.getInputStream();

            PrintWriter writer = new PrintWriter(streamEscrita);
            BufferedReader reader = new BufferedReader(new InputStreamReader(streamLeitura));

            while ((str_resposta = reader.readLine()) != null){
                System.out.println("[TCPClientWriter] Resposta lido: " + str_resposta);
                writer.println(str_resposta);
                System.out.println("[TCPClientWriter] Resposta enviada para cliente.");
                writer.flush();
            }

            writer.close();
            reader.close();
            streamEscrita.close();
            streamLeitura.close();
        } catch (IOException e) {
            System.err.println("[TCPClientWriter] Erro a abrir input stream");
        }


    }


}
