package Implementation;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
import org.xml.sax.SAXException;

import Interface.*;


public class ContactManagerImpl implements ContactManager {
private Set<Contact> contactSet;
private Set<Integer> uniqueContactIDSet;
private List<Meeting> meetingList;
private int uniqueMeetingIDSeq;
private ArrayList<Integer> currentIDBeforeFlush=new ArrayList<Integer>();
private final String CONTACTS_FILE_PATH="contacts.xml";
private final String MEETINGS_FILE_PATH="meetings.xml";

	public ContactManagerImpl() {
		this.contactSet=new HashSet<Contact>();
		this.uniqueContactIDSet=new HashSet<Integer>();
		this.meetingList=new ArrayList<Meeting>();
		
		File contactsFile=new File(CONTACTS_FILE_PATH);
		File meetingsFile=new File(MEETINGS_FILE_PATH);

		if(!contactsFile.exists()||!meetingsFile.exists())creatXML();			
		loadContactsXML();
		loadMeetingsXML();
	}
	
	private void loadContactsXML(){
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
					
					uniqueContactIDSet.add(Integer.parseInt(e.getAttribute("id")));
					currentIDBeforeFlush.add(Integer.parseInt(e.getAttribute("id")));
					contactSet.add(c);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	private void loadMeetingsXML(){
		try {
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			Document doc=db.parse(MEETINGS_FILE_PATH);
			doc.getDocumentElement().normalize();
			NodeList nl=doc.getElementsByTagName("uniqueID");
			uniqueMeetingIDSeq=nl.getLength();
			for(int i=0;i<nl.getLength();i++){
				Node node=nl.item(i);
				if(node.getNodeType()==Node.ELEMENT_NODE){
					Element e=(Element)node;
					int id=Integer.parseInt(e.getAttribute("id"));
					int year=Integer.parseInt(e.getElementsByTagName("year").item(0).getTextContent());
					int month=Integer.parseInt(e.getElementsByTagName("month").item(0).getTextContent());
					int day=Integer.parseInt(e.getElementsByTagName("day").item(0).getTextContent());
					int hour=Integer.parseInt(e.getElementsByTagName("hour").item(0).getTextContent());
					int min=Integer.parseInt(e.getElementsByTagName("min").item(0).getTextContent());
					
					boolean flag=true;
					int counter=0;
					int[] contacts = new int[100] ;
					while(flag){
						contacts[counter]=Integer.parseInt(e.getElementsByTagName("contacts").item(counter).getTextContent());
						counter++;
						if(e.getElementsByTagName("contacts").item(counter)==null) flag=false;
					}
					meetingList.add(new MeetingImpl(id, new GregorianCalendar(year, month, day, hour, min), getContacts(contacts)));

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
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
		
		try{
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			Document doc=db.newDocument();
			
			Element meeting=doc.createElement("meeting");
			doc.appendChild(meeting);
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(doc);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StreamResult result = new StreamResult(new File(MEETINGS_FILE_PATH));	 
			transformer.transform(source, result);
		}catch(ParserConfigurationException pce){
			pce.printStackTrace();
		}catch (TransformerException te){
			te.printStackTrace();
		}
	}

	@Override
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		FutureMeeting temp;
		Calendar currentDate=Calendar.getInstance();
		if(currentDate.after(date)) throw new IllegalArgumentException("Meeting date is set in the past");
		if(!contactSet.containsAll(contacts)) throw new IllegalArgumentException("Unknown Contact");		
		temp=new FutureMeetingImpl(uniqueMeetingIDNo(), date, contacts);
		meetingList.add(temp);
		return temp.getId();
	}

	@Override
	public PastMeeting getPastMeeting(int id) {
		if(getFutureMeeting(id)!=null) throw new IllegalArgumentException("The ID for the meeting is in the future");
		for(Meeting m: meetingList){
			if(id==m.getId()){
				return (PastMeeting) m;
			}
		}
		return null;
	}

	@Override
	public FutureMeeting getFutureMeeting(int id) {
		Calendar cal=Calendar.getInstance();
		for(Meeting m: meetingList){
			if(id==m.getId()){
				if(cal.before(m.getDate())){
					return (FutureMeeting) m;
				}
				else throw new IllegalArgumentException("Meeting id is not in the future");
			}
		}
		return null;
	}

	@Override
	public Meeting getMeeting(int id) {
		for(Meeting m:meetingList){
			if(m.getId()==id)
				return m;
		}
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {
		List<Meeting> list=new ArrayList<Meeting>();
		for(Meeting m:meetingList){
			if(m.getContacts().contains(contact)&&getFutureMeeting(m.getId())!=null) {
				list.add(m);
			}
		}
		if(list.isEmpty()){
			throw new IllegalArgumentException("Contact does not exist");
		}
		return list;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {
		List<Meeting> list=new ArrayList<Meeting>();
		for(Meeting m:meetingList){
			if(dateCompare(m.getDate(), date)) {
				list.add(m);
			}
		}
		if (list.size()==0)return null;
		//Collections for sorting the list by time
		Collections.sort(list, new Comparator<Meeting>() {
			@Override
			public int compare(Meeting o1, Meeting o2) {	
				return (o1.getDate()).compareTo((o2.getDate()));
			}	
		});
		return list;
	}
	private boolean dateCompare(Calendar date1, Calendar date2){
		boolean flag = true;
		if(date1.get(Calendar.YEAR )!=date2.get(Calendar.YEAR)) flag=false;
		if(date1.get(Calendar.MONTH )!=date2.get(Calendar.MONTH)) flag=false;
		if(date1.get(Calendar.DAY_OF_MONTH )!=date2.get(Calendar.DAY_OF_MONTH)) flag=false;
		return flag;
	}

	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
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
			Contact temp=new ContactImpl(uniqueContactIDNo(), name, notes);
			contactSet.add(temp);
		}
	}
	private int uniqueContactIDNo(){
		int i=0;
		while(true){
			if(uniqueContactIDSet.contains(i)) {
				i++;
			}
			else {
				uniqueContactIDSet.add(i);
				return i;
			}
		}
	}
	private int uniqueMeetingIDNo(){
		return uniqueMeetingIDSeq++;
	}

	@Override
	public Set<Contact> getContacts(int... ids) {
		Set<Contact> tempSet = new HashSet<Contact>();
		for (int i : ids) {
			if (!uniqueContactIDSet.contains(i)) {
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
		if(name==""||name==null) throw new NullPointerException();
		Set<Contact> tempSet=new HashSet<Contact>();
		for(Contact c:contactSet){
			if(c.getName()==name) tempSet.add(c);
		}
		return tempSet;
	}

	@Override
	public void flush() {
		saveContacts();
		saveMeetings();
	}
	private void saveContacts(){
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
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void saveMeetings(){
		try {
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			Document doc=db.newDocument();
			Element meeting=doc.createElement("meeting");
			doc.appendChild(meeting);
			
			for(Meeting m:meetingList){
				Element uniqueID=doc.createElement("uniqueID");
				meeting.appendChild(uniqueID);
				uniqueID.setAttribute("id", m.getId()+"");
				
				Element date=doc.createElement("date");
				uniqueID.appendChild(date);
				
				Element dateYear=doc.createElement("year");
				dateYear.appendChild(doc.createTextNode(m.getDate().get(Calendar.YEAR)+""));
				date.appendChild(dateYear);
				
				Element dateMonth=doc.createElement("month");
				dateMonth.appendChild(doc.createTextNode(m.getDate().get(Calendar.MONTH)+""));
				date.appendChild(dateMonth);
				
				Element dateDay=doc.createElement("day");
				dateDay.appendChild(doc.createTextNode(m.getDate().get(Calendar.DAY_OF_MONTH)+""));
				date.appendChild(dateDay);
				
				Element dateHour=doc.createElement("hour");
				dateHour.appendChild(doc.createTextNode(m.getDate().get(Calendar.HOUR_OF_DAY)+""));
				date.appendChild(dateHour);
				
				Element dateMin=doc.createElement("min");
				dateMin.appendChild(doc.createTextNode(m.getDate().get(Calendar.MINUTE)+""));
				date.appendChild(dateMin);
				
				for (Contact id:m.getContacts()) {
					Element contacts=doc.createElement("contacts");
					contacts.appendChild(doc.createTextNode(id.getId()+""));
					uniqueID.appendChild(contacts);
				}				
			}
			TransformerFactory tf=TransformerFactory.newInstance();
			Transformer t=tf.newTransformer();
			DOMSource source=new DOMSource(doc);
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StreamResult result=new StreamResult(new File(MEETINGS_FILE_PATH));
			t.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

}
