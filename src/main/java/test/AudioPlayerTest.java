package test;

import musichub.business.AudioPlayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class AudioPlayerTest {
        @org.junit.jupiter.api.Test
        void run() {
            AudioPlayer p1= new AudioPlayer("zouker");
            p1.run();
        }
    }