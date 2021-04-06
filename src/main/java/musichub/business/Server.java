package musichub.business;

import java.io.IOException;
import java.net.*;

public class Server {
    private final String ip = "localhost";
    private ServerSocket ss;

    public void connect (String ip) {
        try {
            ss = new ServerSocket (6000 );
            System.out.println("Server waiting for connection...");
            while (true) {
                Socket socket = ss.accept();//establishes connection
                System.out.println("Connected as " + ip);
                // create a new thread to handle client socket
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            if (ss != null && !ss.isClosed()) {
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

}
