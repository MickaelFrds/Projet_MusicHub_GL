package test;

import static org.junit.jupiter.api.Assertions.*;

import musichub.Exception.NoPlayListFoundException;
import musichub.business.MusicHub;
import musichub.business.PlayList;
/**
 * This class regroups some tests on the PlayList class
 */
class PlayListTest {


    public static void main(String[] args) throws NoPlayListFoundException {



        MusicHub musicHub = new MusicHub();


        //test ajouter playlist
        PlayList pl = new PlayList("testPlaylist");
        musicHub.addPlaylist(pl);
        musicHub.getTitlePlaylist();

        //test supprimer playlist

        musicHub.deletePlayList("testPlaylist");
        musicHub.getTitlePlaylist();
        //test supprimer playlist vide

        musicHub.playlists.clear();
        musicHub.deletePlayList("testPlaylist");// playlist qui n'existe pas




    }
}
