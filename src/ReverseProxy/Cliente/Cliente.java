package ReverseProxy.Cliente;

import java.io.*;
import java.net.Socket;

/**
 * Created by Andre on 07-05-2017.
 */
public class Cliente {

    public static void main(String[] args) {

        try {
            //String str_pedido = "ECHO REQUEST";
            String str_resposta;

            Socket socket = new Socket(args[0],80);

            OutputStream sockOutStream = socket.getOutputStream();
            InputStream sockInpStream = socket.getInputStream();

            PrintWriter writer = new PrintWriter(sockOutStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(sockInpStream));


            String str_user;
            BufferedReader reader_usr = new BufferedReader(new InputStreamReader(System.in));
            while((str_user = reader_usr.readLine())!=null){

                writer.println(str_user);
                writer.flush();
                System.out.println("[CLIENTE] Pedido enviado: " + str_user);

                str_resposta = reader.readLine();
                System.out.println("[CLIENTE] Resposta recebida: " + str_resposta);
            }

            socket.close();

        } catch (IOException e) {
            System.err.println("[CLIENTE] Erro ao abrir socket");
        }

    }

}
