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
    List<Album> albums2 = new LinkedList<Album>();
    List<PlayList> playlists2 = new LinkedList<PlayList>();
    List<AudioElement> elements2 = new LinkedList<AudioElement>();
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
                case "a" -> {
                    sendAlbum(musicHub);
                    break;
                }
                case "p" -> {
                    sendPlaylist(musicHub);
                    break;
                }
                case "s" -> {
                    sendSongs(musicHub);
                    break;
                }
                case "+a" -> {
                    createAlbum(musicHub);
                    break;
                }
                case "+p" -> {
                    createPlaylist();
                    break;
                }
                case "+s" -> {
                    createSong();
                    break;
                }
                case "l" -> {
                    loadData(musicHub);
                    break;
                }
                case "h" -> {
                    printAvailableCommands();
                    break;
                }
                default -> {
                    envoi = "This command doesn't exist \n Please retry !";
                    out.writeObject(envoi);
                    break;
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
        elements2=musicHub.elements;
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
        out.writeObject("Song :" + s.getTitle()+ "has created !\nDo you want to update song list?\n yes (y)   no (n)");

        elements2.add(s);
        do{
            cmd=in.readObject();
            switch ((String) cmd) {
                case "y" -> {
                    musicHub.elements = elements2;
                    break;
                }
                case "n" -> {
                    out.writeObject("Don't forget to refresh the DataBase to see changes!\n");
                    break;
                }
                default -> {
                    out.writeObject("This command doesn't exist \n Please retry !");
                    break;
                }
            }
        }while(!(cmd.equals("y")||cmd.equals("n")));
    }

    public void createAlbum(MusicHub musicHub) throws IOException, ClassNotFoundException {
        albums2=musicHub.albums;
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

        albums2.add(a);
        do {
            cmd=in.readObject();
            switch ((String) cmd) {
                case "y" -> {
                    musicHub.albums = albums2;
                    break;
                }
                case "n" -> {
                    out.writeObject("Don't forget to refresh the DataBase to see changes!\n");
                    break;
                }
                default -> {
                    out.writeObject("This command doesn't exist \n Please retry !");
                    break;
                }
            }
        }while(!(cmd.equals("y")||cmd.equals("n")));
    }

    public void createPlaylist() throws IOException, ClassNotFoundException {
        playlists2 = musicHub.playlists;//faire une copie sinon ca modifie les 2
        out.writeObject("Type the name of the playlist you wish to create:");
        Object titlePlaylist = in.readObject();
        PlayList pl = new PlayList((String) titlePlaylist);
        out.writeObject("Playlist " + pl.getTitle() + " is created\nDo you want to update playlist?\n yes (y)   no (n)");

        playlists2.add(pl);
        do {
            cmd = in.readObject();
             switch ((String) cmd) {
                case "y" -> {
                    musicHub.playlists = playlists2;
                    sendPlaylist(musicHub);
                    break;
                }
                case "n" -> {
                    out.writeObject("Don't forget to refresh the DataBase to see changes!\n");
                    playlists2=playlists2;
                    break;
                }
                /*default-> {
                    out.writeObject("This command doesn't exist \n Please retry !");
                    break;
                }*/
            }
         }while(!(cmd.equals("y")||cmd.equals("n")));
    }


    public void loadData(MusicHub musichub){
        musicHub.albums=albums2;
        musicHub.playlists=playlists2;
        musicHub.elements=elements2;

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