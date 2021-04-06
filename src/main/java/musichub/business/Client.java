package musichub.business;


import java.io.*;
import java.net.*;

public class Client {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;

    public void connect(String ip)
    {
        int port = 6000;
        try  {
            //create the socket; it is defined by an remote IP address (the address of the server) and a port number
            socket = new Socket(ip, port);

            //create the streams that will handle the objects coming and going through the sockets
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            String textToSend = new String("Je me connecte");
            System.out.println("text sent to the server: " + textToSend);
            output.writeObject(textToSend);		//serialize and write the String to the stream
        } catch  (UnknownHostException uhe) {
            uhe.printStackTrace();
        } catch  (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            try {
                input.close();
                output.close();
                socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}