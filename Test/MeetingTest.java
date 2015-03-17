package Test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import Implementation.ContactImpl;
import Implementation.MeetingImpl;
import Interface.Contact;
import Interface.Meeting;

public class MeetingTest {
private Meeting meeting;
private Set<Contact> contacts;
private Calendar calender;
	@Before
	public void init(){
		contacts=new HashSet<Contact>();
		contacts.add(new ContactImpl(1, "Jim"));
		contacts.add(new ContactImpl(3, "Ali"));
		calender=new GregorianCalendar(2015, 03, 18);
		meeting=new MeetingImpl(0,calender, contacts);
	}
	
	@Test
	public void testGetId() {
		assertEquals(0, meeting.getId());
	}

	@Test
	public void testGetDate() {
		assertEquals(calender, meeting.getDate());
		assertEquals(2015, meeting.getDate().get(Calendar.YEAR));
		assertEquals(3, meeting.getDate().get(Calendar.MONTH));
		assertEquals(18, meeting.getDate().get(Calendar.DAY_OF_MONTH));
	}

	@Test
	public void testGetContacts() {
		assertEquals(contacts, meeting.getContacts());
	}

}
