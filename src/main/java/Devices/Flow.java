package Devices;

import Domain.EventQueueManager;
import Domain.Main;
import Events.AckEvent;
import Events.TimeOutEvent;
import Packets.Packet;
import Packets.PacketType;

import java.util.*;

public class Flow {

    private String name;
    private double start_time_sec;
    private int size_mb;
    private double amt_received_mb = 0;
    private Host source;
    private Host destination;
    private String src,dest;
    private int pktTally = 0;
    private double leftTime = start_time_sec;
    private double rightTime = start_time_sec + Main.RATE_INTERVAL;
    private int highest_received_ack_seqnum = 1;
    private int highest_sent_flow_seqnum = 0;
    private List<Boolean> received = new ArrayList<>(Collections.nCopies(50000,false));
    private int next_ack_seqnum = 1;
    private double window_size = 1;
    private int window_start = 1;
    private int num_duplicate_acks = 0;
    private double timeout_length_ms = DEFAULT_INITIAL_TIMEOUT;
    private double lin_growth_winsize_threshold = -1;
    private boolean FAST_TCP = false;
    private double avg_RTT = -1;
    private double std_RTT = -1;
    private double min_RTT = Double.MAX_VALUE;
    private double pkt_RTT = -1;
    private TimeOutEvent flow_timeout;
    private Map<Integer, Double> rtts = new HashMap<>();
    private double dont_send_duplicate_ack_until = -1;
    private int waiting_for_seqnum_before_resuming = -1;
    public EventQueueManager eventQueueManager;

    public static int FAST_RETRANSMIT_DUPLICATE_ACK_THRESHOLD = 3;
    public static double DEFAULT_INITIAL_TIMEOUT = 1000.0;
    public static double B_TIMEOUT_CALC = 0.1;
    public static double TIME_EPSILON = 0.0000000001;

    private void updateTimeoutLength(double rtt, int flow_seqnum){
        pkt_RTT = rtt;

        if (avg_RTT < 0 && std_RTT < 0) {
            avg_RTT = rtt;
            std_RTT = rtt;
        }
        else {
            avg_RTT = (1 - B_TIMEOUT_CALC) * avg_RTT + B_TIMEOUT_CALC * rtt;
            std_RTT = (1 - B_TIMEOUT_CALC) * std_RTT +
                    B_TIMEOUT_CALC * Math.abs(rtt - avg_RTT);
        }

        if (rtt < min_RTT) {
            min_RTT = rtt;
        }

        timeout_length_ms = avg_RTT + 4 * std_RTT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStart_time_sec() {
        return start_time_sec;
    }

    public void setStart_time_sec(double start_time_sec) {
        this.start_time_sec = start_time_sec;
    }

    public int getSize_mb() {
        return size_mb;
    }

    public void setSize_mb(int size_mb) {
        this.size_mb = size_mb;
    }

    public void setSource(Host source) {
        this.source = source;
    }

    public void setDestination(Host destination) {
        this.destination = destination;
    }

    public double getStartTimeSec(){
        return start_time_sec;
    }

    public double getStartTimeMs(){
        return start_time_sec * 1000;
    }

    public double getSizeMb(){
        return size_mb;
    }

    public Host getDestination(){
        return destination;
    }

    public Host getSource(){
        return source;
    }

    public int getPktTally(){
        return pktTally;
    }

    public double getLeftTime(){
        return leftTime;
    }

    public double getRightTime(){
        return rightTime;
    }

    public int getNumTotalPackets(){
        int size_in_bytes = size_mb * Main.BYTES_PER_MEGABIT;
        if (size_in_bytes % Main.FLOW_PACKET_SIZE == 0)
            return size_in_bytes / Main.FLOW_PACKET_SIZE;
        return size_in_bytes / Main.FLOW_PACKET_SIZE + 1;
    }

    public double getTimeoutLengthMs(){
        return timeout_length_ms;
    }

    public int getHighestAckSeqnum(){
        return highest_received_ack_seqnum;
    }

    public int getNumDuplicateAcks(){
        return num_duplicate_acks;
    }

    public double getAvgRTT(){
        return avg_RTT;
    }

    public double getMinRTT(){
        return min_RTT;
    }

    public boolean isUsingFAST(){
        return FAST_TCP;
    }

    public Map getRoundTripTimes(){
        return rtts;
    }

    public double getWindowSize(){
        return window_size;
    }

    public int getWindowStart(){
        return window_start;
    }

    public double getLinGrowthWinsizeThreshold(){
        return lin_growth_winsize_threshold;
    }

    public double getPktRTT(){
        return pkt_RTT;
    }

    public double getFlowRateBytesPerSec(){
        return getPktTally() * Main.FLOW_PACKET_SIZE / Main.RATE_INTERVAL;
    }

    public double getFlowRateMbps(double time){
        double bytes = (double) getPktTally() * Main.FLOW_PACKET_SIZE / Main.RATE_INTERVAL;
        return bytes * Main.BYTES_PER_MEGABIT;
    }

    public double getFlowPercentage(){
        return amt_received_mb / size_mb * 100;
    }

    public double getPktDelay(double currTime){
        return pkt_RTT;
    }

    public boolean doneTransmitting(){
        if (amt_received_mb + ((double)Main.FLOW_PACKET_SIZE) / Main.BYTES_PER_MEGABIT >= size_mb) {
            return true;
        }
        return false;
    }

    public void updatePktTally(double time){
        if ((time <= rightTime) && (time > leftTime)) {
            pktTally++;
        }
        else if (time > rightTime) {
            pktTally = 0;
            leftTime = rightTime;
            rightTime = rightTime + Main.RATE_INTERVAL;
            updatePktTally(time);
        }
    }

    public void setFASTWindowSize(double new_size){
        window_size = new_size;
    }

    public void setLeftTime(double newTime){
        leftTime = newTime;
    }

    public void setRightTime(double newTime){
        rightTime = newTime;
    }

    public 	void registerAckEvent(int seq, double arrival_time, double sent_time){
        Packet p = new Packet(PacketType.AckPacket,this,seq);

        p.setTransmitTimestamp(sent_time);
        if (seq == (next_ack_seqnum - 1)) {

            if (arrival_time > dont_send_duplicate_ack_until) {
                dont_send_duplicate_ack_until = arrival_time + avg_RTT;
                AckEvent e = new AckEvent(arrival_time, eventQueueManager, this, p);
                eventQueueManager.addEvent(e);
            }
        }
        else {
            dont_send_duplicate_ack_until = -1;
            AckEvent e = new AckEvent(arrival_time, eventQueueManager, this, p);
            eventQueueManager.addEvent(e);
        }
    }

    public List<Packet> peekOutstandingPackets(){
        List<Packet> outstanding_pkts = new ArrayList<>();

        double window_end;
        if (waiting_for_seqnum_before_resuming == -1) {
            window_end = window_start + window_size;
        }
        else {
            window_end = highest_sent_flow_seqnum + 2;
        }

        for (int i = highest_sent_flow_seqnum + 1;
             i < window_end; i++) {

            if (i > getNumTotalPackets()) {
                break;
            }

            outstanding_pkts.add(new Packet(PacketType.DataPacket, this, i));
        }

        return outstanding_pkts;
    }

    public List<Packet> popOutstandingPackets(double start_time, double linkFreeAt){
        if (amt_received_mb >= size_mb) {
            return new ArrayList<Packet>();
        }

        List<Packet> outstanding_pkts = peekOutstandingPackets();

        for(Packet packet : outstanding_pkts){
            rtts.put(packet.getSeq(),-start_time);
        }

        highest_sent_flow_seqnum += outstanding_pkts.size();

        return outstanding_pkts;
    }

    public void receivedAck(Packet pkt, double end_time_ms, double linkFreeAtTime){
        if (waiting_for_seqnum_before_resuming == -1) {

        }
        else if (waiting_for_seqnum_before_resuming <= pkt.getSeq()) {
            waiting_for_seqnum_before_resuming = -1;
        }
        else {
            return;
        }

        if (pkt.getSeq() == highest_received_ack_seqnum) {

            if (++num_duplicate_acks >=
                    FAST_RETRANSMIT_DUPLICATE_ACK_THRESHOLD) {

                highest_sent_flow_seqnum = pkt.getSeq()-1;
                window_start = pkt.getSeq();
                if (!FAST_TCP) {
                    lin_growth_winsize_threshold = window_size / 2;
                    window_size = 1;
                }
                num_duplicate_acks = 0;

                waiting_for_seqnum_before_resuming = pkt.getSeq() + 1;

            }
        }

        else if (pkt.getSeq() >= (highest_received_ack_seqnum + 1)) {

            int diff = pkt.getSeq() - highest_received_ack_seqnum;

            highest_received_ack_seqnum = pkt.getSeq();

            int flow_seqnum = pkt.getSeq() - 1;

            if (pkt.getTransmitTimestamp() != -1) {
                updateTimeoutLength(end_time_ms - pkt.getTransmitTimestamp(),
                        flow_seqnum);
            }

            window_start++;

            for (int i = 0; i < diff; i++) {
                if (!FAST_TCP) {
                    if (lin_growth_winsize_threshold < 0) {
                        window_size++;
                    }
                    else if (window_size < lin_growth_winsize_threshold) {
                        window_size++;
                    }
                    else {
                        window_size += (1 / window_size);
                    }
                }
            }

        }
    }

    public void receivedFlowPacket(Packet pkt, double arrival_time){
        if (!received.get(pkt.getSeq())) {
            amt_received_mb += ((double) Main.FLOW_PACKET_SIZE) / Main.BYTES_PER_MEGABIT;
        }
        received.set(pkt.getSeq(),true);

        while ((received.get(next_ack_seqnum))
                && (next_ack_seqnum <= getNumTotalPackets())) {
            next_ack_seqnum++;
        }
        double sent_time = (next_ack_seqnum == pkt.getSeq() + 1) ?
                pkt.getTransmitTimestamp() : -1;
        registerAckEvent(next_ack_seqnum, arrival_time, sent_time);
    }

    public void timeoutOccurred(){
        lin_growth_winsize_threshold = window_size / 2;
        window_size = 1;
        window_start = highest_received_ack_seqnum;
        num_duplicate_acks = 0;
        highest_sent_flow_seqnum = window_start - 1;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
