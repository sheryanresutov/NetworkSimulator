package Domain;

public class FlowData {

    private double rate;
    private double size;
    private double delay;

    public FlowData(double flowRateMbps, double windowSize, double pktDelay) {
        this.rate = flowRateMbps;
        this.size = windowSize;
        this.delay = pktDelay;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }
}
