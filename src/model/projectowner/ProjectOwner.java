package model.projectowner;

import java.io.Serializable;

public class ProjectOwner implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4252795932235058109L;
	private String firstname; 
	private String surname;
	private String ownerID;
	private String role ;
	private String email;
	private String companyID;
	
	
	public ProjectOwner(String firstname, String surname, String ownerID , String role, String  email, String companyID) {
		super();
		this.firstname = firstname;
		this.surname = surname;
		this.ownerID = ownerID;
		this.role =  role;
		this.email = email;
		this.companyID = companyID;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getCompanyID() {
		return companyID;
	}


	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getSurname() {
		return surname;
	}


	public void setSurname(String surname) {
		this.surname = surname;
	}


	public String getOwnerID() {
		return ownerID;
	}


	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}
	
	
	
	
	
	
}
