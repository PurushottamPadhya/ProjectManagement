package milestone1;

import java.io.Serializable;

public class Company implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6819917000538418971L;
	private String ID;
	private String name;
	private String ABNNumber;
	private String URL;
	private String address;
	
	public Company(String iD, String name, String aBNNumber, String uRL, String address) {
		super();
		ID = iD;
		this.name = name;
		ABNNumber = aBNNumber;
		URL = uRL;
		this.address = address;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getABNNumber() {
		return ABNNumber;
	}

	public void setABNNumber(String aBNNumber) {
		ABNNumber = aBNNumber;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}
