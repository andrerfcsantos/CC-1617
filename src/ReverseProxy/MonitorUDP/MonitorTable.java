package ReverseProxy.MonitorUDP;


import java.net.InetAddress;
import java.time.Duration;
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

    public Instant getLastAvailable(InetAddress ip, Instant ins){
        MonitorTableEntry entry = tabela.get(ip);
        return entry.getLastAvailable();
    }

    public int getLastSeqReceived(InetAddress ip) {
        MonitorTableEntry entry = tabela.get(ip);
        return entry.getLastSeqReceived();
    }

    public void setLastSeqReceived(InetAddress ip,int lastSeqReceived) {
        MonitorTableEntry entry = tabela.get(ip);
        entry.setLastSeqReceived(lastSeqReceived);
    }

    public int getLastSeqSent(InetAddress ip) {
        MonitorTableEntry entry = tabela.get(ip);
        return entry.getLastSeqReceived();
    }


    public Instant getTimeLastSeqReceived(InetAddress ip) {
        MonitorTableEntry entry = tabela.get(ip);
        return entry.getTimeLastSeqReceived();
    }

    public void setTimeLastSeqReceived(InetAddress ip,Instant timeLastSeqReceived) {
        MonitorTableEntry entry = tabela.get(ip);
        entry.setTimeLastSeqReceived(timeLastSeqReceived);
    }

    public Instant getTimeLastSeqSent(InetAddress ip) {
        MonitorTableEntry entry = tabela.get(ip);
        return entry.getTimeLastSeqSent();
    }


    public void setSeqSent(InetAddress ip,int lastSeqSent,Instant timeLastSeqSent){
        MonitorTableEntry entry = tabela.get(ip);
        entry.setTimeLastSeqSent(timeLastSeqSent);
        entry.setLastSeqSent(lastSeqSent);
        entry.incPackagesSent();
    }

    public void setSeqReceived(InetAddress ip,int lastSeqReceived,Instant timeLastSeqReceived){
        Duration rtt;

        MonitorTableEntry entry = tabela.get(ip);
        if(lastSeqReceived==entry.getLastSeqSent()){
            rtt=Duration.between(entry.getTimeLastSeqSent(),timeLastSeqReceived);
            entry.addRTTEntry(rtt);
        }
        entry.setLastSeqReceived(lastSeqReceived);
        entry.setTimeLastSeqReceived(timeLastSeqReceived);
        entry.setLastAvailable(timeLastSeqReceived);
    }

    public void addRTTEntry(InetAddress ip,Duration d){
        MonitorTableEntry entry = tabela.get(ip);
        entry.addRTTEntry(d);
    }

    public Duration getAverageRTT(InetAddress ip){
        MonitorTableEntry entry = tabela.get(ip);
        return entry.getAverageRTT();
    }

    public int getPackagesSent(InetAddress ip) {
        MonitorTableEntry entry = tabela.get(ip);
        return entry.getPackagesSent();
    }

    public void incPackagesSent(InetAddress ip) {
        MonitorTableEntry entry = tabela.get(ip);
        entry.incPackagesSent();
    }

    public int getPackagesLost(InetAddress ip) {
        MonitorTableEntry entry = tabela.get(ip);
        return entry.getPackagesLost();
    }

    public void incPackagesLost(InetAddress ip) {
        MonitorTableEntry entry = tabela.get(ip);
        entry.incPackagesLost();
    }

}
