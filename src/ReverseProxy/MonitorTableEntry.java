package ReverseProxy;

import java.time.Instant;

public class MonitorTableEntry {

    Instant lastAvailable;

    public MonitorTableEntry(){
        lastAvailable = Instant.EPOCH;
    }

    public MonitorTableEntry(Instant lastAv){
        lastAvailable = lastAv;
    }


}
