package Implementation;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import Interface.Contact;
import Interface.Meeting;

public class MeetingImpl implements Meeting {
private int id;
private Calendar calendar=new GregorianCalendar();
private Set<Contact> contacts=new HashSet<Contact>();

	public MeetingImpl(int id, Calendar calender, Set<Contact> contacts) {
		this.id=id;
		this.calendar=calender;
		this.contacts=contacts;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public Calendar getDate() {
		return this.calendar;
	}

	@Override
	public Set<Contact> getContacts() {
		return contacts;
	}


}
