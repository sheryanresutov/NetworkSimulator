package Domain;

import java.util.ArrayList;
import java.util.List;

public class Host {

    //has a network address, which uniquely identifies each node on the network
    private String id;

    private List<Flow> flows = new ArrayList<>();

    //will have at most one link connected
    private Link link;


    //can process an infinite amount of incoming data instantaneously, but outgoing data
    //must sit on a link buffer until the link is free


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Flow> getFlows() {
        return flows;
    }

    public void setFlows(List<Flow> flows) {
        this.flows = flows;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public void addFlow(Flow flow) {
        flows.add(flow);
    }
}
