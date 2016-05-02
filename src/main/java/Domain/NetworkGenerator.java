package Domain;

import Devices.Flow;
import Devices.Link;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class NetworkGenerator {

    public static Network parseNetworkSpecs(String networkFile, EventQueueManager eventQueueManager) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Network network = mapper.readValue(new File(networkFile), Network.class);
            connectLinksAndFlows(network,eventQueueManager);
            return network;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void connectLinksAndFlows(Network network, EventQueueManager eventQueueManager){
        for(Link link:network.getLinks()){
            network.getHosts().stream()
                    .filter(host -> host.getName().equals(link.getEndpoint1()) || host.getName().equals(link.getEndpoint2()))
                    .forEach(host -> host.setLink(link));
            if(network.getRouters() == null) {
                network.setRouters(new ArrayList<>());
            }
            network.getRouters().stream()
                    .filter(router -> router.getName().equals(link.getEndpoint1()) || router.getName().equals(link.getEndpoint2()))
                    .forEach(router -> router.addLink(link));

        }
        for(Flow flow:network.getFlows()){
            network.getHosts().stream()
                    .filter(host -> host.getName().equals(flow.getSrc()) || host.getName().equals(flow.getDest()))
                    .forEach(host -> host.addFlow(flow));
            flow.setDestination(network.getHosts().stream()
                    .filter(host -> host.getName().equals(flow.getDest())).collect(Collectors.toList()).get(0));
            flow.setSource(network.getHosts().stream()
                    .filter(host -> host.getName().equals(flow.getSrc())).collect(Collectors.toList()).get(0));
            flow.eventQueueManager = eventQueueManager;
        }
    }
}
