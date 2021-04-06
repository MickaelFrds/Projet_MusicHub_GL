package musichub.main;

import java.io.IOException;

public class ClientConnection {

    public static void main(String[] args) throws IOException {
        ServerConnection.Client client1 = new ServerConnection.Client();
        client1.connect("localHost");


    }
}
