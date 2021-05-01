package musichub.Exception;
/**
 * This class handles the exception when no song are found
 */
public class NoElementFoundException extends Exception {

	public NoElementFoundException (String msg) {
		super(msg);
	}
}