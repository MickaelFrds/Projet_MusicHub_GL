package test;

import musichub.Exception.NoPlayListFoundException;
import musichub.business.MusicHub;
import musichub.business.PlayList;

public class PlaylistTest {


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
