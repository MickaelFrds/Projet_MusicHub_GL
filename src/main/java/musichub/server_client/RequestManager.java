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
    /**
     *
     */


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
        Object sTitle,sArtist,sLength,sContent, sGenre;
        out.writeObject("Title :");
        do{
            sTitle = in.readObject();
            if(sTitle==""){out.writeObject("The title should not be empty");}
        }while (sTitle=="");
        out.writeObject("Artiste :");
        do{
            sArtist = in.readObject();
            if(sArtist==""){out.writeObject("The artist should not be empty");}
        }while (sArtist=="");
        out.writeObject("Length :");
        Integer number;
        do{
            sLength = in.readObject();
            try{
                number=(Integer.parseInt((String)sLength));
            }catch (NumberFormatException e){ System.err.println("un nombre entier est attendu");}
            if(sLength=="" || (Integer.parseInt((String)sLength)<1)){out.writeObject("The Length format is not valid");}
        }while (sLength=="" || (Integer.parseInt((String)sLength)<1));
        out.writeObject("Content :");
        do{
            sContent = in.readObject();
            if(sContent==""){out.writeObject("The content should not be empty");}
        }while (sContent=="");
        out.writeObject("Genre : (jazz,classic,hiphop,rock,pop,rap)");
        String type;
        List<String> type2 = new LinkedList<>();
        type2.add("jazz");
        type2.add("classic");
        type2.add("pop");
        type2.add("hiphop");
        type2.add("rock");
        type2.add("rap");
        do{
            sGenre = in.readObject();
            type=(String) sGenre;
            if(!type2.contains(type)){out.writeObject("The genre doesn't match, try again");}
        }while (!type2.contains(type));
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
            }catch (NumberFormatException e){ out.writeObject("un nombre entier est attendu");}
            if(aLength=="" || (Integer.parseInt((String)aLength)<1)){out.writeObject("The Length format is not valid");}
        }while (aLength=="" || (Integer.parseInt((String)aLength)<1));
        out.writeObject("Date : (format : YYYY-DD-MM)");
        String date;
        do{
            aDate = in.readObject();
            date=(String)aDate;
            if(date.length()<10 || date.length()>10 || date.charAt(4)!='-' || date.charAt(7)!='-'){out.writeObject("The date format is not valid, try again");}
        }while (date.length()<10 || date.charAt(4)!='-' || date.charAt(7)!='-');
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