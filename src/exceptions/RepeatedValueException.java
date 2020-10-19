package exceptions;

public class RepeatedValueException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5310546637180712410L;

	public RepeatedValueException(String errorMessage) {
        super(errorMessage);
    }
}
