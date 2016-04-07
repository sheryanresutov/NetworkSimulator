package Domain;

import java.util.LinkedList;
import java.util.List;

public class Link {

    private String id;
    private String connOne;
    private String connTwo;
    private int rate;
    private int delay;
    private int bufferSize;
    private List buffer = new LinkedList<>();
    //every link has specified capacity in bits per second
    //link buffers are FIFO
    //packets that try to enter full buffer will be dropped
    //links are half-duplex, meaning data can flow in both directions, but only in one direction at a time
    //has static cost, (its length)
    //has dynamic cost, dependent on link congestion


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public List getBuffer() {
        return buffer;
    }

    public void setBuffer(List buffer) {
        this.buffer = buffer;
    }

    public String getConnOne() {
        return connOne;
    }

    public void setConnOne(String connOne) {
        this.connOne = connOne;
    }

    public String getConnTwo() {
        return connTwo;
    }

    public void setConnTwo(String connTwo) {
        this.connTwo = connTwo;
    }
}
