package Domain;

public class Flow {

    //have a source and destination address

    private String id;
    private int delay;
    private String src;
    private String dest;
    private int dataSize;
    private String congCtrlAlgo;

    //working packet sending and receiving
    public void sendPacket(){

    }

    public void receivePacket(){

    }

    //implement 2+ cong. cont. algorithms

    //generate/send packets at a rate controlled by the congestion control algorithm defined for the flow
    //

    //should implement at least 2 algos, TCP Reno and FAST-TCP, and be able to choose indep.
    //  between them for each flow
    //may send continuous stream of data or may send finite user-specified amount of data
    //may start immediately or after some user-specified delay

    //cwnd congestion window size
    //rwnd receivers advertised window
    //ssthreshold slow start threshold


    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public String getCongCtrlAlgo() {
        return congCtrlAlgo;
    }

    public void setCongCtrlAlgo(String congCtrlAlgo) {
        this.congCtrlAlgo = congCtrlAlgo;
    }
}
