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

        try {
            serverSocket = new ServerSocket(80);

            while(true){
                Socket sockCliente = serverSocket.accept();
                new ReverseProxyWriter(sockCliente).start();
            }

        } catch (IOException e) {
            System.err.println("[WebServer] Erro ao abrir socket TCP");
        }
    }
}
