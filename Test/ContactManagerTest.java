package Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Implementation.ContactManagerImpl;
import Interface.*;

public class ContactManagerTest {
private ContactManager cm;

	@Before
	public void init(){
		cm=new ContactManagerImpl();
	}

	@Test
	public void testAddFutureMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPastMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFutureMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFutureMeetingListContact() {
		fail("Not yet implemented");
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
	public void testGetContactsString() {
		fail("Not yet implemented");
	}

	@Test
	public void testFlush() {
		fail("Not yet implemented");
	}

}
