package Events;

import Devices.Flow;
import Domain.EventQueueManager;
import Domain.Main;

import java.util.stream.Collectors;

public class UpdateWindowEvent extends Event{

    private Flow flow;

    public UpdateWindowEvent(double time, EventQueueManager eventQueueManager, Flow flow){
        super(time,eventQueueManager);
        this.flow = flow;
    }

    @Override
    public void runEvent() {

        double w = flow.getWindowSize();

        double new_windowsize;
        if (flow.getAvgRTT() == -1) {
            new_windowsize = Main.ALPHA;
        }
        else {
            new_windowsize = w * (flow.getMinRTT()/(flow.getPktRTT())) + Main.ALPHA;
        }

        flow.setFASTWindowSize(new_windowsize);
    }
}
