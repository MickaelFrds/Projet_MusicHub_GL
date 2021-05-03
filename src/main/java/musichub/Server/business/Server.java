package musichub.Server.business;

import musichub.Server.business.RequestManager;

import java.io.*;
import java.net.*;
/**
 * This class handles the server sockets
 */
public class Server {

    public void run() {
        try {
            ServerSocket ss = new ServerSocket(6000);
            System.out.println("En attente de connexion...");
            Socket socket = ss.accept();
            System.out.println("client connecté");
            RequestManager requestManager1 = new RequestManager(socket);
            requestManager1.request();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
