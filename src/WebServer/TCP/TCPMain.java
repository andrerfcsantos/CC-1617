package WebServer.TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Andre on 06-05-2017.
 */
public class TCPMain extends Thread{

    ServerSocket serverSocket;

    @Override
    public void run() {
        String str_pedido;
        String str_resposta = "ECHO REPLY";
        Socket sockCliente;
        try {
            serverSocket = new ServerSocket(80);

            while(true){
                sockCliente = serverSocket.accept();

                OutputStream streamEscrita = sockCliente.getOutputStream();
                InputStream streamLeitura= sockCliente.getInputStream();

                PrintWriter writer = new PrintWriter(streamEscrita);
                BufferedReader reader = new BufferedReader(new InputStreamReader(streamLeitura));

                while ((str_pedido = reader.readLine()) != null){
                    System.out.println("[WebServer] Pedido lido: "+ str_pedido);
                    writer.println(str_resposta);
                    writer.flush();
                    System.out.println("[WebServer] Resposta enviada: "+ str_resposta);
                }

                writer.close();
                reader.close();
                streamEscrita.close();
                streamLeitura.close();
            }

        } catch (IOException e) {
            System.err.println("[WebServer] Erro ao abrir socket TCP");
        }
    }
}
