package Domain;

public class Main {

    public static void main(String [] args){
        Network network = NetworkGenerator.parseNetworkSpecs(
                "/home/sheryan/networksimulator/src/main/java/Domain/Input.json");
        System.out.println();
        //at this point the network is initialized
        //have to start the simulation
    }
}
