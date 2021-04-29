package test;
import musichub.Exception.NoElementFoundException;
import musichub.business.AudioElement;
import musichub.business.MusicHub;
import musichub.business.Song;

import javax.lang.model.element.Element;

public class SongTest {


    public static void main(String[] args) throws NoElementFoundException {



        MusicHub musicHub = new MusicHub();


        //test ajouter son
        Song songTest = new Song("testTitle", "artistTest", 10, "contentTest","JAZZ") {
        };
        musicHub.addElement(songTest);

        //test supprimer album
        String cmd = songTest.getTitle();
        AudioElement theSong = null;
        boolean result = false;
        for (AudioElement so : musicHub.elements) {
            if (so.getTitle().toLowerCase().equals(((String)cmd).toLowerCase())) {
                theSong = so;
                break;
            }
        }

        if (theSong != null)
            result = musicHub.elements.remove(theSong);
        if (!result) throw new NoElementFoundException("Song " + ((String)cmd) + " not found!");


        //test supprimer album vide

        musicHub.elements.clear();
        theSong = null;
        result = false;
        for (AudioElement so : musicHub.elements) {
            if (so.getTitle().toLowerCase().equals(((String)cmd).toLowerCase())) {
                theSong = so;
                break;
            }
        }

        if (theSong != null)
            result = musicHub.albums.remove(theSong);
        if (!result) throw new NoElementFoundException("Album " + ((String)cmd) + " not found!");




    }
}
