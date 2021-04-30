package musichub.Exception;

public class NoAlbumFoundException extends Exception {
	/**
	 * This class handles the exception when no albums are found
	 * @param msg
	 */

	public NoAlbumFoundException (String msg) {
		super(msg);
	}
}