package Events;

import Devices.Flow;
import Domain.EventQueueManager;
import Packets.Packet;

import java.io.IOException;
import java.util.List;

public class TimeOutEvent extends Event{

    private Flow flow;
    private int seqnum;

    TimeOutEvent(double time, EventQueueManager eventQueueManager, Flow flow, int seqnum){
        super(time, eventQueueManager);
        this.flow = flow;
        this.seqnum = seqnum;
    }

    @Override
    public void runEvent() {
        System.out.println();
        if (!flow.getRoundTripTimes().containsKey(seqnum)) {
            return;
        }

        flow.timeoutOccurred();

        double linkFreeAt = flow.getSource().getLink().getLinkFreeAtTime();
        List<Packet> pkts_to_send =
                flow.popOutstandingPackets(getTime(),
                        linkFreeAt == 0 ? getTime() : linkFreeAt);

        for(Packet packet : pkts_to_send){
            packet.setTransmitTimestamp(getTime());
            SendPacketEvent e = new SendPacketEvent(getTime(), eventQueueManager, flow,
                packet, (flow.getSource().getLink()),(flow.getSource()).getName());
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
