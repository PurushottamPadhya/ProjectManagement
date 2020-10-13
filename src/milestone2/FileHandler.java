package milestone2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import milestone1.Preference;
import milestone1.Project;
import milestone1.ShortlistProject;
import milestone1.Student;
import milestone1.Utility;

public class FileHandler {
	

	
	/*
	 * Write all file from the old txt files to dat file using serialization
	 * */
	
	public void serializeTxFile() {
		this.WriteProjects();
		this.WriteStudents();
		this.WritePreferences();
		this.WriteSelectedProjects();
		this.WriteStudentsInfo();
	}

	@SuppressWarnings("unchecked")
	public void  WriteProjects() {
		
		List<Project> projects = Utility.getProjectDetails(Utility.ProjectFileName);
		
		try  {
			ObjectOutputStream outPutStream = new ObjectOutputStream(new FileOutputStream(Handler.ProjectFileNameSerialize));
			
			outPutStream.writeObject(projects);
			
			
		}
		catch(IOException e) {
			e.getMessage();
		} 
			
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void  WriteStudents() {
		
		List<Student> projects = Utility.getStudentDetails(Utility.StudentFileName);
		
		try  {
			ObjectOutputStream outPutStream = new ObjectOutputStream(new FileOutputStream(Handler.StudentFileNameSerialize));
			
			outPutStream.writeObject(projects);
			
			
		}
		catch(IOException e) {
			e.getMessage();
		} 

		
	}
	

	
	
	@SuppressWarnings("unchecked")
	public void  WriteStudentsInfo() {
		
		List<Student> projects = Utility.getStudentPersonalityDetails(Utility.StudentInfoFileName);
		
		try  {
			ObjectOutputStream outPutStream = new ObjectOutputStream(new FileOutputStream(Handler.StudentInfoFileNameSerialize));
			
			outPutStream.writeObject(projects);
			
			
		}
		catch(IOException e) {
			e.getMessage();
		} 

		
	}
	
	@SuppressWarnings("unchecked")
	public void  WritePreferences() {
		
		List<Preference> preference = Utility.getStudentPreferencesDetails(Utility.PreferenceFileName);
		
		try  {
			ObjectOutputStream outPutStream = new ObjectOutputStream(new FileOutputStream(Handler.PreferenceFileNameSerialize));
			
			outPutStream.writeObject(preference);
			
			
		}
		catch(IOException e) {
			e.getMessage();
		} 

		
	}
	
	@SuppressWarnings("unchecked")
	public void  WriteSelectedProjects() {
		
		List<ShortlistProject> shortList = Utility.getShortListedProjects(Utility.ShortlistFileName);
		
		try  {
			ObjectOutputStream outPutStream = new ObjectOutputStream(new FileOutputStream(Handler.ShortlistFileNameSerialize));
			
			outPutStream.writeObject(shortList);
			
			
		}
		catch(IOException e) {
			e.getMessage();
		} 

		
	}
	
	
}
