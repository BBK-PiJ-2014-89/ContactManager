package Implementation;

import java.util.Calendar;
import java.util.Set;

import Interface.Contact;
import Interface.PastMeeting;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
	private String notes="";
	public PastMeetingImpl(int id, Calendar calendar, Set<Contact> contacts, String notes) {
		super(id, calendar, contacts);
		this.notes=notes;
	}

	@Override
	public String getNotes() {
		return notes;
	}



}
