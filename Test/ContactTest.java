package Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Implementation.ContactImpl;
import Interface.Contact;

public class ContactTest {
	private Contact contact;
	
	@Before
	public void init(){
		contact=new ContactImpl(1,"Saima");
	}

	@Test
	public void testGetId() {
		assertEquals(1, contact.getId());
	}

	@Test
	public void testGetName() {
		assertEquals("Saima", contact.getName());
	}

	@Test
	public void testGetNotes() {
		assertEquals("", contact.getNotes());
	}

	@Test
	public void testAddNotes() {
		contact.addNotes("new notes");
		
		assertEquals("new notes", contact.getNotes());
	}

}
