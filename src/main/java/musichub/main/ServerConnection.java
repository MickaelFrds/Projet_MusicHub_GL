package musichub.main;

import musichub.server_client.Server;

public class ServerConnection {
    /**
     * Executing this class allow the client to connect
     * @param args
     */
    public static void main(String[] args)  {
        Server server1 = new Server();
        while (true) {
            server1.run("localhost");
        }
    }
}
