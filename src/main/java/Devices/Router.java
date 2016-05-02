package Devices;

import Domain.EventQueueManager;
import Domain.Main;
import Events.SendPacketEvent;
import Packets.Packet;
import Packets.PacketType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Router {

    private String name;
    private List<Link> links = new ArrayList<>();
    private Map<String, Link> rTable = new HashMap<>();
    private Map<String, Double> rDistances = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getLinks() {
        return links;
    }

    public void setLinks(List links) {
        this.links = links;
    }

    public Map getrTable() {
        return rTable;
    }

    public void setrTable(Map rTable) {
        this.rTable = rTable;
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public String getOtherNode(Link link){
        if(name.equals(link.getEndpoint1())) {
            return link.getEndpoint2();
        }
        return link.getEndpoint1();
    }

    public Map receivePacket(Packet packet){
        Map<Link,Packet> linkPacketMap = new HashMap<>();
        Link link = rTable.get(packet.getDestination());
        linkPacketMap.put(link,packet);
        return linkPacketMap;
    }

    public void receiveRoutingPacket(double time, Packet rPacket, Link link, EventQueueManager eventQueueManager){
        Map<String,Double> received_dist = rPacket.getDistances();
        boolean updated = false;
        double travel_time = time - rPacket.getTransmitTimestamp();

        for(Map.Entry<String,Double> entry: received_dist.entrySet()){
            String key = entry.getKey();
            if(entry.getValue() + travel_time < rDistances.get(key)){
                updated = true;
                rDistances.put(key,entry.getValue() + travel_time);
                rTable.put(key,link);
            }
        }

        if(updated){
            List<Link> adj_links = getLinks();
            for (Link adj_link : adj_links) {

                String other_node = getOtherNode(adj_link);

                List<Router> routers = Main.network.getRouters().stream()
                        .filter(router1 -> router1.getName().equals(other_node))
                        .collect(Collectors.toList());
                if (!routers.isEmpty()) {

                    Packet rpack = new Packet(PacketType.RoutingPacket, getName(), other_node);
                    rpack.setDistances(rDistances);
                    rpack.setTransmitTimestamp(time);
                    SendPacketEvent e = new SendPacketEvent(time, eventQueueManager,
                            rpack, adj_link, getName());
                    eventQueueManager.addEvent(e);

                }

            }
        }

    }

    public void resetDistances(){
        for(Router router : Main.network.getRouters()){
            if(!router.getName().equals(getName())){
                rDistances.put(router.getName(),Double.MAX_VALUE);
            }
        }

        for(Host host : Main.network.getHosts()){
            if(!host.getOtherNode().equals(getName())){
                rDistances.put(host.getName(),Double.MAX_VALUE);
            }
        }
    }

    public Map<String, Double> getRDistances() {
        return rDistances;
    }

    public void setDistances(Map<String, Double> distances) {
        this.rDistances = distances;
    }

    public void initializeTables(){
        for(Router router : Main.network.getRouters()){
            rTable.put(router.getName(),null);

            if(router.getName().equals(getName())){
                rDistances.put(router.getName(),0.0);
            }
            else{
                rDistances.put(router.getName(),Double.MAX_VALUE);
            }
        }

        for(Host host : Main.network.getHosts()){

            if(host.getOtherNode().equals(getName())){
                rTable.put(host.getName(),host.getLink());
                rDistances.put(host.getName(),0.0);
            }
            else{
                rTable.put(host.getName(),null);
                rDistances.put(host.getName(),Double.MAX_VALUE);
            }
        }
    }
}
