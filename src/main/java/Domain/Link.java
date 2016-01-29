package Domain;

import java.util.List;

public class Link {

    private String id;
    private int rate;
    private int delay;
    private List buffer;
    //connect hosts and routers
    //carry packets from one end to the other
    //every link has specified capacity in bits per second
    //link buffers are FIFO
    //packets that try to enter full buffer will be dropped
    //links are half-duplex, meaning data can flow in both directions, but only in one direction at a time
    //has static cost, (its length)
    //has dynamic cost, dependent on link congestion

    public int calculateTotalCost(){
        return 0;
    }



}
