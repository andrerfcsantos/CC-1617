package ReverseProxy.MonitorUDP;


import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorTable {
    private HashMap<InetAddress,MonitorTableEntry> tabela;
    private ReentrantLock lock;

    public MonitorTable(){
        lock = new ReentrantLock();
        tabela = new HashMap<>();
    }

    public boolean ipExists(InetAddress ip){
        return tabela.containsKey(ip);
    }

    public void addEntry(InetAddress ip, MonitorTableEntry entrada){
        tabela.put(ip,entrada);
    }

    public MonitorTableEntry getEntry(InetAddress ip){
        return tabela.get(ip);
    }

    public void deleteEntry(InetAddress ip){
        MonitorTableEntry entrada = tabela.get(ip);
        entrada.lock();
        tabela.remove(ip);
        entrada.unlock();
    }

    public void lock(){
        lock.lock();
    }

    public void unlock(){
        lock.unlock();
    }

}
