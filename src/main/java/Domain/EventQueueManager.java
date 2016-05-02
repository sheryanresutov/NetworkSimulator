package Domain;

import Devices.Flow;
import Devices.Link;
import Devices.Router;
import Events.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class EventQueueManager {

    private ListMultimap<Double, Event> events = ArrayListMultimap.create();
    List<Double> times = new ArrayList<>();
    List<LinkData> linkDataList = new ArrayList<>();
    List<FlowData> flowDataList = new ArrayList<>();
    private int eventCount = 0;
    private PrintWriter printWriter;

    public void addEvent(Event e) {
        events.put(e.getTime(), e);
    }

    public void removeEvent(Event e){
        events.remove(e.getTime(), e);
    }


    public boolean allFlowsDone() {
        boolean isDone = true;

        for(Flow flow : Main.network.getFlows()){
            isDone = isDone && flow.doneTransmitting();
        }

        return isDone;
    }

    public void logEvent(double currTime) throws IOException {
        String linkStr = "",flowStr="";
        eventCount++;
        if((eventCount % 50) != 0 ){
            return;
        }
        else {
            times.add(currTime);
            for (Link link : Main.network.getLinks()) {
//                LinkData linkData = new LinkData(link.getName(), link.getRateMbps(), link.getBufferOccupancy() / 1000, link.getPktLoss());
//                linkDataList.add(linkData);
//                System.out.println(link.getRateMbps());
                linkStr = linkStr+","+link.getName()+","+link.getRateMbps()+","+(link.getBufferOccupancy()/1000)+","+link.getPktLoss();
            }

            for (Flow flow : Main.network.getFlows()) {
//                FlowData flowData = new FlowData(flow.getName(), flow.getFlowRateMbps(currTime), flow.getWindowSize(), flow.getPktDelay(currTime));
//                flowDataList.add(flowData);
//                System.out.println(flow.getPktDelay(currTime));
                flowStr = flowStr+","+flow.getName()+","+flow.getFlowRateMbps(currTime)+","+flow.getWindowSize()+","+flow.getPktDelay(currTime);
            }
            printWriter.println(currTime+linkStr+flowStr);
            //write currTime,l1 stats,l2 stats,...f1 stats,f2 stats...

        }
    }

    public void runSimulation() throws IOException {
        printWriter = new PrintWriter(
                new FileWriter("/home/sheryan/networksimulator/src/main/java/Domain/TestCase"+Main.TEST_CASE+"Results.csv"));
        Main.network.getRouters().forEach(Devices.Router::initializeTables);

        RouterDiscoveryEvent r_event = new RouterDiscoveryEvent(0, this);
        addEvent(r_event);

        for (int update_t = 550; update_t < Main.UPPER_TIME_ROUTING_LIMIT;
             update_t += 5000) {
            RouterDiscoveryEvent r_event1 = new
                    RouterDiscoveryEvent(update_t, this);
            addEvent(r_event1);
        }

        for(Flow flow : Main.network.getFlows()){
            StartFlowEvent fevent = new
                    StartFlowEvent(flow.getStartTimeMs(), this, flow);
            addEvent(fevent);

            if (flow.isUsingFAST()) {

                for (double update_w = flow.getStartTimeMs();
                     update_w < Main.UPPER_TIME_ROUTING_LIMIT;
                     update_w += 20) {

                    UpdateWindowEvent w_event = new
                            UpdateWindowEvent(update_w, this, flow);
                    addEvent(w_event);
                }
            }
        }

        while (!events.isEmpty() && !(allFlowsDone())) {
            Event earliestEvent = getEarliestEvent(events);
            events.remove(earliestEvent.getTime(), earliestEvent);
//            System.out.println(earliestEvent.getTime());
            earliestEvent.runEvent();
        }
        printWriter.close();
    }

    private Event getEarliestEvent(ListMultimap<Double, Event> events) {
        double minTime = Double.MAX_VALUE;
        Event earliestEvent = null;
        for(Map.Entry<Double,Event> entry : events.entries()){
            if (entry.getKey()<minTime){
                minTime = entry.getKey();
                earliestEvent = entry.getValue();
            }
        }
        return earliestEvent;
    }
}
