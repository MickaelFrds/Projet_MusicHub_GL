package test;

import musichub.Server.business.Album;
import musichub.Server.business.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.Element;

import static org.junit.jupiter.api.Assertions.*;

class SongTest {
    @BeforeEach
    void setUp() {
        Album AlbumTest = new Album("atitle","aartist",10, "1000-02-10");
        Song SongTest = new Song( "stitle",  "sartist",  1 , "sTest.wav" ,  "jazz");
    }

    @Test
    void TestAddSong() {
        Album AlbumTest = new Album("atitle","aartist",10,"1000-02-10");
        Song SongTest = new Song( "stitle",  "sartist",  1 , "sTest.wav" ,  "jazz");
        AlbumTest.addSong(SongTest.getUUID());
        AlbumTest.getSongs();
    }

    @Test
    void TestAddSongWithFalseDate() {
        Album AlbumTest = new Album("atitle","aartist",10,"10");
        Song SongTest = new Song( "stitle",  "sartist",  1 , "sTest.wav" ,  "jazz");
        AlbumTest.addSong(SongTest.getUUID());
        AlbumTest.getSongs();
    }
}