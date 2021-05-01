package musichub.business;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class AudioPlayer implements Runnable {


    private AudioInputStream audioInput;
    private AudioFormat audioFormat;
    private int numBytes;
    private byte[] tab;
    private SourceDataLine line;
    private boolean stop = false;


    public AudioPlayer(File file) {
        try {
            this.audioInput = AudioSystem.getAudioInputStream(file);
            int bytesPerFrame = audioInput.getFormat().getFrameSize();
            this.numBytes = 1024 * bytesPerFrame;
            this.tab = new byte[numBytes];
            this.audioFormat = audioInput.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            this.line = (SourceDataLine) AudioSystem.getLine((info));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            line.open(audioFormat);
            line.start();
            int nb;
            while (!stop) {
                if ((nb = audioInput.read(tab, 0, numBytes)) == -1) {
                    line.close();
                    return;
                }
                    line.write(tab, 0, nb);
                line.close();
            }
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        stop = true;
    }

}
