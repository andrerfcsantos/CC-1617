package WebServer.TCP;

import java.io.*;
import java.net.Socket;

/**
 * Created by Andre on 07-05-2017.
 */
public class ReverseProxyWriter extends Thread{

    public Socket socketRP;

    public ReverseProxyWriter(Socket socketRP){
        this.socketRP = socketRP;
    }

    @Override
    public void run() {
        String str_pedido;
        String str_resposta = "ECHO REPLY";

        try {
            OutputStream streamEscrita = socketRP.getOutputStream();
            InputStream streamLeitura= socketRP.getInputStream();

            PrintWriter writer = new PrintWriter(streamEscrita);
            BufferedReader reader = new BufferedReader(new InputStreamReader(streamLeitura));

            while ((str_pedido = reader.readLine()) != null){
                System.out.println("[WebServer] Pedido lido: "+ str_pedido);
                writer.println(str_resposta);
                writer.flush();
                System.out.println("[WebServer] Resposta enviada: "+ str_resposta);
            }

            socketRP.shutdownOutput();
            socketRP.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
