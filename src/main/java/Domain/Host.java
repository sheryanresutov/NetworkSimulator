package Domain;

import java.util.List;

public class Host {

    //has a network address, which uniquely identifies each node on the network
    private String id;

    private List flows;

    //will have at most one link connected
    private String link;


    //can process an infinite amount of incoming data instantaneously, but outgoing data
    //must sit on a link buffer until the link is free



}
