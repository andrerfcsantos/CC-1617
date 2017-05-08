package ReverseProxy.TCP;

import ReverseProxy.MonitorUDP.MonitorTable;
import ReverseProxy.MonitorUDP.MonitorTableEntry;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class ClientReaderHandler implements HttpHandler {

    MonitorTable tabelaMonitorizacao;

    public ClientReaderHandler(MonitorTable tabelaMonitorizacao){
        this.tabelaMonitorizacao = tabelaMonitorizacao;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {

        tabelaMonitorizacao.lock();
        InetAddress ipMelhorWebServer = null;
        MonitorTableEntry melhorEntrada = null;
        double melhorPontuacao = Double.MAX_VALUE;

        for(Map.Entry<InetAddress,MonitorTableEntry> entrada: tabelaMonitorizacao.tabela.entrySet()){
            double pontuacao=0;
            double p_ld, p_rtt,p_perdas,p_nConexoes;

            InetAddress ip = entrada.getKey();
            MonitorTableEntry entradaTabela = entrada.getValue();

            Duration lastDisp = Duration.between(entradaTabela.getLastAvailable(), Instant.now());
            Duration rtt = entradaTabela.getAverageRTT(10);
            int perdas = entradaTabela.getPackagesLost(10).getPkgCount();
            int nConexoes = entradaTabela.getnConexoes();

            p_ld = (double) 0.20*lastDisp.toMillis()/ 3000.0;
            p_rtt = (double) 0.20*rtt.toMillis()/150.0;
            p_perdas = (double) 0.30*perdas/5.0;
            p_nConexoes = (double) 0.30*nConexoes/3.0;

            pontuacao = p_ld + p_rtt + p_perdas + p_nConexoes;

            System.out.println("[TCPMain] Pontuacao para " + ip + " = " +  pontuacao +
                    "( " + p_ld  + " , " + p_rtt  + " , " + p_perdas + " , " + p_nConexoes +  ")" );

            if(pontuacao < melhorPontuacao) {
                ipMelhorWebServer = ip;
                melhorEntrada = entradaTabela;
                melhorPontuacao = pontuacao;
            }
        }
        tabelaMonitorizacao.unlock();

        System.out.println("[TCPMain] Escolhido melhor server: " +  ipMelhorWebServer + " com pontuacao " + melhorPontuacao);

        melhorEntrada.lock();
        melhorEntrada.incNConexoes();
        melhorEntrada.unlock();

        URL url = new URL("http", ipMelhorWebServer.getHostName(),
                                            80, ex.getRequestURI().getPath());

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoInput(true);


        BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        ex.sendResponseHeaders(200, response.length());
        OutputStream os = ex.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}