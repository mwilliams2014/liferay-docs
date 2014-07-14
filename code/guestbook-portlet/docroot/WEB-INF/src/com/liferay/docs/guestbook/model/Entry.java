package com.liferay.docs.guestbook.model;

public class Entry {

	private String name;
	private String message;
	private String noun;
	private String person;
	private String place;
	
	public Entry() {

		this.name = null;
		this.message = null;
		this.noun = null;
		this.person = null;
		this.place = null;

	}
	
	public Entry (String name, String message, String noun, String person, String place) {
		
		setName(name);
		setMessage(message);
		setName(noun);
		setMessage(person);
		setName(place);

	}
	
	public String getName() {

		return name;

	}

	public void setName(String name) {

		this.name = name;

	}

	public String getMessage() {

		return message;

	}

	public void setMessage(String message) {

		this.message = message;

	}
	
	public String getNoun() {

		return noun;

	}

	public void setNoun(String noun) {

		this.noun = noun;

	}
	
	public String getPerson() {

		return person;

	}

	public void setPerson(String person) {

		this.person = person;

	}
	
	public String getPlace() {

		return place;

	}

	public void setPlace(String place) {

		this.place = place;

	}
	
}
