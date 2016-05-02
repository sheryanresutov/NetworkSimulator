package Domain;

public class LinkData {

    private String linkId;
    private double linkRate;
    private double BuffOcc;
    private double packetLoss;

    public LinkData(String name, double rateMbps, double buffOcc, int pktLoss) {
        this.linkId = name;
        this.linkRate = rateMbps;
        this.BuffOcc = buffOcc;
        this.packetLoss = pktLoss;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public double getLinkRate() {
        return linkRate;
    }

    public void setLinkRate(double linkRate) {
        this.linkRate = linkRate;
    }

    public double getBuffOcc() {
        return BuffOcc;
    }

    public void setBuffOcc(double buffOcc) {
        BuffOcc = buffOcc;
    }

    public double getPacketLoss() {
        return packetLoss;
    }

    public void setPacketLoss(double packetLoss) {
        this.packetLoss = packetLoss;
    }
}
