package Domain;

public class DataThree {

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
        LinkData L2;
        LinkData L3;
        LinkData L4;
        LinkData L5;
        LinkData L6;
        LinkData L7;
        LinkData L8;
        LinkData L9;

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

        public LinkData getL6() {
            return L6;
        }

        public void setL6(LinkData l6) {
            L6 = l6;
        }

        public LinkData getL7() {
            return L7;
        }

        public void setL7(LinkData l7) {
            L7 = l7;
        }

        public LinkData getL8() {
            return L8;
        }

        public void setL8(LinkData l8) {
            L8 = l8;
        }

        public LinkData getL9() {
            return L9;
        }

        public void setL9(LinkData l9) {
            L9 = l9;
        }
    }

    class Flows{
        FlowData F1;
        FlowData F2;
        FlowData F3;

        public FlowData getF1() {
            return F1;
        }

        public void setF1(FlowData f1) {
            F1 = f1;
        }

        public FlowData getF2() {
            return F2;
        }

        public void setF2(FlowData f2) {
            F2 = f2;
        }

        public FlowData getF3() {
            return F3;
        }

        public void setF3(FlowData f3) {
            F3 = f3;
        }
    }
}
