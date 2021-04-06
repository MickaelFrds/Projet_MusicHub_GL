package musichub.business;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Server {
    private ServerSocket ss;
    private final String ip = "localHost";
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;

    public void run (String ip) {
        try {
            ss = new ServerSocket (6000 );
            System.out.println("Server waiting for connection...");
            Socket socket = ss.accept();//establishes connection
            System.out.println("Connected as " + ip);
            input = new ObjectInputStream(socket.getInputStream());
            String cmd = (String)input.readObject();
            switch (cmd){
                case "h" : displayAlbum(socket); break;
                default: break;
            }
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
        }
    }

    public void displaySong() throws IOException {
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }

    public void displayAlbum(Socket socket) throws IOException {
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        String textToSend = new String("Albums");
        output.writeObject(textToSend);
    }
    public void displayPlaylist() throws IOException {
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }
    public void playSong() throws IOException {
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }

    public void close(){
        if (ss != null && !ss.isClosed()) {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
