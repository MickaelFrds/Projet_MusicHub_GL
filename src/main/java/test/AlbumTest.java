package test;
import musichub.Exception.NoAlbumFoundException;
import musichub.business.Album;
import musichub.business.MusicHub;

public class AlbumTest {


    public static void main(String[] args) throws NoAlbumFoundException {



        MusicHub musicHub = new MusicHub();


        //test ajouter album
        Album albumtest = new Album("testAlbum", "artistTest",10, "1920-14-12");
        musicHub.addAlbum(albumtest);

        //test supprimer album
        String cmd = albumtest.getTitle();
        Album theAlbum = null;
        boolean result = false;
        for (Album al : musicHub.albums) {
            if (al.getTitle().toLowerCase().equals(((String)cmd).toLowerCase())) {
                theAlbum = al;
                break;
            }
        }

        if (theAlbum != null)
            result = musicHub.albums.remove(theAlbum);
        if (!result) throw new NoAlbumFoundException("Playlist " + ((String)cmd) + " not found!");


        //test supprimer album qui n'existe pas

        musicHub.albums.clear();
        theAlbum = null;
        result = false;
        for (Album al : musicHub.albums) {
            if (al.getTitle().toLowerCase().equals(((String)cmd).toLowerCase())) {
                theAlbum = al;
                break;
            }
        }

        if (theAlbum != null)
            result = musicHub.albums.remove(theAlbum);
        if (!result) throw new NoAlbumFoundException("Album " + ((String)cmd) + " not found!");




    }
}
