package Domain;

import java.io.IOException;

public class Main {

    public static int MS_PER_SEC = 1000;
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
    public static int TEST_CASE;
    public static int EVENT_LOG_FREQ;
    public static boolean USE_FAST;
    public static int WAIT_TIME;
    public static String PATH;
    //1 min 53 sec for Tahoe Case 0
    //7 min 1 sec for Tahoe Case 1
    //25 min 16 sec for Tahoe Case 2
    //2 min 58 sec for FAST Case 0
    //22 min 53 sec for FAST Case 1
    //57 min 4 sec for FAST Case 2

    public static void main(String [] args) throws IOException {
        if(args.length != 5){
            System.out.println("Not enough arguments, need five.\n " +
                            "Call like this: java -cp {jarPath}/networksimulator-1.0-SNAPSHOT.jar Main testcase logfreq waittime usefast(true/false) path");
            System.exit(-1);
        }
        TEST_CASE = Integer.parseInt(args[0]);
        EVENT_LOG_FREQ = Integer.parseInt(args[1]);
        WAIT_TIME = Integer.parseInt(args[2]);
        USE_FAST = Boolean.parseBoolean(args[3]);
        PATH = args[4];

        EventQueueManager eventQueueManager = new EventQueueManager();
        network = NetworkGenerator.parseNetworkSpecs(
                PATH+"/networksimulator/src/main/java/Domain/TestCase"+TEST_CASE+".json",eventQueueManager);
        eventQueueManager.runSimulation();
    }
}
