package musichub.main;

import musichub.business.Client;

import java.io.IOException;

public class ClientConnection {

    public static void main(String[] args) throws IOException {
        Client client1 = new Client();
        client1.connect("localHost");
    }
}
