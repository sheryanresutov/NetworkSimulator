package Domain;

public class FlowData {

    private String flowId;
    private double flowRate;
    private double winSize;
    private double packetDelay;

    public FlowData(String name, double flowRateMbps, double windowSize, double pktDelay) {
        this.flowId = name;
        this.flowRate = flowRateMbps;
        this.winSize = windowSize;
        this.packetDelay = pktDelay;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public double getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(double flowRate) {
        this.flowRate = flowRate;
    }

    public double getWinSize() {
        return winSize;
    }

    public void setWinSize(double winSize) {
        this.winSize = winSize;
    }

    public double getPacketDelay() {
        return packetDelay;
    }

    public void setPacketDelay(double packetDelay) {
        this.packetDelay = packetDelay;
    }
}
