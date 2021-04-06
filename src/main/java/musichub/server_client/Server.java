package musichub.server_client;

import java.io.*;
import java.net.*;

public class Server {

    public void run (String ip) {
        try {
           ServerSocket ss = new ServerSocket (6000 );
           System.out.println("En attente de connexion...");
           Socket socket = ss.accept();
           System.out.println("client connecté");

           //request
           ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
           out.writeObject("Bienvenue");
           ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
           System.out.println("Le serveur a créé les flux");

           Object recu = in.readObject();
           System.out.println("Client :"+ recu);
        } catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
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
