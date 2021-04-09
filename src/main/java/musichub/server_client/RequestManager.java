package musichub.server_client;

import musichub.Exception.NoElementFoundException;
import musichub.Exception.NoPlayListFoundException;
import musichub.business.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import musichub.business.MusicHub.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import musichub.util.XMLHandler;
public class RequestManager {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    MusicHub musicHub = new MusicHub();
    MusicHub musicHubClient = new MusicHub();
    Object cmd;


    public RequestManager(Socket s) throws IOException {
        this.socket=s;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void request() throws IOException, ClassNotFoundException, NoPlayListFoundException, NoElementFoundException {

        String envoi;
        cmd = in.readObject();
        while (!(cmd).equals("q")){
            switch ((String) cmd) {
                case "a" -> sendAlbum();
                case "p" -> sendPlaylist();
                case "s" -> sendSongs();
                case "+a" -> createAlbum();
                case "+p" -> createPlaylist();
                case "+s" -> createSong();
                case "l" -> {
                    loadData(musicHub);
                }
                case "h" -> printAvailableCommands();
                default -> {
                    envoi = "This command doesn't exist \n Please retry !";
                    out.writeObject(envoi);
                }
            }
            cmd = in.readObject();
        }
    }




    public void sendAlbum() throws IOException {
        StringBuffer text =new StringBuffer();
        String TitleAlbums = musicHubClient.getTitleAlbum();
        text.append("Liste des albums :\n" + TitleAlbums );
        out.writeObject(text.toString());
    }

    public  void sendPlaylist(){
       try {
           String envoi = musicHubClient.getTitlePlaylist();
           out.writeObject(envoi);
       } catch (IOException e) {
            System.out.println(e.toString());
       }
    }

    public void sendSongs( ) throws IOException {
        String envoi = musicHubClient.getTitleSongs();
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
        out.writeObject("Song :" + s.getTitle()+ "has created !\nDo you want to update song list?\n");
        musicHub.addElement(s);
        musicHub.saveElements();
    }

    public void createAlbum() throws IOException, ClassNotFoundException {
        out.writeObject("Title :");
        Object aTitle = in.readObject();
        out.writeObject("Artiste :");
        Object aArtist = in.readObject();
        out.writeObject("Lenght :");
        Object aLength = in.readObject();
        out.writeObject("Date : (format : YYYY-DD-MM)");
        Object aDate = in.readObject();
        Album a = new Album((String) aTitle,(String) aArtist,Integer.parseInt((String)aLength),(String)aDate);
        out.writeObject("Album : "+a.getTitle()+" has created !\nDo you want to update album list?\n yes (y)   no (n)");
        musicHub.addAlbum(a);
        musicHub.saveAlbums();

    }

    public void createPlaylist() throws IOException, ClassNotFoundException {
        out.writeObject("Type the name of the playlist you wish to create:");
        Object titlePlaylist = in.readObject();
        PlayList pl = new PlayList((String) titlePlaylist);
        out.writeObject("Playlist " + pl.getTitle() + " is created\n");
        musicHub.addPlaylist(pl);
        musicHub.savePlayLists();
    }


    public void loadData(MusicHub musichub) throws IOException {
        musicHubClient.playlists.clear();
        for (Iterator<PlayList> playlistsIter = musichub.playlists(); playlistsIter.hasNext();) {
            PlayList currentPlayList = playlistsIter.next();
            musicHubClient.playlists.add(currentPlayList);
        }
        musicHubClient.albums.clear();
        for (Iterator<Album> albumsIter = musichub.albums(); albumsIter.hasNext();) {
            Album currentAlbum = albumsIter.next();
            musicHubClient.albums.add(currentAlbum);
        }
        musicHubClient.elements.clear();
        for (Iterator<AudioElement> audioElementIter = musichub.elements(); audioElementIter.hasNext();) {
            AudioElement currentElement = audioElementIter.next();
            musicHubClient.elements.add(currentElement);
        }
        out.writeObject("Data has refresh");
    }

    private void printAvailableCommands() throws IOException {
        StringBuffer menu =new StringBuffer();
        menu.append("a: display the album titles \n");
        menu.append("p: display the playlist titles \n");
        menu.append("s: display all songs \n");
        menu.append("+a: add a new album\n");
        menu.append("+p: add a new playlist\n");
        menu.append("+s: add a new song\n");
        menu.append("l: refresh database\n");
        menu.append("h: menu \n");
        menu.append(" \n");
        out.writeObject(menu);
        /*
        System.out.println("+: add a song to an album");
        System.out.println("p: create a new playlist from existing songs and audio books");
        System.out.println("-: delete an existing playlist");
        System.out.println(");
        System.out.println("q: quit program");
         */
    }

}