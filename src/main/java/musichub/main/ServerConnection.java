package musichub.main;

import musichub.business.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
    Server server1 = new Server();
    server1.run("localhost");
    }

    public static class Client {

        private ObjectOutputStream output;
        private ObjectInputStream input;
        private Socket socket;

        public void connect(String ip)
        {
            int port = 6000;
            try  {
                //create the socket; it is defined by an remote IP address (the address of the server) and a port number
                socket = new Socket(ip, port);
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                String cmd = "h";
                output.writeObject(cmd);
                String album = (String)input.readObject();
                System.out.println(album);
            } catch  (IOException | ClassNotFoundException uhe) {
                uhe.printStackTrace();
            } finally {
                try {
                    input.close();
                    output.close();
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

        public void getAlbum(Socket socket) throws IOException, ClassNotFoundException {

        }
    }
}
