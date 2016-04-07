package Domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router {

    private String id;

    private List<Link> links = new ArrayList<>();

    //need to implement a dynamic routing protocol that uses link cost as a distance metric
    //  and route packets along the shortest path according to this metric
    private Map routingTable = new HashMap<>();

    //the dynamic routing protocol must be decentralized, and thus will use message passing
    //  to communicate among routers

    //this message passing must send packets along the link and thus coexist along with the rest of
    //  the simulation

    //routing table?

    //need to initialize routing table
    //need to update routing table
    //need to receive routing packet
    //need to receive regular packet



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List getLinks() {
        return links;
    }

    public void setLinks(List links) {
        this.links = links;
    }

    public Map getRoutingTable() {
        return routingTable;
    }

    public void setRoutingTable(Map routingTable) {
        this.routingTable = routingTable;
    }

    public void addLink(Link link) {
        links.add(link);
    }
}
