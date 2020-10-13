package milestone1;

import java.io.Serializable;
import java.util.Map;

public class Student implements Serializable{
	

	private static final long serialVersionUID = -2443905708763606456L;
	private String studentNumber;
	private Map<String, Integer> grade;
	private String personality;
	private String[] conflictStudent;

		
	public Student(String studentNumber, Map<String, Integer> grade) {
		super();
		this.studentNumber = studentNumber;
		this.grade = grade;
	}
	



	public Student(String studentNumber, Map<String, Integer> grade, String personality, String[] conflictStudent) {
		
		this(studentNumber, grade);
		this.personality = personality;
		this.conflictStudent = conflictStudent;
	}

	public String getPersonality() {
		return personality;
	}





	public void setPersonality(String personality) {
		this.personality = personality;
	}





	public String[] getConflictStudent() {
		return conflictStudent;
	}





	public void setConflictStudent(String[] conflictStudent) {
		this.conflictStudent = conflictStudent;
	}





	public String getStudentNumber() {
		return studentNumber;
	}


	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}


	public Map<String, Integer> getGrade() {
		return grade;
	}


	public void setGrade(Map<String, Integer> grade) {
		this.grade = grade;
	}
	
	
	
	

}
