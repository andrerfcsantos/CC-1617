package ReverseProxy.MonitorUDP;

import ReverseProxy.ReverseProxy;

import java.time.Duration;

public class RTTMonitor {

    private int nEntries;
    private int validEntries;
    private int cursor;
    private Duration[] durations;

    public RTTMonitor() {
        this(10);
    }

    public RTTMonitor(int nEntries) {
        this.nEntries = nEntries;
        durations = new Duration[nEntries];
        validEntries=0;
        cursor=0;
    }

    public void addEntry(Duration d){
        durations[cursor] = d;
        cursor = (cursor+1)%nEntries;
        if(validEntries < nEntries){
            validEntries++;
        }
    }

    public Duration getAverageRTT(){
        Duration res = Duration.ZERO;

        for(int i=0; i<validEntries;i++){
            res = res.plus(durations[i]);
        }

        res = res.dividedBy(validEntries);
        return res;
    }

    public int getValidEntries(){
        return validEntries;
    }

}
