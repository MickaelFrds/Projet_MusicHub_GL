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
        Object cmd ;
        while (true){
            cmd = scan.nextLine();
            if (cmd.equals("h"))printAvailableCommands();
            out.writeObject(cmd);
            Object recu = in.readObject();
            System.out.println(recu);
        }
    }

    private void printAvailableCommands() {
        System.out.println("a: display the album titles");
        System.out.println("p: display the playlist titles");
        System.out.println("s: display all songs");

        System.out.println("+a: add a new song");
        System.out.println("+p: add a new album");
        System.out.println("+s: add a new playlist");
        System.out.println("h: menu");
        /*
        System.out.println("+: add a song to an album");
        System.out.println("p: create a new playlist from existing songs and audio books");
        System.out.println("-: delete an existing playlist");
        System.out.println("s: save elements, albums, playlists");
        System.out.println("q: quit program");
         */
    }

}