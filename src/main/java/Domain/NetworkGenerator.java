package Domain;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class NetworkGenerator {

    public static Network parseNetworkSpecs(String networkFile) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Network network = mapper.readValue(new File(networkFile), Network.class);
            connectLinksAndFlows(network);
            return network;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void connectLinksAndFlows(Network network){
        for(Link link:network.getLinks()){
            network.getHosts().stream()
                    .filter(host -> host.getId().equals(link.getSrc()) || host.getId().equals(link.getDest()))
                    .forEach(host -> host.setLink(link));
            network.getRouters().stream()
                    .filter(router -> router.getId().equals(link.getSrc()) || router.getId().equals(link.getDest()))
                    .forEach(router -> router.addLink(link));
        }
        for(Flow flow:network.getFlows()){
            network.getHosts().stream()
                    .filter(host -> host.getId().equals(flow.getSrc()) || host.getId().equals(flow.getDest()))
                    .forEach(host -> host.addFlow(flow));
        }
    }
}
