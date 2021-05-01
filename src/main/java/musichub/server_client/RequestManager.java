package musichub.server_client;

import musichub.Exception.ConnectionFailedException;
import musichub.Exception.NoAlbumFoundException;
import musichub.Exception.NoElementFoundException;
import musichub.Exception.NoPlayListFoundException;
import musichub.business.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


public class RequestManager {

    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    MusicHub musicHub = new MusicHub();
    MusicHub musicHubClient = new MusicHub();

    public  List<Album> albums2= new LinkedList<>(musicHub.albums);
    public  List<PlayList> playlists2= new LinkedList<>(musicHub.playlists);
    public  List<AudioElement> elements2= new LinkedList<>(musicHub.elements);
    Object cmd;


    public RequestManager(Socket socket) throws ConnectionFailedException {
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new ConnectionFailedException("Connexion failed.");
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
                        envoi = "";
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
        StringBuilder text =new StringBuilder();
        String TitleAlbums = musicHubClient.getTitleAlbum();
        text.append("Liste des albums :\n").append(TitleAlbums);
        out.writeObject(text.toString());
    }

    public  void sendPlaylist() throws IOException {
           String envoi = musicHubClient.getTitlePlaylist();
           out.writeObject(envoi);
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
        Object aTitle = in.readObject();
        out.writeObject("Artiste :");
        Object aArtist = in.readObject();
        out.writeObject("Lenght :");
        Object aLength = in.readObject();
        out.writeObject("Date : (format : YYYY-DD-MM)");
        Object aDate = in.readObject();
        Album a = new Album((String) aTitle,(String) aArtist,Integer.parseInt((String)aLength),(String)aDate);
        out.writeObject("Album : "+a.getTitle()+" has been created !\n");
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

    public void loadData() throws IOException {
        musicHubClient.playlists.clear();
        musicHubClient.playlists.addAll(musicHub.playlists);
        musicHubClient.albums.clear();
        musicHubClient.albums.addAll(musicHub.albums);
        musicHubClient.elements.clear();
        musicHubClient.elements.addAll(musicHub.elements);
        out.writeObject("Data has refresh");
    }

    public void playSong(){
        try {
            out.writeObject("Quel song voulez vous jouer ? ");
            Object titleSong = in.readObject();
            String chemin = System.getProperty("user.dir") + "\\files\\" + titleSong + ".wav";
            File file=new File(chemin);
            out.writeObject(file);
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
        cmd=in.readObject();
        musicHub.deletePlayList((String)cmd);
        out.writeObject("the playlist has been removed");
        musicHub.savePlayLists();
    }

    public void remove_albums() throws IOException, ClassNotFoundException,NoAlbumFoundException {

        out.writeObject("tapez le nom de l'album que vous souhaitez supprimer");
        cmd=in.readObject();
        musicHub.deleteAlbum((String)cmd);
        out.writeObject("the album has been removed");
        musicHub.saveAlbums();
    }

    public void remove_elements() throws IOException, ClassNotFoundException, NoElementFoundException {
        out.writeObject("tapez le nom du son que vous souhaitez supprimer");
        cmd=in.readObject();
        musicHub.deleteElement((String)cmd);
        out.writeObject("the song has been removed");
        musicHub.saveElements();
    }

    private void printAvailableCommands() throws IOException {
        StringBuffer menu =new StringBuffer();
        menu.append("play: play a song | press 'Stop' to stop");
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
        menu.append("\n");
        out.writeObject(menu);
    }

}