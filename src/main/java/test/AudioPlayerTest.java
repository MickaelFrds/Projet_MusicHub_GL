package test;

import musichub.business.AudioPlayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class AudioPlayerTest {
        @org.junit.jupiter.api.Test
        void run() {
            String titleSong = "zouker";
            String chemin = System.getProperty("user.dir") + "\\files\\" + titleSong + ".wav";
           AudioPlayer p1= new AudioPlayer(new File(chemin));
           p1.run();
        }
    }