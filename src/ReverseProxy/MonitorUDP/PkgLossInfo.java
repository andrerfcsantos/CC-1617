package ReverseProxy.MonitorUDP;


public class PkgLossInfo {
    private PkgStatus status;
    private int totalPackages;
    private int pkgCount;


    public PkgLossInfo(){
        this(PkgStatus.LOST,0,0);
    }

    public PkgLossInfo(PkgStatus status, int pkgCount, int totalPackages) {
        this.status = status;
        this.pkgCount = pkgCount;
        this.totalPackages = totalPackages;
    }

    public PkgStatus getStatus() {
        return status;
    }

    public void setStatus(PkgStatus status) {
        this.status = status;
    }

    public int getTotalPackages() {
        return totalPackages;
    }

    public void setTotalPackages(int totalPackages) {
        this.totalPackages = totalPackages;
    }

    public int getPkgCount() {
        return pkgCount;
    }

    public void setPkgCount(int pkgCount) {
        this.pkgCount = pkgCount;
    }
}
