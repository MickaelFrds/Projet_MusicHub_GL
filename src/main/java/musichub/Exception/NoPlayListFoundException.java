package musichub.Exception;
/**
 * This class handles the exception when no PlayList are found
 */
public class NoPlayListFoundException extends Exception {

	public NoPlayListFoundException (String msg) {
		super(msg);
	}
}