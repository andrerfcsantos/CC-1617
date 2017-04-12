package WebServer;

import java.io.*;
import java.net.*;
import Utils.*;

public class ProbeReplier extends Thread {

    private DatagramSocket socket;


    public ProbeReplier() {
        try {
            socket = new DatagramSocket(5555);
        } catch (SocketException e) {
            System.err.println("[ProbeReplier] Erro ao abrir socket para respostas de probe.");
        }

    }

    @Override
    public void run() {
        ByteArrayOutputStream b_out;
        ObjectOutputStream o_out;
        ByteArrayInputStream b_in;
        ObjectInputStream o_in;
        DatagramPacket pacote_in, pacote_out;
        PDU pdu_pedido, pdu_resposta;
        byte b_array[];
        byte buf_in[];

        while (true) {

            try {
                buf_in = new byte[1024];
                pacote_in = new DatagramPacket(buf_in, buf_in.length);
                socket.receive(pacote_in);
                b_in = new ByteArrayInputStream(buf_in,0,pacote_in.getLength());
                o_in = new ObjectInputStream(b_in);
                pdu_pedido = (PDU) o_in.readObject();

                pdu_resposta = new PDU(pdu_pedido.getSeq(),TipoPDU.PROB_REPLY);

                b_out = new ByteArrayOutputStream();
                o_out = new ObjectOutputStream(b_out);

                o_out.writeObject(pdu_resposta);
                o_out.flush();

                b_array = b_out.toByteArray();
                pacote_out = new DatagramPacket(b_array, b_array.length, pacote_in.getAddress(), 5555);
                socket.send(pacote_out);

            } catch (IOException e) {
                System.err.println("[ProbeReplier] Erro ao abrir ObjectInputStream");
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("[ProbeReplier] Erro ao criar PDU do pacote");
                break;
            }
        }

        socket.close();

    }
}
