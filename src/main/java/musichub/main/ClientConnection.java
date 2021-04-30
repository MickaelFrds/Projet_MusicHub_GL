package musichub.main;

import musichub.server_client.*;

public class ClientConnection {
    /**
     * Executing this class after ServerConnection allow the client connection to the server
     * @param args
     */
    public static void main(String[] args)  {
        Client client1 = new Client();
        client1.connect("localHost",6000);
    }
}
