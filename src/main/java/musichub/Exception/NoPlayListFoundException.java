package musichub.Exception;

public class NoPlayListFoundException extends Exception {
	/**
	 * This class handles the exception when no PlayList are found
	 * @param msg
	 */
	public NoPlayListFoundException (String msg) {
		super(msg);
	}
}