package ReverseProxy.TCP;

import ReverseProxy.MonitorUDP.MonitorTable;
import ReverseProxy.MonitorUDP.MonitorTableEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class TCPMain extends Thread {

    ServerSocket ss;
    MonitorTable tabelaMonitorizacao;

    public TCPMain (MonitorTable tabelaMonitorizacao){
        this.tabelaMonitorizacao = tabelaMonitorizacao;
    }


    @Override
    public void run() {
        try {
            Socket sockCliente;
            ss = new ServerSocket(80);

            while(true){
                PrintWriter choiceLog =new PrintWriter(
                        new FileOutputStream(
                                new File("server-choice-log.txt"),true
                        ));
                DecimalFormat df = new DecimalFormat("#.#####");
                df.setRoundingMode(RoundingMode.CEILING);
                sockCliente = ss.accept();

                tabelaMonitorizacao.lock();
                InetAddress ipMelhorWebServer = null;
                MonitorTableEntry melhorEntrada = null;
                double melhorPontuacao = Double.MAX_VALUE;

                for(Map.Entry<InetAddress,MonitorTableEntry> entrada: tabelaMonitorizacao.tabela.entrySet()){
                    double pontuacao=0;
                    double p_ld, p_rtt,p_perdas,p_nConexoes;

                    InetAddress ip = entrada.getKey();
                    MonitorTableEntry entradaTabela = entrada.getValue();

                    Duration lastDisp = Duration.between(entradaTabela.getLastAvailable(),Instant.now());
                    Duration rtt = entradaTabela.getAverageRTT(10);
                    int perdas = entradaTabela.getPackagesLost(10).getPkgCount();
                    int nConexoes = entradaTabela.getnConexoes();

                    p_ld = (double) 0.20*lastDisp.toMillis()/ 3000.0;
                    p_rtt = (double) 0.20*rtt.toMillis()/150.0;
                    p_perdas = (double) 0.30*perdas/5.0;
                    p_nConexoes = (double) 0.30*nConexoes/3.0;

                    pontuacao = p_ld + p_rtt + p_perdas + p_nConexoes;
                    choiceLog.println("=== NOVO PEDIDO ===");
                    choiceLog.println("Pontuacao para " + ip + " = " +  df.format(pontuacao) + " "+
                                                    "disp: " + df.format(p_ld)  + " (" + lastDisp.toMillis() + " ms), "+
                                                    "rtt: " + df.format(p_rtt) + " (" + rtt.toMillis() + " ms), "+
                                                    "perdas: " + df.format(p_perdas) + " (" + perdas + "), " +
                                                    "con: " + df.format(p_nConexoes) +  " (" + nConexoes + ")" );
                    choiceLog.flush();

                    if(pontuacao < melhorPontuacao) {
                        ipMelhorWebServer = ip;
                        melhorEntrada = entradaTabela;
                        melhorPontuacao = pontuacao;
                    }
                }
                tabelaMonitorizacao.unlock();

                choiceLog.println(" ---> Escolhido melhor server: " +  ipMelhorWebServer + " com pontuacao " + melhorPontuacao);
                choiceLog.println("============");
                Socket sockWebServer = new Socket(ipMelhorWebServer,80);

                melhorEntrada.lock();
                melhorEntrada.incNConexoes();
                melhorEntrada.unlock();

                new TCPClientListener(sockCliente,sockWebServer).start();
                new TCPClientWriter(melhorEntrada,sockCliente,sockWebServer).start();
                choiceLog.close();
            }


        } catch (IOException e) {
            System.err.println("[TCPMain] Erro ao abrir Socket");
        }
    }
}
