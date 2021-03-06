package musichub.Server.business;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
/**
 * This class handles the PlayList type
 */
public class PlayList extends Groupsong {

	private ArrayList<UUID> elementUUIDs;

	public PlayList (String title, String id, ArrayList<UUID> elementUUIDs) {
		super(title, id);
		this.elementUUIDs = elementUUIDs;
	}
	
	public PlayList (String title) {
		super(title);
		this.elementUUIDs = new ArrayList<UUID>();
	}
	
	public void addElement (UUID element)
	{
		elementUUIDs.add(element);
	}
	
	public ArrayList<UUID> getElements() {
		return elementUUIDs;
	}
	
	public String getTitle() {
		return title;
	}
	
	public PlayList (Element xmlElement) throws Exception {
		super(xmlElement);
		try {
			
			//parse list of elements:
			Node elementsElement = xmlElement.getElementsByTagName("elements").item(0);
			NodeList elementUUIDNodes = elementsElement.getChildNodes();
			if (elementUUIDNodes == null) return;
		
			this.elementUUIDs = new ArrayList<UUID>();
			
			
			for (int i = 0; i < elementUUIDNodes.getLength(); i++) {
				if (elementUUIDNodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
					Element elementElement = (Element) elementUUIDNodes.item(i);
					if (elementElement.getNodeName().equals("UUID")) 	{
						try {
							this.addElement(UUID.fromString(elementElement.getTextContent()));
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				} 
			}
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	
	public void createXMLElement(Document document, Element parentElement)
	{
		Element playlistElement = document.createElement("playlist");
		parentElement.appendChild(playlistElement);
		
		Element nameElement = document.createElement("title");
        nameElement.appendChild(document.createTextNode(title));
        playlistElement.appendChild(nameElement);
		
		Element UUIDElement = document.createElement("UUID");
        UUIDElement.appendChild(document.createTextNode(uuid.toString()));
        playlistElement.appendChild(UUIDElement);

		
		Element elementsElement = document.createElement("elements");
		for (Iterator<UUID> elementUUIDIter = this.elementUUIDs.listIterator(); elementUUIDIter.hasNext();) {
			
			UUID currentUUID = elementUUIDIter.next();
			
			Element elementUUIDElement = document.createElement("UUID");
			elementUUIDElement.appendChild(document.createTextNode(currentUUID.toString()));
			elementsElement.appendChild(elementUUIDElement);
		}
		playlistElement.appendChild(elementsElement);
	}
	
}