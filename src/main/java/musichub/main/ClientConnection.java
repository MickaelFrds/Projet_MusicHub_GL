package musichub.main;

import musichub.server_client.*;

public class ClientConnection {

    public static void main(String[] args)  {
        Client client1 = new Client();
        client1.connect("localHost",6000);
    }
}
