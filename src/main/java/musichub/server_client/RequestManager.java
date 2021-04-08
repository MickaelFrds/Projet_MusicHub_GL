package musichub.server_client;

import musichub.Exception.NoElementFoundException;
import musichub.Exception.NoPlayListFoundException;
import musichub.business.Album;
import musichub.business.MusicHub;
import musichub.business.PlayList;
import musichub.business.Song;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

public class RequestManager {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public RequestManager(Socket s) throws IOException {
        this.socket=s;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void request() throws IOException, ClassNotFoundException, NoPlayListFoundException, NoElementFoundException {
        MusicHub musicHub = new MusicHub();
        String envoi;
        Object cmd = in.readObject();
        while (!(cmd).equals("q")){
            switch ((String) cmd) {
                case "a" -> sendAlbum(musicHub);
                case "p" -> sendPlaylist(musicHub);
                case "s" -> sendSongs(musicHub);
                case "+a" -> createAlbum(musicHub);
                case "+p" -> createPlaylist();
                case "+s" -> createSong();
                case "h" -> printAvailableCommands();
                default -> {
                    envoi = "This command doesn't exist \n Please retry !";
                    out.writeObject(envoi);
                }
            }
            cmd = in.readObject();
        }
    }




    public void sendAlbum(MusicHub musicHub) throws IOException {
        StringBuffer text =new StringBuffer();
        String TitleAlbums = musicHub.getTitleAlbum();
        text.append("Liste des albums :\n" + TitleAlbums );
        out.writeObject(text.toString());
    }

    public  void sendPlaylist(MusicHub musicHub){
       try {
           String envoi = musicHub.getTitlePlaylist();
           out.writeObject(envoi);
       } catch (IOException e) {
            System.out.println(e.toString());
       }
    }

    public void sendSongs(MusicHub musicHub) throws IOException {
        String envoi = musicHub.getTitleSongs();
        out.writeObject(envoi);
    }

    public void createSong() throws IOException, ClassNotFoundException {
        out.writeObject("Title :");
        Object sTitle = in.readObject();
        out.writeObject("Artiste :");
        Object sArtist = in.readObject();
        out.writeObject("Length :");
        Object sLength = in.readObject();
        out.writeObject("Content :");
        Object sContent = in.readObject();
        out.writeObject("Genre : (jazz,classic,hiphop,rock,pop,rap)");
        Object sGenre = in.readObject();
        Song s =new Song( (String) sTitle, (String) sArtist, Integer.parseInt((String) sLength) , (String) sContent, (String) sGenre);
        out.writeObject("Song :" + s.getTitle()+ "has created !");
    }

    public void createAlbum(MusicHub musicHub) throws IOException, ClassNotFoundException {
        out.writeObject("Title :");
        Object aTitle = in.readObject();
        out.writeObject("Artiste :");
        Object aArtist = in.readObject();
        out.writeObject("Lenght :");
        Object aLength = in.readObject();
        out.writeObject("Date : (format : YYYY-DD-MM)");
        Object aDate = in.readObject();
        Album a = new Album((String) aTitle,(String) aArtist,Integer.parseInt((String)aLength),(String)aDate);
        out.writeObject("Album : "+a.getTitle()+" has created !");
        //musicHub.addAlbum(a);
    }

    public void createPlaylist() throws IOException, ClassNotFoundException {
        out.writeObject("Type the name of the playlist you wish to create:");
        Object titlePlaylist = in.readObject();
        PlayList pl = new PlayList((String) titlePlaylist);
        out.writeObject("Playlist "+pl.getTitle()+" is created");
    }




    private void printAvailableCommands() throws IOException {
        StringBuffer menu =new StringBuffer();
        menu.append("a: display the album titles \n");
        menu.append("p: display the playlist titles \n");
        menu.append("s: display all songs \n");
        menu.append("+a: add a new album\n");
        menu.append("+p: add a new playlist\n");
        menu.append("+s: add a new song\n");
        menu.append("h: menu \n");
        menu.append(" \n");
        menu.append(" \n");
        out.writeObject(menu);
        /*
        System.out.println("+: add a song to an album");
        System.out.println("p: create a new playlist from existing songs and audio books");
        System.out.println("-: delete an existing playlist");
        System.out.println("s: save elements, albums, playlists");
        System.out.println("q: quit program");
         */
    }

}