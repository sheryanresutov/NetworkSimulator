package Domain;

public class LinkData {

    private double rate;
    private double buffer;
    private double loss;

    public LinkData(double rateMbps, double buffer, int pktLoss) {
        this.rate = rateMbps;
        this.buffer = buffer;
        this.loss = pktLoss;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getBuffer() {
        return buffer;
    }

    public void setBuffer(double buffOcc) {
        buffer = buffOcc;
    }

    public double getLoss() {
        return loss;
    }

    public void setLoss(double loss) {
        this.loss = loss;
    }
}
