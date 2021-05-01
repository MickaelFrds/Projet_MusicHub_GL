package musichub.main;

import musichub.server_client.Server;

public class ServerConnection {

    public static void main(String[] args)  {
        Server server1 = new Server();
        server1.run();
    }
}
