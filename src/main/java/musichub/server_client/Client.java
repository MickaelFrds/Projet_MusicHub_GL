package musichub.server_client;


import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    private Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    public void connect(String ip) {
        int port = 6000;
        try {
            socket = new Socket(ip, port);
            requestClient(socket);
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


    public void requestClient(Socket socket) throws IOException, ClassNotFoundException {

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
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

