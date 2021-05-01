package musichub.business;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.UUID;
/**
 * This class is an abstraction for playlists and albums
 */
public abstract class Groupsong {

    protected String title;
    protected UUID uuid;

    public Groupsong(String title, String id){
        this.title=title;
        this.uuid=UUID.fromString(id);
    }
    public Groupsong(String title){
        this.title=title;
        this.uuid=UUID.randomUUID();
    }
    public Groupsong(Element xmlElement) throws Exception{
        try {
            this.title = xmlElement.getElementsByTagName("title").item(0).getTextContent();

            String uuid = null;
            try {
                uuid = xmlElement.getElementsByTagName("UUID").item(0).getTextContent();
            } catch (Exception ex) {
                System.out.println("Empty UUID, will create a new one");
            }
            if ((uuid == null) || (uuid.isEmpty()))
                this.uuid = UUID.randomUUID();
            else this.uuid = UUID.fromString(uuid);
        }catch (Exception ex) {
            throw ex;
        }
    }
}
