package Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Interface.Contact;

public class ContactTest {
	private Contact contact;
	
	@Before
	public void init(){
		contact=new ContactImpl("Saima");
	}

	@Test
	public void testGetId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
		assertEquals("Saima", contact.getName());
	}

	@Test
	public void testGetNotes() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNotes() {
		fail("Not yet implemented");
	}

}
