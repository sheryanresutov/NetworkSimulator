package Domain;

import Devices.Flow;
import Devices.Link;
import Devices.Router;
import Events.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class EventQueueManager {

    private ListMultimap<Double, Event> events = ArrayListMultimap.create();
    private int eventCount = 0;
    private ObjectMapper objectMapper;

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
        eventCount++;
        if((eventCount % Main.EVENT_LOG_FREQ) != 0 ){
            return;
        }
        else {
            try{
                Thread.sleep(Main.WAIT_TIME);
            }catch(InterruptedException ie){
                Thread.currentThread().interrupt();
            }
            DataOne dataOne = new DataOne();
            DataTwo dataTwo = new DataTwo();
            DataThree dataThree = new DataThree();

            for (Link link : Main.network.getLinks()) {
                LinkData linkData = new LinkData(link.getRateMbps(), link.getBufferOccupancy()/1000, link.getPktLoss());
                switch (link.getName()){
                    case "L0":
                        dataTwo.links.setL0(linkData);
                    case "L1":
                        dataOne.links.setL1(linkData);
                        dataTwo.links.setL1(linkData);
                        dataThree.links.setL1(linkData);
                        break;
                    case "L2":
                        dataTwo.links.setL2(linkData);
                        dataThree.links.setL2(linkData);
                        break;
                    case "L3":
                        dataTwo.links.setL3(linkData);
                        dataThree.links.setL3(linkData);
                        break;
                    case "L4":
                        dataTwo.links.setL4(linkData);
                        dataThree.links.setL4(linkData);
                        break;
                    case "L5":
                        dataTwo.links.setL5(linkData);
                        dataThree.links.setL5(linkData);
                        break;
                    case "L6":
                        dataThree.links.setL6(linkData);
                        break;
                    case "L7":
                        dataThree.links.setL7(linkData);
                        break;
                    case "L8":
                        dataThree.links.setL8(linkData);
                        break;
                    case "L9":
                        dataThree.links.setL9(linkData);
                        break;
                }
            }

            for (Flow flow : Main.network.getFlows()) {
                FlowData flowData = new FlowData(flow.getFlowRateMbps(currTime), flow.getWindowSize(), flow.getPktDelay(currTime));
                switch (flow.getName()){
                    case "F1":
                        dataOne.flows.setF1(flowData);
                        dataTwo.flows.setF1(flowData);
                        dataThree.flows.setF1(flowData);
                        break;
                    case "F2":
                        dataThree.flows.setF2(flowData);
                        break;
                    case "F3":
                        dataThree.flows.setF3(flowData);
                        break;
                }
            }
            if(Main.TEST_CASE == 0){
                objectMapper.writeValue(new File(Main.PATH+"/networksimulator/livePlotting/data.json"),dataOne);
            }
            else if (Main.TEST_CASE == 1){
                objectMapper.writeValue(new File(Main.PATH+"/networksimulator/livePlotting/data.json"),dataTwo);
            }
            else{
                objectMapper.writeValue(new File(Main.PATH+"/networksimulator/livePlotting/data.json"),dataThree);
            }
        }
    }

    public void runSimulation() throws IOException {
        double startTime = System.currentTimeMillis();
        objectMapper = new ObjectMapper();
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
            earliestEvent.runEvent();
        }
        double endTime = System.currentTimeMillis();
        System.out.println((endTime-startTime)/1000);
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
