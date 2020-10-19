package model.project;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// adding Serializable 
public class Project implements Serializable{

// default serialVersion ID
	private static final long serialVersionUID = 7541784430846253461L;
	
	private String title;
	private String ID;
	private String description;
	private String ownerID;
	private Map<String, Integer> skillRanking;
	private Map<String, Integer> studentPreference;
	private Map<String, Integer> selectedStudents; // key value stores students preferences
	private Double skillGap = 0.0;
	private Double skillCompetency= 0.0;
	private Double preferencePercentage= 0.0;


	public Project() {
		// default constructor
	}

	public Project(String title, String ID, String description, String ownerID) {
		super();
		this.title = title;
		this.ID = ID;
		this.description = description;
		this.ownerID = ownerID;
		this.skillRanking =  new HashMap<String, Integer>();
		this.setStudentPreference(new HashMap<String, Integer>());
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public Map<String, Integer> getSkillRanking() {
		return skillRanking;
	}


	public void setSkillRanking(Map<String, Integer> skillRanking) {
		this.skillRanking = skillRanking;
	}


	public Map<String, Integer> getStudentPreference() {
		return studentPreference;
	}


	public void setStudentPreference(Map<String, Integer> studentPreference) {
		this.studentPreference = studentPreference;
	}


	public Map<String, Integer> getSelectedStudents() {
		return selectedStudents;
	}


	public void setSelectedStudents(Map<String, Integer> selectedStudents) {
		this.selectedStudents = selectedStudents;
	}
	

	public Double getSkillGap() {
		return skillGap;
	}


	public void setSkillGap(Double skillGap) {
		this.skillGap = skillGap;
	}


	public Double getSkillCompetency() {
		return skillCompetency;
	}


	public void setSkillCompetency(Double skillCompetency) {
		this.skillCompetency = skillCompetency;
	}


	public Double getPreferencePercentage() {
		return preferencePercentage;
	}


	public void setPreferencePercentage(Double preferencePercentage) {
		this.preferencePercentage = preferencePercentage;
	}
	
}
