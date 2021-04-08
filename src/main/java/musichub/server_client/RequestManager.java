package musichub.server_client;

import musichub.Exception.NoElementFoundException;
import musichub.Exception.NoPlayListFoundException;
import musichub.business.Album;
import musichub.business.MusicHub;
import musichub.business.PlayList;

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
        StringBuffer text =new StringBuffer();
        MusicHub musicHub = new MusicHub();
        String envoi;
        Object recu = in.readObject();
        String cmd =(String)recu;
        while (!((String) cmd).equals("q")){
            switch (cmd)
            {
                case "a" :
                    envoi = musicHub.getTitleAlbum();
                    text.append("Liste des albums :\n" + envoi );
                    out.writeObject(text.toString());
                    break;
                case "p" :
                    envoi = musicHub.getTitlePlaylist();
                    out.writeObject(envoi);
                    break;
                case "s" :
                    envoi = musicHub.getTitleSongs();
                    out.writeObject(envoi);
                    break;
                case "+a":
                    createAlbum();
                    break;
                case "+p":
                    //createPlaylist();
                    break;
                case "+s":

                    break;
                case "h":
                    printAvailableCommands();
                    break;
                default:
                    envoi = "This command doesn't exist \n Please retry !" ;
                    out.writeObject(envoi);
                    break;
            }
            cmd = (String)in.readObject();
        }
    }

    public void createSong(){}

    public void createAlbum() throws IOException, ClassNotFoundException {
        String envoi;
        out.writeObject("Title :");
        Object aTitle = in.readObject();
        out.writeObject("Artiste :");
        Object aArtist = in.readObject();
        out.writeObject("Lenght :");
        Object aLength = in.readObject();
        out.writeObject("Date : (format : YYYY-DD-MM)");
        Object aDate = in.readObject();
        Album a = new Album((String) aTitle,(String) aArtist,Integer.parseInt((String)aLength),(String)aDate);
        envoi = a.getTitle();
        out.writeObject("Album : "+envoi+" has created !");
    }

    public void createPlaylist() throws IOException, ClassNotFoundException, NoElementFoundException, NoPlayListFoundException {
        String envoi;
        MusicHub musicHub = new MusicHub();
        out.writeObject("Type the name of the playlist you wish to create:");
        Object titlePlaylist = in.readObject();
        PlayList pl = new PlayList((String) titlePlaylist);
        Object cmd = "a";
        while (cmd != "n"){
            out.writeObject("Available elements:  ");
            Object titleElement = in.readObject();
            musicHub.addElementToPlayList((String) titleElement,(String) titlePlaylist);
            out.writeObject("Do you want to stop [y/n] :");
            cmd = in.readObject();
        }
        out.writeObject("Playlist "+titlePlaylist+" is created");
    }



    public void load(){

    }

    public void Save(MusicHub musicHub){
        MusicHub musicHubLastVersion = new MusicHub();


    }

    private void printAvailableCommands() throws IOException {
        StringBuffer menu =new StringBuffer();
        menu.append("a: display the album titles \n");
        menu.append("p: display the playlist titles \n");
        menu.append("s: display all songs \n");
        menu.append("+a: add a new song\n");
        menu.append("+p: add a new album\n");
        menu.append("+s: add a new playlist\n");
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