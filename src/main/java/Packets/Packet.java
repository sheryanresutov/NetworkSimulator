package Packets;

import Devices.Flow;
import Domain.Main;

import java.util.Map;
import java.util.stream.Collectors;

public class Packet {

    private long pkt_id;
    PacketType type;
    public long id_gen = 1;
    private String source_ip, dest_ip;
    private Flow parent_flow;
    private double size;
    private int seqnum;
    private Map<String, Double> distance_vec;
    private double transmit_timestamp;



    public Packet(PacketType type, String source_ip, String dest_ip) {
        constructorHelper(type, source_ip, dest_ip, Main.SEQNUM_FOR_NONFLOWS,
                null, ((double)Main.ROUTING_PACKET_SIZE) / Main.BYTES_PER_MEGABIT);
    }

    public Packet(PacketType type, Flow parent_flow, int seqnum) {
        switch (type){
            case DataPacket:
                constructorHelper(type, parent_flow.getSource().getName(), parent_flow.getDestination().getName(), seqnum, parent_flow,
                    ((double)Main.FLOW_PACKET_SIZE) / Main.BYTES_PER_MEGABIT);
                break;
            case AckPacket:
                constructorHelper(type, parent_flow.getDestination().getName(), parent_flow.getSource().getName(), seqnum, parent_flow,
                    ((double)Main.ACK_PACKET_SIZE) / Main.BYTES_PER_MEGABIT);
                break;
        }
    }

    private void constructorHelper(PacketType type, String source_ip,
                                   String dest_ip, int seqnum,
                                   Flow parent_flow, double size) {
        this.type = type;
        this.source_ip = source_ip;
        this.dest_ip = dest_ip;
        this.seqnum = seqnum;
        this.parent_flow = parent_flow;
        this.size = size;
        this.pkt_id = id_gen++;
    }

    public String getSource_ip() {
        return source_ip;
    }

    public void setSource_ip(String source_ip) {
        this.source_ip = source_ip;
    }

    public String getDest_ip() {
        return dest_ip;
    }

    public void setDest_ip(String dest_ip) {
        this.dest_ip = dest_ip;
    }

    public Flow getFlow() {
        return parent_flow;
    }

    public void setFlow(Flow flow) {
        this.parent_flow = flow;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public boolean isNullPacket(){
        return pkt_id == 0;
    }

    public String getSource(){
        return source_ip;
    }

    public String getDestination(){
        return dest_ip;
    }

    public int getSeq(){
        return seqnum;
    }

    public long getId(){
        return pkt_id;
    }

    public Map getDistances(){
        return distance_vec;
    }

    public void setDistances(Map map){
        distance_vec = map;
    }

    public Flow getParentFlow(){
        return parent_flow;
    }

    public PacketType getType(){
        return type;
    }

    public double getSizeMb(){
        return size;
    }

    public double getSizeBytes(){
        return size * Main.BYTES_PER_MEGABIT;
    }

    public String getTypeString(){
        switch(type) {
            case AckPacket:
                return "ACK";
            case DataPacket:
                return"FLOW";
            case RoutingPacket:
                return "ROUTING";
            default:
                return "ERROR_TYPE";
        }
    }

    public double getTransmitTimestamp(){
        return transmit_timestamp;
    }

    public void setTransmitTimestamp(double time){
        transmit_timestamp = time;
        }
}
