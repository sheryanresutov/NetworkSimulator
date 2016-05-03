package Domain;

public class DataOne {

    Links links = new Links();
    Flows flows = new Flows();

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Flows getFlows() {
        return flows;
    }

    public void setFlows(Flows flows) {
        this.flows = flows;
    }

    class Links{
        LinkData L1;

        public LinkData getL1() {
            return L1;
        }

        public void setL1(LinkData l1) {
            L1 = l1;
        }
    }

    class Flows{
        FlowData F1;

        public FlowData getF1() {
            return F1;
        }

        public void setF1(FlowData f1) {
            F1 = f1;
        }
    }
}
