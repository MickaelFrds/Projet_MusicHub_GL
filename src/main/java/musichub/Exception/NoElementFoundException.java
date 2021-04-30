package musichub.Exception;

public class NoElementFoundException extends Exception {
	/**
	 * This class handles the exception when no song are found
	 * @param msg
	 */
	public NoElementFoundException (String msg) {
		super(msg);
	}
}