package Events;

import Devices.Link;
import Devices.Router;
import Domain.EventQueueManager;
import Domain.Main;
import Packets.Packet;
import Packets.PacketType;

import java.util.List;
import java.util.stream.Collectors;

public class RouterDiscoveryEvent extends Event{

    private Router router;

    public RouterDiscoveryEvent(double time, EventQueueManager eventQueueManager){
        super(time,eventQueueManager);
        this.router = null;
    }

    @Override
    public void runEvent() {

        for(Router router : Main.network.getRouters()){
            router.resetDistances();
        }

        for(Router router : Main.network.getRouters()){
            List<Link> adj_links = router.getLinks();
            for(Link link : adj_links){
                String other_node = router.getOtherNode(link);
                List<Router> routers = Main.network.getRouters().stream()
                        .filter(router1 -> router1.getName().equals(other_node))
                        .collect(Collectors.toList());
                if(!routers.isEmpty()){
                    Packet rpack = new Packet(PacketType.RoutingPacket, router.getName(),
                            other_node);
                    rpack.setDistances(router.getRDistances());
                    rpack.setTransmitTimestamp(getTime());

                    SendPacketEvent e = new SendPacketEvent(getTime(), eventQueueManager,
                            rpack, link, router.getName());
                    eventQueueManager.addEvent(e);
                }
            }
        }
    }
}
