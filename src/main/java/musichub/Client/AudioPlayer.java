package musichub.Client;

import musichub.Erreurs;

import javax.sound.sampled.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This class handles the reading of audio files
 */

public class AudioPlayer implements Runnable {

    private AudioInputStream audioInput;
    private AudioFormat audioFormat;
    private int numBytes;
    private byte[] tab;
    private SourceDataLine line;
    private boolean stop = false;

    public AudioPlayer(File file) throws IOException {
        try {
            this.audioInput = AudioSystem.getAudioInputStream(file);
            int bytesPerFrame = audioInput.getFormat().getFrameSize();
            this.numBytes = 1024 * bytesPerFrame;
            this.tab = new byte[numBytes];
            this.audioFormat = audioInput.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            this.line = (SourceDataLine) AudioSystem.getLine((info));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            Erreurs erreurs = new Erreurs();
            erreurs.WriteError("file not found !" );
            System.out.println("file not found ! Please retry");
        }
    }

    public void run() {
        try {
            line.open(audioFormat);
            line.start();
            int nb;
            while ((nb = audioInput.read(tab, 0, numBytes)) != -1) {
                line.write(tab, 0, nb);
                if (stop){
                    line.close();
                    return;
                }
            }
            line.close();
        } catch (LineUnavailableException | IOException e) {
            Erreurs erreurs = new Erreurs();
            erreurs.WriteError("Can't run the audioPlayer" );
            System.out.println("Can't run the audioPlayer");
        }
    }

    public void stop() {
        stop = true;
    }

}
