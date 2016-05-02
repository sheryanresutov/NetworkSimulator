package Events;

import Devices.Flow;
import Devices.Link;
import Devices.Router;
import Domain.EventQueueManager;
import Domain.Main;
import Domain.Network;
import Packets.Packet;
import Packets.PacketType;

import java.io.IOException;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

public class SendPacketEvent extends Event {

    private Flow flow;
    private Packet pkt;
    private Link link;
    private String departure_node;

    public SendPacketEvent(double time, EventQueueManager eventQueueManager, Packet rpack, Link link, String name) {
        super(time,eventQueueManager);
        this.pkt = rpack;
        this.link = link;
        this.departure_node = name;
        this.flow = null;

    }

    public SendPacketEvent(double time, EventQueueManager eventQueueManager, Flow flow, Packet rpack, Link link,
                           String name) {
        super(time, eventQueueManager);
        this.pkt = rpack;
        this.link = link;
        this.departure_node = name;
        this.flow = flow;
    }

    private String getDestinationNode(){
        if (departure_node.equals(link.getEndpoint1())) {
            return link.getEndpoint2();
        }
        else if (departure_node.equals(link.getEndpoint2())) {
            return link.getEndpoint1();
        }
        return null;
    }

    @Override
    public void runEvent() {
        boolean use_delay = !link.isSameDirectionAsLastPacket(getDestinationNode())
                || link.getBufferOccupancy() == 0;
        double arrival_time = link.getArrivalTime(pkt, use_delay, getTime());

        if (link.sendPacket(pkt, getDestinationNode(), use_delay, getTime())) {
            ReceivePacketEvent e = new ReceivePacketEvent(arrival_time, eventQueueManager,
                flow, pkt, getDestinationNode(), link);
            eventQueueManager.addEvent(e);
            List<Router> routers = Main.network.getRouters().stream().filter(router -> router.getName().equals(departure_node)).collect(Collectors.toList());
            //idk if this should be commented
//            if(!routers.isEmpty() && pkt.getType() == PacketType.DataPacket){
//                TimeOutEvent timeOutEvent = new TimeOutEvent(getTime()+flow.getTimeoutLengthMs(),eventQueueManager,flow,pkt.getSeq());
//                eventQueueManager.addEvent(timeOutEvent);
//            }
        }


        double currTime = getTime();
        try {
            eventQueueManager.logEvent(currTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
