package exceptions;

import customAlert.CustomAlert;

public class InvalidSelectionException extends Exception{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Inavlaid selection exception 
	 */

	public InvalidSelectionException(String errorMessage) {
		 super(errorMessage);
		CustomAlert.getInstance().showAlert("Exception", errorMessage);

    }
}
