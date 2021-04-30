package musichub.server_client;

import musichub.Exception.NoElementFoundException;
import musichub.Exception.NoPlayListFoundException;

import java.io.*;
import java.net.*;

public class Server {
    /**
     * This class handles the server sockets
     * @param ip
     */


    public void run (String ip) {
        try {
           ServerSocket ss = new ServerSocket (6000 );
          //System.out.println("En attente de connexion...");
           Socket socket = ss.accept();
          //System.out.println("client connecté");
           RequestManager requestManager1 = new RequestManager(socket);
           requestManager1.request();
        } catch (IOException e) {e.printStackTrace();}
    }


    public void close(ServerSocket ss){
        if (ss != null && !ss.isClosed()) {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
