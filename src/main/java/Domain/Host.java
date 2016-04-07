package Domain;

import java.util.ArrayList;
import java.util.List;

public class Host {

    private String id;

    private List<Flow> flows = new ArrayList<>();

    private Link link;

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
