package musichub.server_client;


import java.io.*;
import java.net.*;

public class Client {

    private Socket socket;

    public void connect(String ip)
    {
        int port = 6000;
        try  {
            socket = new Socket(ip, port);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject("hello");
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Client a cree les flux");
            Object recu = in.readObject();
            System.out.println("Serveur : " + recu);
            recu = in.readObject();
            System.out.println(recu);
        } catch  (IOException | ClassNotFoundException uhe) {
            uhe.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void addSong(){
    }

    public void addAlbum(){

    }

    public void addPlaylist(){

    }

    public void getAlbum()  {

    }
}