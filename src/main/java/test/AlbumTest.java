package test;

import musichub.Server.business.Album;
import musichub.Server.business.Song;
import org.junit.jupiter.api.Test;

class AlbumTest {

    @Test
    void Construteurs() {
        Album AlbumTest = new Album("atitle","aartist",10, "1000-02-10");
        Song SongTest = new Song( "stitle",  "sartist",  1 , "sTest.wav" ,  "jazz");
        Album AlbumTest2 = new Album("atitle","aartist",10,"10");
    }

    @Test
    void AddSong() {
        Album AlbumTest = new Album("atitle","aartist",10,"1000-02-10");
        Song SongTest = new Song( "stitle",  "sartist",  1 , "sTest.wav" ,  "jazz");
        AlbumTest.addSong(SongTest.getUUID());
    }

    @Test
    void getSongs() {
        Album AlbumTest = new Album("atitle","aartist",10,"1000-02-10");
        Song SongTest = new Song( "stitle",  "sartist",  1 , "sTest.wav" ,  "jazz");
        Song SongTest1 = new Song( "stitle1",  "sartist",  1 , "sTest.wav" ,  "jazz");
        Song SongTest2 = new Song( "stitle2",  "sartist",  1 , "sTest.wav" ,  "jazz");
        AlbumTest.addSong(SongTest.getUUID());
        AlbumTest.addSong(SongTest1.getUUID());
        AlbumTest.addSong(SongTest2.getUUID());
        System.out.println(AlbumTest.getSongs());
    }

    @Test
    void getSongsRandomly() {
        Album AlbumTest = new Album("atitle","aartist",10,"1000-02-10");
        Song SongTest = new Song( "stitle",  "sartist",  1 , "sTest.wav" ,  "jazz");
        Song SongTest1 = new Song( "stitle1",  "sartist",  1 , "sTest.wav" ,  "jazz");
        Song SongTest2 = new Song( "stitle2",  "sartist",  1 , "sTest.wav" ,  "jazz");
        AlbumTest.addSong(SongTest.getUUID());
        AlbumTest.addSong(SongTest1.getUUID());
        AlbumTest.addSong(SongTest2.getUUID());
        System.out.println(AlbumTest.getSongsRandomly());
    }


    @Test
    void getTitle() {
        Album AlbumTest = new Album("atitle","aartist",10,"1000-02-10");
        System.out.println(AlbumTest.getTitle());
    }

    @Test
    void getDate() {
        Album AlbumTest = new Album("atitle","aartist",10,"1000-02-10");
        System.out.println(AlbumTest.getDate());
    }
}