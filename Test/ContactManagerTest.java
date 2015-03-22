package Test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import Implementation.*;
import Interface.*;

public class ContactManagerTest {
private ContactManager cm;

	@Before
	public void init(){
		cm=new ContactManagerImpl();
		cm.addNewContact("Ali", "");
		cm.addNewContact("Jim", "nil");	
		cm.addNewContact("Saima", "nil");
		cm.addNewContact("Jim", "");
		
		cm.addFutureMeeting(cm.getContacts(2,0), new GregorianCalendar(2015,03,01));
		cm.addFutureMeeting(cm.getContacts("Saima"), new GregorianCalendar(2015, 02,23));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddFutureMeeting_exeption_Date() {
		
		Calendar date=new GregorianCalendar(2014,02,15);
		cm.addFutureMeeting(cm.getContacts(1), date);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddFutureMeeting_exeption_Contacts() {
		
		Calendar date=new GregorianCalendar(2015,3,20);
		cm.addFutureMeeting(cm.getContacts(11), date);
	}
	@Test
	public void testAddFutureMeeting() {
		Calendar date=new GregorianCalendar(2015,3,20);
		assertEquals(2,cm.addFutureMeeting(cm.getContacts(1), date));
	}

	@Test
	public void testGetPastMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFutureMeeting() {
		assertNull(cm.getFutureMeeting(11));
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetFutureMeeting_exeption() {
		cm.getFutureMeeting(11);//TODO : need to add add addNewPastMeeting first 
	}

	@Test
	public void testGetMeeting_Null() {
		assertNull(cm.getMeeting(11));
	}
	@Test
	public void testGetMeeting() {
		//contacts with id 2 and 0 are in meeting with id 0
		assertEquals(cm.getContacts(0,2), cm.getMeeting(0).getContacts());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetFutureMeetingListContact_exeption() {
		cm.getFutureMeetingList(new ContactImpl(33, "Tim"));
	}
	
	@Test
	public void testGetFutureMeetingListContact() {
		List<Meeting> list= cm.getFutureMeetingList(cm.getContacts("Ali").iterator().next());
		assertEquals(0, list.get(0).getId());//Ali is in meeting with id 0
	}

	@Test
	public void testGetFutureMeetingListCalendar() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPastMeetingList() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewPastMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddMeetingNotes() {
		fail("Not yet implemented");
	}

	@Test (expected=NullPointerException.class)
	public void testAddNewContact_nameExeptionHandling() {
		cm.addNewContact(null, "new notes");
	}
	@Test (expected=NullPointerException.class)
	public void testAddNewContact_notesExeptionHandling() {
		cm.addNewContact("Rustam", null);
	}
	@Test
	public void testAddContac_IDNumberTest() {
		cm.addNewContact("Rustam", "");
		cm.addNewContact("Sam", "notes");
		cm.addNewContact("Saima", "notes");
		cm.getContacts(1,0,2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetContactsIntArray_exeptionHandling() {
		cm.addNewContact("Rustam", "");
		cm.addNewContact("Sam", "notes");
		cm.getContacts(1,8,3);
	}

	@Test
	public void testGetContactsIntArray_output(){
		cm.addNewContact("Rustam", "");
		cm.addNewContact("Sam", "notes");
		cm.addNewContact("Saima", "notes");
		cm.addNewContact("Rossie", "notes");
		
		ArrayList<Contact> array=new ArrayList<>();
		for(Contact c:cm.getContacts(3,0,1)){
			array.add(c);
		}
		assertEquals("Rustam", array.get(0).getName());
		assertEquals(3, array.get(2).getId());

	}
	@Test(expected=NullPointerException.class)
	public void testGetContactsString_exeptionHandling() {
		cm.addNewContact("Rustam", "");
		cm.addNewContact("Sam", "notes");
		cm.addNewContact("Saima", "notes");
		cm.addNewContact("Rustam", "notes");
		cm.getContacts("");
	}
	@Test
	public void testGetContactsString() {
		cm.addNewContact("Rustam", "");
		cm.addNewContact("Sam", "notes");
		cm.addNewContact("Saima", "notes");
		cm.addNewContact("Rustam", "notes");
		
		ArrayList<Contact> array=new ArrayList<>();
		for(Contact c:cm.getContacts("Rustam")){
			array.add(c);
		}
		
		assertEquals(0, array.get(0).getId());
		assertEquals(3, array.get(1).getId());
	}


}
