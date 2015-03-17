package Implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Interface.*;


public class ContactManagerImpl implements ContactManager {
private Set<Contact> contactSet;
private Set<Integer> idSet;
private ArrayList<Integer> currentIDBeforeFlush=new ArrayList<Integer>();
private final String CONTACTS_FILE_PATH="contacts.xml";

	public ContactManagerImpl() {
		this.contactSet=new HashSet<Contact>();
		this.idSet=new HashSet<Integer>();
		File file=new File(CONTACTS_FILE_PATH);
		
		if(!file.exists())creatXML();			
		
		loadXML();

	}
	private void loadXML(){
		try {
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			Document doc=db.parse(CONTACTS_FILE_PATH);
			
			doc.getDocumentElement().normalize();
			
			NodeList nl=doc.getElementsByTagName("uniqueID");
			
			for(int i=0;i<nl.getLength();i++){
				Node node=nl.item(i);	
				if(node.getNodeType()==Node.ELEMENT_NODE){
					
					Element e=(Element) node;					
					String notes=e.getElementsByTagName("notes").item(0).getTextContent();
					Contact c=new ContactImpl(Integer.parseInt(e.getAttribute("id")), e.getElementsByTagName("name").item(0).getTextContent());
					c.addNotes(notes);
					
					idSet.add(Integer.parseInt(e.getAttribute("id")));
					currentIDBeforeFlush.add(Integer.parseInt(e.getAttribute("id")));
					contactSet.add(c);
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void creatXML(){
		try{
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			Document doc=db.newDocument();
			
			Element contact=doc.createElement("contact");
			doc.appendChild(contact);
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(doc);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StreamResult result = new StreamResult(new File(CONTACTS_FILE_PATH));	 
			transformer.transform(source, result);
		}catch(ParserConfigurationException pce){
			pce.printStackTrace();
		}catch (TransformerException te){
			te.printStackTrace();
		}
	}

	@Override
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PastMeeting getPastMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FutureMeeting getFutureMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting getMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date,
			String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMeetingNotes(int id, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addNewContact(String name, String notes) {
		if(name==null||notes==null){
			throw new NullPointerException();
		}else {
			Contact temp=new ContactImpl(uniqueIDNo(), name);
			temp.addNotes(notes);
			contactSet.add(temp);
		}
	}
	private int uniqueIDNo(){
		int i=0;
		while(true){
			if(idSet.contains(i)) {
				i++;
			}
			else {
				idSet.add(i);
				return i;
			}
		}
	}

	@Override
	public Set<Contact> getContacts(int... ids) {
		Set<Contact> tempSet = new HashSet<Contact>();
		for (int i : ids) {
			if (!idSet.contains(i)) {
				throw new IllegalArgumentException();
			} else {
				for (Contact c : contactSet) {
					if (c.getId() == i)
						tempSet.add(c);
				}
			}
		}
		return tempSet;
	}

	@Override
	public Set<Contact> getContacts(String name) {
		if(name=="") throw new NullPointerException();
		Set<Contact> tempSet=new HashSet<Contact>();
		for(Contact c:contactSet){
			if(c.getName()==name) tempSet.add(c);
		}
		return tempSet;
	}

	@Override
	public void flush() {
		saveConatacts();
	}
	
	private void saveConatacts(){
		try {
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			Document doc=db.parse(CONTACTS_FILE_PATH);
			
			Node contact=doc.getFirstChild();
			boolean flag=false;
			for (Contact c:contactSet){
				for(int i:currentIDBeforeFlush){
					if(i==c.getId()) flag=true;
				}
				if(flag==false){
						Element uniqueID=doc.createElement("uniqueID");
						contact.appendChild(uniqueID);
						uniqueID.setAttribute("id", c.getId()+"");
						
						Element name=doc.createElement("name");
						name.appendChild(doc.createTextNode(c.getName()));
						uniqueID.appendChild(name);
						
						Element notes=doc.createElement("notes");
						notes.appendChild(doc.createTextNode(c.getNotes()));
						uniqueID.appendChild(notes);
						
						currentIDBeforeFlush.add(c.getId());
				}else flag=false;

			}
			
			TransformerFactory tf=TransformerFactory.newInstance();
			Transformer t=tf.newTransformer();
			DOMSource source=new DOMSource(doc);
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StreamResult result=new StreamResult(new File(CONTACTS_FILE_PATH));
			t.transform(source, result);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
