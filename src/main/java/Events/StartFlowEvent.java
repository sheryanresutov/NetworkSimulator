package Events;

import Devices.Flow;
import Domain.EventQueueManager;
import Packets.Packet;

import java.io.IOException;
import java.util.List;

public class StartFlowEvent extends Event {

    private Flow flow;

    public StartFlowEvent(double time, EventQueueManager eventQueueManager, Flow flow){
        super(time, eventQueueManager);
        this.flow = flow;

    }

    @Override
    public void runEvent() {
        double linkFreeAt = flow.getSource().getLink().getLinkFreeAtTime();
        List<Packet> pkts_to_send =
                flow.popOutstandingPackets(getTime(),
                        linkFreeAt == 0 ? flow.getStartTimeMs() : linkFreeAt);

        for(Packet packet : pkts_to_send){
            packet.setTransmitTimestamp(getTime());
            SendPacketEvent e = new SendPacketEvent(getTime(), eventQueueManager, flow,
                packet, (flow.getSource().getLink()), (flow.getSource()).getName());
            eventQueueManager.addEvent(e);
        }

        double currTime = getTime();
        try {
            eventQueueManager.logEvent(currTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
