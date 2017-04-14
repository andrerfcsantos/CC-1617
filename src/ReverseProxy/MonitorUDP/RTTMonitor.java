package ReverseProxy.MonitorUDP;

import java.time.Duration;

public class RTTMonitor {

    private int totalPackages;
    private Duration allTimeDuration;

    private int nMaxEntriesMonitored;
    private int nCurrentEntries;

    private int cursor;
    private Duration[] durations;

    public RTTMonitor() {
        this(100);
    }

    public RTTMonitor(int nMaxEntriesMonitored) {
        this.totalPackages = 0;
        this.allTimeDuration = Duration.ZERO;
        this.nMaxEntriesMonitored = nMaxEntriesMonitored;
        this.nCurrentEntries = 0;
        this.durations = new Duration[nMaxEntriesMonitored];
        this.cursor = 0;
    }

    public void addEntry(Duration d) {
        totalPackages += 1;
        allTimeDuration = allTimeDuration.plus(d);

        durations[cursor] = d;
        cursor = (cursor + 1) % nMaxEntriesMonitored;
        if (nCurrentEntries < nMaxEntriesMonitored) {
            nCurrentEntries++;
        }
    }

    public int getTotalPackages() {
        return totalPackages;
    }

    public int getnMaxEntriesMonitored() {
        return nMaxEntriesMonitored;
    }

    public Duration[] getDurations() {
        return durations;
    }

    public Duration getAverageRTT() {
        return getAverageRTT(nCurrentEntries);
    }

    public Duration getAverageRTT(int nLastEntries) {
        Duration res = Duration.ZERO;

        if(nLastEntries > nCurrentEntries){
            nLastEntries = nCurrentEntries;
        }

        if (nLastEntries != 0) {

            for (int i = 0; i < nLastEntries; i++) {
                res = res.plus(durations[(cursor - (i+1)) % nMaxEntriesMonitored]);
            }

            res = res.dividedBy(nLastEntries);
        }

        return res;
    }

    public Duration getAllTimeAverageRTT() {
        return (totalPackages!=0) ? allTimeDuration.dividedBy(totalPackages) : Duration.ZERO;
    }

    public int getnCurrentEntries() {
        return nCurrentEntries;
    }

}
