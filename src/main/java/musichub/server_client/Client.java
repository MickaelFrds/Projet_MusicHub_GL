package musichub.server_client;


import javax.sound.sampled.AudioInputStream;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    /**
     * This class handles the client sockets
     */
    private Socket socket;
    private ObjectOutputStream out;
    ObjectInputStream in;


    public void connect(String ip,int port) {
        try {
            this.socket = new Socket(ip, port);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            requestClient();
        } catch (IOException | ClassNotFoundException uhe) {
            uhe.printStackTrace();

        } finally {
            try {
                socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }


    public void requestClient() throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Type 'h' to display the menu : ");
        Object cmd="a";
        while (!cmd.equals("q")) {
            cmd = scan.nextLine();
            out.writeObject(cmd);
            Object recu = in.readObject();
            System.out.println(recu);
        }
    }
}

