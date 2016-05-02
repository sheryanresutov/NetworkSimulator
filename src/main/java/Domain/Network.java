package Domain;

import Devices.Flow;
import Devices.Host;
import Devices.Link;
import Devices.Router;

import java.util.List;

public class Network {

    private List<Host> hosts;
    private List<Router> routers;
    private List<Link> links;
    private List<Flow> flows;

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }

    public List<Router> getRouters() {
        return routers;
    }

    public void setRouters(List<Router> routers) {
        this.routers = routers;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Flow> getFlows() {
        return flows;
    }

    public void setFlows(List<Flow> flows) {
        this.flows = flows;
    }

}
