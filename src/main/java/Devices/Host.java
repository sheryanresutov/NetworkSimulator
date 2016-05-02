package Devices;

import java.util.ArrayList;
import java.util.List;

public class Host {

    private String name;
    private List<Flow> flows = new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Flow> getFlows() {
        return flows;
    }

    public void setFlows(List<Flow> flows) {
        this.flows = flows;
    }

    public Link getLink() {
        return links.get(0);
    }

    public void setLink(Link link) {
        this.links.add(link);
    }

    public List getLinks(){
        return links;
    }

    public void addFlow(Flow flow) {
        flows.add(flow);
    }

    public String getOtherNode(){
        if(name.equals(links.get(0).getEndpoint1())) {
            return links.get(0).getEndpoint2();
        }
        return links.get(0).getEndpoint1();
    }
}
