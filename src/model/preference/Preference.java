package model.preference;

import java.io.Serializable;
import java.util.Map;

public class Preference implements Serializable{


	private static final long serialVersionUID = -3475282909793568349L;
	private String studentNumber;
	private Map<String, Integer> preferedProjects;
	
	public Preference(String studentNumber, Map<String, Integer> preferedProjects) {
		super();
		this.studentNumber = studentNumber;
		this.preferedProjects = preferedProjects;
	}
	
	
	public String getStudentNumber() {
		return studentNumber;
	}
	
	
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	
	
	public Map<String, Integer> getPreferedProjects() {
		return preferedProjects;
	}
	
	
	public void setPreferedProjects(Map<String, Integer> preferedProjects) {
		this.preferedProjects = preferedProjects;
	}
	
	
	
}
