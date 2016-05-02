package Domain;

import java.io.IOException;

public class Main {

    public static final int MS_PER_SEC = 1000;
    public static Network network;
    public static int FLOW_PACKET_SIZE = 1024;
    public static int ACK_PACKET_SIZE = 64;
    public static int ROUTING_PACKET_SIZE = 64;
    public static int BYTES_PER_KB = 1024;
    public static int KB_PER_MB = 1024;
    public static int BITS_PER_BYTE = 8;
    public static int BYTES_PER_MEGABIT = BYTES_PER_KB * KB_PER_MB / BITS_PER_BYTE;
    public static int RATE_INTERVAL = 1000;
    public static int SEQNUM_FOR_NONFLOWS = -1;
    public static double ALPHA = 20;
    public static int UPPER_TIME_ROUTING_LIMIT = 400000;
    public static int TEST_CASE=2;

    public static void main(String [] args) throws IOException {
        String testCase = "0";
        EventQueueManager eventQueueManager = new EventQueueManager();
        network = NetworkGenerator.parseNetworkSpecs(
                "/home/sheryan/networksimulator/src/main/java/Domain/TestCase"+TEST_CASE+".json",eventQueueManager);
        eventQueueManager.runSimulation();
        System.out.println();
    }
}
