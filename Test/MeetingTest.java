package Test;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import Implementation.ContactImpl;
import Interface.Contact;
import Interface.Meeting;

public class MeetingTest {
private Meeting meeting;
	@Before
	public void init(){
		meeting=new MeetingImpl(0,new GregorianCalendar(2015, 03, 18), new ContactImpl(5, "Jim"));
	}
	@Test
	public void testGetId() {
		assertEquals(0, meeting.getId());
	}

	@Test
	public void testGetDate() {
		assertEquals("18/03/2015", meeting.getDate());
	}

	@Test
	public void testGetContacts() {
		for(Contact c:meeting.getContacts()){
			assertEquals("Jim", c.getName());
		}
	}

}
