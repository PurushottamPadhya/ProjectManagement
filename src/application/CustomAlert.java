package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CustomAlert {

	private static CustomAlert customAlert = new CustomAlert();
	public static CustomAlert getInstance() {
		return customAlert;
	}
	
	public void showAlert(String title, String message) {
		   Alert alert = new Alert(AlertType.ERROR);
		   alert.setTitle(title);
		   alert.setHeaderText(message);
		  
		   alert.showAndWait(); 	
	}
	
}
