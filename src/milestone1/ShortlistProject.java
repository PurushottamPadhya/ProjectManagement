package milestone1;

import java.io.Serializable;

public class ShortlistProject implements Serializable{
	
	

	private static final long serialVersionUID = -2977014328968540682L;
	private String projectID;
	private String prefernceSum;
	
	
	public ShortlistProject(String projectID, String prefernceSum) {
		this.projectID = projectID;
		this.prefernceSum = prefernceSum;
	}


	public String getProjectID() {
		return projectID;
	}


	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}


	public String getPrefernceSum() {
		return prefernceSum;
	}


	public void setPrefernceSum(String prefernceSum) {
		this.prefernceSum = prefernceSum;
	}
	
	
	
	
}
    
