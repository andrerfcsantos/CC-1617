package ReverseProxy;

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

    public void addEntry(InetAddress ip, Instant ins){
        tabela.put(ip,new MonitorTableEntry());
    }

    public void setLastAvailable(InetAddress ip, Instant ins){
        MonitorTableEntry entry = tabela.get(ip);
        entry.lastAvailable=ins;
    }

    public Instant getLastAvailable(InetAddress ip, Instant ins){
        MonitorTableEntry entry = tabela.get(ip);
        return entry.lastAvailable;
    }

}
