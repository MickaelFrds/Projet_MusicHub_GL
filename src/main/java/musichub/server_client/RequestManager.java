package musichub.server_client;

import musichub.Exception.NoAlbumFoundException;
import musichub.Exception.NoElementFoundException;
import musichub.Exception.NoPlayListFoundException;
import musichub.business.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import musichub.business.MusicHub.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import musichub.util.XMLHandler;
import org.w3c.dom.NodeList;

public class RequestManager {
    /**
     * This class regroups all requests the Client can send to the server
     */
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    MusicHub musicHub = new MusicHub();
    MusicHub musicHubClient = new MusicHub();

    public static final String DIR = System.getProperty("user.dir");
    public static final String ALBUMS_FILE_PATH = DIR + "\\files\\albums.xml";
    public static final String PLAYLISTS_FILE_PATH = DIR + "\\files\\playlists.xml";
    public static final String ELEMENTS_FILE_PATH = DIR + "\\files\\elements.xml";

    public  List<Album> albums2= new LinkedList<Album>(musicHub.albums);
    public  List<PlayList> playlists2= new LinkedList<PlayList>(musicHub.playlists);
    public  List<AudioElement> elements2= new LinkedList<AudioElement>(musicHub.elements);
    Object cmd;


    public RequestManager(Socket s) {
        try {
            this.socket=s;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void request(){

        String envoi;
        try {
            cmd = in.readObject();
            while (!(cmd).equals("q")){
                switch ((String) cmd) {
                    case "a" -> sendAlbum();
                    case "p" -> sendPlaylist();
                    case "s" -> sendSongs();
                    case "+a" -> createAlbum();
                    case "+p" -> createPlaylist();
                    case "+s" -> createSong();
                    case "-a" -> remove_albums();
                    case "-p" -> remove_playlists();
                    case "-s" -> remove_elements();
                    case "play" -> playSong();
                    case "l" -> loadData();
                    case "c" -> cancelchanges();
                    case "h" -> printAvailableCommands();
                    default -> {
                        envoi = "This command doesn't exist \n Please retry !";
                        out.writeObject(envoi);
                    }
                }
                cmd = in.readObject();
            }
        } catch (IOException | ClassNotFoundException | NoPlayListFoundException | NoAlbumFoundException | NoElementFoundException e) {
            e.printStackTrace();
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
        out.writeObject("Song :" + s.getTitle()+ "has been created !\n");
        musicHub.addElement(s);
        musicHub.saveElements();
    }

    public void createAlbum() throws IOException, ClassNotFoundException {
        out.writeObject("Title :");
        Object aTitle,aArtist,aLength,aDate;
        do{
            aTitle = in.readObject();
            if(aTitle==""){out.writeObject("The title should not be empty");}
        }while (aTitle=="");
        out.writeObject("Artiste :");
        do{
            aArtist = in.readObject();
            if(aArtist==""){out.writeObject("The artist should not be empty");}
        }while (aArtist=="");
        out.writeObject("Lenght :");
        Integer number;
        do{
            aLength = in.readObject();
            try{
            number=(Integer.parseInt((String)aLength));
            }catch (NumberFormatException e){ System.err.println("un nombre entier est attendu");}
            if(aLength=="" || (Integer.parseInt((String)aLength)<1)){out.writeObject("The Length format is not valid");}
        }while (aLength=="" || (Integer.parseInt((String)aLength)<1));
        out.writeObject("Date : (format : YYYY-DD-MM)");
        do{
            aDate = in.readObject();
            if(aDate==""){out.writeObject("The date should not be empty");}
        }while (aDate=="");
        Album a = new Album((String) aTitle,(String) aArtist,Integer.parseInt((String)aLength),(String)aDate);
        out.writeObject("Album : "+a.getTitle()+" has been created !\n");
        musicHub.addAlbum(a);
        musicHub.saveAlbums();

    }

    public void createPlaylist() throws IOException, ClassNotFoundException {
        out.writeObject("Type the name of the playlist you wish to create:");
        Object titlePlaylist;
        do{
            titlePlaylist = in.readObject();
            if(titlePlaylist==""){out.writeObject("The name should not be empty");}
            }while (titlePlaylist=="");
        PlayList pl = new PlayList((String) titlePlaylist);
        out.writeObject("Playlist " + pl.getTitle() + " is created\n");
        musicHub.addPlaylist(pl);
        musicHub.savePlayLists();
    }


    public void loadData() throws IOException {
        musicHubClient.playlists.clear();
        for (Iterator<PlayList> playlistsIter = musicHub.playlists(); playlistsIter.hasNext();) {
            PlayList currentPlayList = playlistsIter.next();
            musicHubClient.playlists.add(currentPlayList);
        }
        musicHubClient.albums.clear();
        for (Iterator<Album> albumsIter = musicHub.albums(); albumsIter.hasNext();) {
            Album currentAlbum = albumsIter.next();
            musicHubClient.albums.add(currentAlbum);
        }
        musicHubClient.elements.clear();
        for (Iterator<AudioElement> audioElementIter = musicHub.elements(); audioElementIter.hasNext();) {
            AudioElement currentElement = audioElementIter.next();
            musicHubClient.elements.add(currentElement);
        }
        out.writeObject("Data has refresh");
    }

    public void playSong(){
        try {
            out.writeObject("Quel song voulez vous jouer ? ");
            AudioPlayer p = new AudioPlayer((String) in.readObject());
            p.run(out);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cancelchanges() throws IOException{
        musicHubClient.playlists.clear();
        musicHubClient.playlists.addAll(playlists2);
        musicHubClient.elements.clear();
        musicHubClient.elements.addAll(elements2);
        musicHubClient.albums.clear();
        musicHubClient.albums.addAll(albums2);

        musicHub.albums.clear();
        musicHub.albums.addAll(albums2);
        musicHub.playlists.clear();
        musicHub.playlists.addAll(playlists2);
        musicHub.elements.clear();
        musicHub.elements.addAll(elements2);

        musicHub.saveElements();
        musicHub.savePlayLists();
        musicHub.saveAlbums();

        out.writeObject("changes has been canceled");
    }

    public void remove_playlists() throws IOException, ClassNotFoundException, NoPlayListFoundException {

        out.writeObject("tapez le nom de la playlist que vous souhaitez supprimer");

        int test;
        do {
            cmd=(String)in.readObject();
            test=musicHub.getTitlePlaylist().indexOf((String) cmd);
            if ( test!= -1) {
                musicHub.deletePlayList((String) cmd);
                out.writeObject("the playlist has been removed");
            }
            else{out.writeObject("the playlist you are trying to remove doesn't exist");}
        }while(test== -1);

        musicHub.savePlayLists();
    }

    public void remove_albums() throws IOException, ClassNotFoundException,NoAlbumFoundException {

        out.writeObject("tapez le nom de l'album que vous souhaitez supprimer");


        int test;
        do {
            cmd=(String)in.readObject();
            test=musicHub.getTitleAlbum().indexOf((String) cmd);
            if ( test!= -1) {

                Album theAlbum = null;
                boolean result = false;
                for (Album al : musicHub.albums) {
                    if (al.getTitle().toLowerCase().equals(((String) cmd).toLowerCase())) {
                        theAlbum = al;
                        break;
                    }
                }

                if (theAlbum != null)
                    result = musicHub.albums.remove(theAlbum);
                musicHubClient.albums.remove(theAlbum);
                if (!result) throw new NoAlbumFoundException("Album " + ((String) cmd) + " not found!");


                out.writeObject("the album has been removed");
            }
            else{out.writeObject("the album you are trying to remove doesn't exist");}
        }while(test== -1);
        musicHub.saveAlbums();
    }

    public void remove_elements() throws IOException, ClassNotFoundException, NoElementFoundException {

        out.writeObject("tapez le nom du son que vous souhaitez supprimer");


        int test;
        do {
            cmd=(String)in.readObject();
            test=musicHub.getTitleSongs().indexOf((String) cmd);
            if ( test!= -1) {

        AudioElement theElement= null;
        boolean result = false;
        for (AudioElement el : musicHub.elements) {
            if (el.getTitle().toLowerCase().equals(((String)cmd).toLowerCase())) {
                theElement = el;
                break;
            }
        }

        if (theElement != null)
            result = musicHub.elements.remove(theElement);
        musicHubClient.elements.remove(theElement);
        if (!result) throw new NoElementFoundException("song " + ((String)cmd) + " not found!");




        out.writeObject("the song has been removed");
            }
            else{out.writeObject("the song you are trying to remove doesn't exist");}
        }while(test== -1);
        musicHub.saveElements();
    }


    private void printAvailableCommands() throws IOException {
        StringBuffer menu =new StringBuffer();
        menu.append("a: display the album titles \n");
        menu.append("p: display the playlist titles \n");
        menu.append("s: display all songs \n");
        menu.append("+a: add a new album\n");
        menu.append("+p: add a new playlist\n");
        menu.append("+s: add a new song\n");
        menu.append("-a: remove an existing album\n");
        menu.append("-p: remove an existing playlist\n");
        menu.append("-s: remove an existing song\n");
        menu.append("l: refresh database\n");
        menu.append("c: cancel changes\n");
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