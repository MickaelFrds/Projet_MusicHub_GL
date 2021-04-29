package musichub.business;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public abstract class AudioPlayer {

    private AudioInputStream audioInput;
    private AudioFormat audioFormat;
    private int bytesPerFrame;
    private int numBytes;
    private byte[] tab;
    private DataLine.Info info;
    public SourceDataLine line;

    public AudioPlayer(Object file) {
        try {
            this.audioInput = AudioSystem.getAudioInputStream((File) file);
            this.bytesPerFrame = audioInput.getFormat().getFrameSize();
            this.numBytes = 1024 * bytesPerFrame;
            this.tab = new byte[numBytes];
            this.audioFormat = audioInput.getFormat();
            this.info = new DataLine.Info(SourceDataLine.class, audioFormat);
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
            while ((nb = audioInput.read(tab, 0, numBytes)) != -1) {
                line.write(tab, 0, nb);
            }
            line.close();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
}
