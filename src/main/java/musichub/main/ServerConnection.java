package musichub.main;

import musichub.server_client.Server;

import java.io.IOException;

public class ServerConnection {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
    Server server1 = new Server();
    server1.run("localhost");
    }
}
