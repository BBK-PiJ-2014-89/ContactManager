package Implementation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Interface.Contact;
import Interface.ContactManager;
import Interface.FutureMeeting;
import Interface.Meeting;
import Interface.PastMeeting;

public class ContactManagerImpl implements ContactManager {
private Set<Contact> contactSet;
private Set<Integer> idSet;

	public ContactManagerImpl() {
		this.contactSet=new HashSet<Contact>();
		this.idSet=new HashSet<Integer>();
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}
}
