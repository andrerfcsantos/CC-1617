package ReverseProxy.MonitorUDP;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorTableEntry {

    private Instant lastAvailable;
    private int lastSeqReceived;
    private int lastSeqSent;
    private Instant timeLastSeqReceived;
    private Instant timeLastSeqSent;
    private RTTMonitor monitorRTT;
    private PkgLossMonitor pkgLossMonitor;
    private ReentrantLock lock;

    public MonitorTableEntry(){
        this(Instant.EPOCH, -1, -1,Instant.EPOCH,Instant.EPOCH,100,100);
    }


    public MonitorTableEntry(Instant lastAvailable, int lastSeqReceived, int lastSeqSent,
                             Instant timeLastSeqReceived, Instant timeLastSeqSent,
                             int rttEntries, int pkgLossEntries) {
        this.lastAvailable = lastAvailable;
        this.lastSeqReceived = lastSeqReceived;
        this.lastSeqSent = lastSeqSent;
        this.timeLastSeqReceived = timeLastSeqReceived;
        this.timeLastSeqSent = timeLastSeqSent;
        this.monitorRTT = new RTTMonitor(rttEntries);
        this.pkgLossMonitor = new PkgLossMonitor(pkgLossEntries);
        this.lock = new ReentrantLock();
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
        return monitorRTT.getnCurrentEntries();
    }

    public void reportPkgStatus(PkgStatus s){
        pkgLossMonitor.reportPkgStatus(s);
    }

    public PkgLossInfo getPackagesLost() {
        return pkgLossMonitor.getPkgLoss();
    }

    public PkgLossInfo getPackagesLost(int nLastEntries) {
        return pkgLossMonitor.getPkgLoss(nLastEntries,PkgStatus.LOST);
    }

    public int getTotalPackages() {
        return pkgLossMonitor.getTotalPackages();
    }

    public void lock(){
        this.lock.lock();
    }

    public void unlock(){
        this.lock.unlock();
    }

}
