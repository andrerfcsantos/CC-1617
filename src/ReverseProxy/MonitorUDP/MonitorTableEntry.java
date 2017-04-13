package ReverseProxy.MonitorUDP;

import ReverseProxy.ReverseProxy;

import java.time.Duration;
import java.time.Instant;

public class MonitorTableEntry {

    private Instant lastAvailable;
    private int lastSeqReceived;
    private int lastSeqSent;
    private Instant timeLastSeqReceived;
    private Instant timeLastSeqSent;
    private RTTMonitor monitorRTT;
    private int packagesSent;
    private int packagesLost;

    public MonitorTableEntry(){
        this(Instant.EPOCH, -1, -1,Instant.EPOCH,Instant.EPOCH,10);
    }

    public MonitorTableEntry(Instant lastAv){
        this(lastAv, -1, -1,Instant.EPOCH,Instant.EPOCH,10);
    }

    public MonitorTableEntry(Instant lastAv, int rttEntries){
        this(lastAv, -1, -1,Instant.EPOCH,Instant.EPOCH,rttEntries);
    }

    public MonitorTableEntry(Instant lastAvailable, int lastSeqReceived, int lastSeqSent,
                             Instant timeLastSeqReceived, Instant timeLastSeqSent, int rttEntries) {
        this.lastAvailable = lastAvailable;
        this.lastSeqReceived = lastSeqReceived;
        this.lastSeqSent = lastSeqSent;
        this.timeLastSeqReceived = timeLastSeqReceived;
        this.timeLastSeqSent = timeLastSeqSent;
        this.monitorRTT = new RTTMonitor(rttEntries);
        this.packagesSent = 0;
        this.packagesLost = 0;
    }

    public Instant getLastAvailable() {
        return lastAvailable;
    }

    public void setLastAvailable(Instant lastAvailable) {
        this.lastAvailable = lastAvailable;
    }

    public int getLastSeqReceived() {
        return lastSeqReceived;
    }

    public void setLastSeqReceived(int lastSeqReceived) {
        this.lastSeqReceived = lastSeqReceived;
    }

    public int getLastSeqSent() {
        return lastSeqSent;
    }

    public void setLastSeqSent(int lastSeqSent) {
        this.lastSeqSent = lastSeqSent;
    }

    public Instant getTimeLastSeqReceived() {
        return timeLastSeqReceived;
    }

    public void setTimeLastSeqReceived(Instant timeLastSeqReceived) {
        this.timeLastSeqReceived = timeLastSeqReceived;
    }

    public Instant getTimeLastSeqSent() {
        return timeLastSeqSent;
    }

    public void setTimeLastSeqSent(Instant timeLastSeqSent) {
        this.timeLastSeqSent = timeLastSeqSent;
    }

    public void addRTTEntry(Duration d){
        monitorRTT.addEntry(d);
    }

    public Duration getAverageRTT(){
        return monitorRTT.getAverageRTT();
    }

    public int getNEntriesRTT(){
        return monitorRTT.getValidEntries();
    }

    public int getPackagesSent() {
        return packagesSent;
    }

    public void incPackagesSent() {
        this.packagesSent += 1;
    }

    public int getPackagesLost() {
        return packagesLost;
    }

    public void incPackagesLost() {
        this.packagesLost+=1;
    }
}
