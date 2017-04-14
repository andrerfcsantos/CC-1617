package ReverseProxy.MonitorUDP;

public class PkgLossMonitor {

    private int totalPackages;
    private int allTimePkgsLost;

    private int nMaxPkgsMonitored;
    private int nCurrentPkgs;

    private int cursor;
    private PkgStatus[] pkgsStatus;

    public PkgLossMonitor() {
        this(100);
    }

    public PkgLossMonitor(int nMaxPkgsMonitored) {
        this.totalPackages = 0;
        this.allTimePkgsLost = 0;
        this.nMaxPkgsMonitored = nMaxPkgsMonitored;
        this.nCurrentPkgs = 0;
        this.pkgsStatus = new PkgStatus[nMaxPkgsMonitored];
        this.cursor = 0;
    }

    public void reportPkgStatus(PkgStatus s) {
        pkgsStatus[cursor] = s;

        cursor = (cursor + 1) % nMaxPkgsMonitored;

        if (nCurrentPkgs < nMaxPkgsMonitored) nCurrentPkgs++;

        if(s==PkgStatus.LOST){
            allTimePkgsLost +=1;
        }

        totalPackages+=1;
    }

    public PkgLossInfo getPkgLoss() {
        return getPkgLoss(nCurrentPkgs,PkgStatus.LOST);
    }

    public PkgLossInfo getPkgLoss(int nLastEntries, PkgStatus s) {
        PkgLossInfo res;

        int count = 0;

        if (nLastEntries > nCurrentPkgs) {
            nLastEntries = nCurrentPkgs;
        }

        for (int i = 0; i < nLastEntries; i++) {
            if (pkgsStatus[(cursor - (i + 1)) % nMaxPkgsMonitored]==s) {
                count += 1;
            }
        }
        res = new PkgLossInfo(s,count,nLastEntries);

        return res;
    }


    public float getAllTimePkgLoss() {
        return (totalPackages!=0) ? (float) allTimePkgsLost / totalPackages : 0.0f;
    }

    public int getTotalPackages() {
        return totalPackages;
    }

    public int getAllTimePkgsLost() {
        return allTimePkgsLost;
    }

    public int getnMaxPkgsMonitored() {
        return nMaxPkgsMonitored;
    }

    public int getnCurrentPkgs() {
        return nCurrentPkgs;
    }

}
