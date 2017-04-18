package ReverseProxy.MonitorUDP;


import java.net.InetAddress;
import java.time.Instant;
import java.util.HashMap;

public class MonitorTable {
    private HashMap<InetAddress,MonitorTableEntry> tabela;

    public MonitorTable(){
        tabela = new HashMap<>();
    }

    public boolean ipExists(InetAddress ip){
        return tabela.containsKey(ip);
    }

    public void addEntry(InetAddress ip, MonitorTableEntry entrada){
        tabela.put(ip,entrada);
    }

    public void setLastAvailable(InetAddress ip, Instant ins){
        MonitorTableEntry entry = tabela.get(ip);
        entry.setLastAvailable(ins);
    }

    public MonitorTableEntry getEntry(InetAddress ip){
        return tabela.get(ip);
    }

    public void deleteEntry(InetAddress ip){
        tabela.remove(ip);
    }


}
