package Devices;

import Domain.Main;
import Packets.Packet;
import Packets.PacketType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Link {

    private String name;
    private String endpoint1;
    private String endpoint2;
    private int rate_bpms;
    private int delay_ms;
    private int buffer_capacity;
    private Map<Double, Packet> buffer = new HashMap<>();
    private int packets_dropped = 0;
    private Map<String, Integer> linkTraffic = new HashMap<String, Integer>(){{
        put("ack",0);
        put("flow",0);
        put("rtr",0);
    }};
    private double leftTime = 0;
    private double rightTime = 0;
    private String destination_last_packet = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate_bpms() {
        return rate_bpms;
    }

    public void setRate_bpms(int rate_bpms) {
        this.rate_bpms = rate_bpms* Main.BYTES_PER_MEGABIT / Main.MS_PER_SEC;
    }

    public int getDelay_ms() {
        return delay_ms;
    }

    public void setDelay_ms(int delay_ms) {
        this.delay_ms = delay_ms;
    }

    public int getBuffer_capacity() {
        return buffer_capacity;
    }

    public void setBuffer_capacity(int buffer_capacity) {
        this.buffer_capacity = buffer_capacity * Main.BYTES_PER_KB;
    }

    public Map getBuffer() {
        return buffer;
    }

    public void setBuffer(Map buffer) {
        this.buffer = buffer;
    }

    public void setConnOne(String connOne) {
        this.endpoint1 = connOne;
    }

    public double getBuflen(){
        return buffer_capacity;
    }

    public double getBuflenKB(){
        return buffer_capacity / Main.BYTES_PER_KB;
    }

    public int getDelay(){
        return delay_ms;
    }

    public String getEndpoint1(){
        return endpoint1;
    }

    public String getEndpoint2(){
        return endpoint2;
    }

    public double getCapacityBitsPerSec(){
        return rate_bpms * Main.MS_PER_SEC;
    }

    public double getCapacityMbps(){
        return ((double) rate_bpms) / Main.BYTES_PER_MEGABIT * Main.MS_PER_SEC;
    }

    public double getRateMbps(){
        int flowsize =  Main.FLOW_PACKET_SIZE * linkTraffic.get("flow");
        int acksize = Main.ACK_PACKET_SIZE * linkTraffic.get("ack");
        int rtrsize = Main.ROUTING_PACKET_SIZE * linkTraffic.get("rtr");
        return ((double) (flowsize + acksize + rtrsize)) * 8 / 1000000;
    }

    public double getTransmissionTimeMs(Packet pkt){
        return pkt.getSizeBytes() / rate_bpms;
    }

    public double getLinkFreeAtTime(){
        if (buffer.size() == 0) {
            return 0;
        }
        return getLargestKey(buffer);
    }

    public double getBufferOccupancy(){
        long buffer_occupancy = 0;
        for(Map.Entry<Double,Packet> entry: buffer.entrySet()){
            buffer_occupancy += entry.getValue().getSizeBytes();
        }
        return buffer_occupancy;
    }

    public int getPktLoss(){
        return packets_dropped;
    }

    public Map getLinkTraffic(){
        return linkTraffic;
    }

    public boolean isSameDirectionAsLastPacket(String destination){
        if(destination_last_packet == null) { // need to use link delay for first
            return false;
        }
        if(destination.equals(destination_last_packet)){
            return true;
        }
        return false;
    }

    public double getArrivalTime(Packet pkt, boolean useDelay, double time){
        return (useDelay ? getDelay() : 0) + getTransmissionTimeMs(pkt) +
                (buffer.size() > 0 ? getLargestKey(buffer) : time);
    }

    private double getLargestKey(Map<Double,Packet> buffer){
        double maxVal = Double.MIN_VALUE;
        for(Map.Entry<Double,Packet> entry : buffer.entrySet()){
            if(entry.getKey()>maxVal){
                maxVal = entry.getKey();
            }
        }
        return maxVal;
    }

    private Map.Entry getSmallestKey(Map<Double,Packet> buffer){
        Map.Entry entryVal = null;
        double minVal = Double.MAX_VALUE;
        for(Map.Entry<Double,Packet> entry : buffer.entrySet()){
            if(entry.getKey()<minVal){
                minVal = entry.getKey();
                entryVal = entry;
            }
        }
        return entryVal;
    }

    public void setEndpoint1(String val){
        endpoint1 = val;
    }

    public void setEndpoint2(String val){
        endpoint2 = val;
    }

    public boolean sendPacket(Packet pkt, String destination, boolean useDelay, double time){
        destination_last_packet = destination;

        if (getBufferOccupancy() + pkt.getSizeBytes() > buffer_capacity) {
            packets_dropped++;
            return false;
        }
        buffer.put(getArrivalTime(pkt,useDelay,time), pkt);
        packets_dropped = 0;
        return true;
    }

    public boolean receivedPacket(long pkt_id){
        Map.Entry<Double,Packet> entry = getSmallestKey(buffer);
        if(buffer.size() == 0 || (entry.getValue().getId() != pkt_id)) {
            return false;
        }
        buffer.remove(entry.getKey());
        return true;
    }

    public void resetLinkTraffic(){
        linkTraffic.put("ack", 0);
        linkTraffic.put("flow", 0);
        linkTraffic.put("rtr", 0);
    }

    public void updateLinkTraffic(double time, PacketType type){
        if ((time <= rightTime) && (time > leftTime)) {
            if (type == PacketType.DataPacket){
                linkTraffic.put("flow",linkTraffic.get("flow")+1);
            }
            else if (type == PacketType.AckPacket){
                linkTraffic.put("ack",linkTraffic.get("ack")+1);
            }
            else if (type == PacketType.RoutingPacket){
                linkTraffic.put("rtr",linkTraffic.get("rtr")+1);
            }
        }
        else if (time > rightTime) {
            resetLinkTraffic();
            leftTime = rightTime;
            rightTime = rightTime + Main.RATE_INTERVAL;
            updateLinkTraffic(time, type);
        }
    }
}
