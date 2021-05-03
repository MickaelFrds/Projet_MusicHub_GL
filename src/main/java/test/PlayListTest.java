package test;

import musichub.Server.business.Album;
import musichub.Server.business.PlayList;
import musichub.Server.business.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayListTest {

    @Test
    void Construteurs() {
        PlayList PlaylistTest = new PlayList("pTitle");
    }

    @Test
    void addElement() {
        PlayList PlaylistTest = new PlayList("pTitle");
        Song SongTest = new Song( "stitle",  "sartist",  1 , "sTest.wav" ,  "jazz");
        Song SongTest1 = new Song( "stitle1",  "sartist",  1 , "sTest.wav" ,  "jazz");
        Song SongTest2 = new Song( "stitle2",  "sartist",  1 , "sTest.wav" ,  "jazz");
        PlaylistTest.addElement(SongTest.getUUID());
        PlaylistTest.addElement(SongTest1.getUUID());
        PlaylistTest.addElement(SongTest2.getUUID());
    }

    @Test
    void getElements() {
        PlayList PlaylistTest = new PlayList("pTitle");
        Song SongTest = new Song( "stitle",  "sartist",  1 , "sTest.wav" ,  "jazz");
        Song SongTest1 = new Song( "stitle1",  "sartist",  1 , "sTest.wav" ,  "jazz");
        Song SongTest2 = new Song( "stitle2",  "sartist",  1 , "sTest.wav" ,  "jazz");
        PlaylistTest.addElement(SongTest.getUUID());
        PlaylistTest.addElement(SongTest1.getUUID());
        PlaylistTest.addElement(SongTest2.getUUID());
        System.out.println(PlaylistTest.getElements());
    }

    @Test
    void getTitle() {
        PlayList PlaylistTest = new PlayList("pTitle");
        System.out.println(PlaylistTest.getTitle());
    }
}