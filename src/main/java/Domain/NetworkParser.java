package Domain;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

//Is responsible for deserialization of json into network objects
//should have a method to return the initialized network
public class NetworkParser {


    public static Network parseNetworkSpecs(String networkFile) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Network network = mapper.readValue(new File(networkFile), Network.class);
            return network;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
