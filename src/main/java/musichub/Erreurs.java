package musichub;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Erreurs {

    private String chemin ;
    private File fichier ;
    private FileWriter fw ;
    private BufferedWriter bw;
    private DateFormat format;
    private Date date ;
    private String content;

    public void WriteError(String msg)  {
        try {
            String chemin = System.getProperty("user.dir") + "\\files\\Erreurs.txt" ;
            File fichier =new File(chemin);
            FileWriter fw = new FileWriter(fichier,true);
            BufferedWriter bw = new BufferedWriter(fw);
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String content =   format.format(date) + "\n" + msg + "\n";
            bw.write(content);
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't write errors in the file Errors.txt\n");
        }
    }
}
