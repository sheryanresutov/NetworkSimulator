package Domain;

public class DataTwo {

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
        LinkData L0;
        LinkData L1;
        LinkData L2;
        LinkData L3;
        LinkData L4;
        LinkData L5;

        public LinkData getL0() {
            return L0;
        }

        public void setL0(LinkData l0) {
            L0 = l0;
        }

        public LinkData getL1() {
            return L1;
        }

        public void setL1(LinkData l1) {
            L1 = l1;
        }

        public LinkData getL2() {
            return L2;
        }

        public void setL2(LinkData l2) {
            L2 = l2;
        }

        public LinkData getL3() {
            return L3;
        }

        public void setL3(LinkData l3) {
            L3 = l3;
        }

        public LinkData getL4() {
            return L4;
        }

        public void setL4(LinkData l4) {
            L4 = l4;
        }

        public LinkData getL5() {
            return L5;
        }

        public void setL5(LinkData l5) {
            L5 = l5;
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
