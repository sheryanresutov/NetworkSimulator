package Events;

import Devices.Flow;
import Domain.EventQueueManager;
import Packets.Packet;

import java.io.IOException;

public class AckEvent extends Event{

    private Flow flow;
    private Packet dup_pkt;

    public AckEvent(double time, EventQueueManager eventQueueManager, Flow flow, Packet dup_pkt) {
        super(time, eventQueueManager);
        this.flow = flow;
        this.dup_pkt = dup_pkt;
    }

    @Override
    public void runEvent() {
        SendPacketEvent e = new SendPacketEvent(getTime(), eventQueueManager, flow  ,
                dup_pkt, (flow.getDestination().getLink()),(flow.getDestination()).getName());
        eventQueueManager.addEvent(e);

        double currTime = getTime();
        try {
            eventQueueManager.logEvent(currTime);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
