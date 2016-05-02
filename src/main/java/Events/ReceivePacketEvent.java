package Events;

import Devices.Flow;
import Devices.Host;
import Devices.Link;
import Devices.Router;
import Domain.EventQueueManager;
import Domain.Main;
import Packets.Packet;
import Packets.PacketType;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReceivePacketEvent extends Event{

    private Flow flow;
    private Packet pkt;
    private String step_destination;
    private Link link;

    ReceivePacketEvent(double time, EventQueueManager eventQueueManager, Flow flow, Packet pkt,
                                     String step_destination, Link link){
        super(time, eventQueueManager);
        constructorHelper(flow, pkt, step_destination, link);
    }

    ReceivePacketEvent(double time, EventQueueManager eventQueueManager, Packet pkt,
                                     String step_destination, Link link){
        super(time, eventQueueManager);
        constructorHelper(null, pkt, step_destination, link);
    }

    void constructorHelper(Flow flow, Packet pkt,
                                                 String step_destination, Link link) {
        this.flow = flow;
        this.pkt = pkt;
        this.step_destination = step_destination;
        this.link = link;
    }

    @Override
    public void runEvent() {

        List<Router> routers = Main.network.getRouters().stream().filter(router1 -> router1.getName().equals(this.step_destination))
                .collect(Collectors.toList());

        double time = getTime();
        link.updateLinkTraffic(time, pkt.getType());

        if (!routers.isEmpty()) {
            Router router = routers.get(0);

            if (pkt.getType() == PacketType.RoutingPacket) {
                router.receiveRoutingPacket(getTime(), pkt, link, eventQueueManager);
            }

            else {
                Map<Link,Packet> link_pkt_map = router.receivePacket(pkt);
                for(Map.Entry<Link,Packet> entry: link_pkt_map.entrySet()){
                    SendPacketEvent e = new SendPacketEvent(
                            getTime(), eventQueueManager, flow, pkt, entry.getKey(),step_destination);
                    eventQueueManager.addEvent(e);
                }
            }
        }

	    else if (pkt.getType() == PacketType.DataPacket) {
            flow.receivedFlowPacket(pkt, getTime());
            flow.updatePktTally(time);
        }

	    else if (pkt.getType() == PacketType.AckPacket) {
            flow.receivedAck(pkt, getTime(), link.getLinkFreeAtTime());

            double linkFreeAt = flow.getSource().getLink().getLinkFreeAtTime();
            List<Packet> pkts_to_send =
                    flow.popOutstandingPackets(getTime(),
                            linkFreeAt == 0 ? getTime() : linkFreeAt);

            int i=0;
            for(Packet packet : pkts_to_send){
                packet.setTransmitTimestamp(getTime());
                SendPacketEvent e = new SendPacketEvent(
                        getTime() + (i++) * Flow.TIME_EPSILON, eventQueueManager,
                        flow, packet, flow.getSource().getLink(), flow.getSource().getName());
                eventQueueManager.addEvent(e);
            }
        }

        link.receivedPacket(pkt.getId());

        double currTime = getTime();
        try {
            eventQueueManager.logEvent(currTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
