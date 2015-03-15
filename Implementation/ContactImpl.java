package Implementation;

import Interface.Contact;

public class ContactImpl implements Contact {
private String name;
private String notes="";
private int id;

	public ContactImpl(int id, String name) {
		this.name=name;
		this.id=id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getNotes() {
		return notes;
	}

	@Override
	public void addNotes(String notes) {
		this.notes=notes;
	}

}
