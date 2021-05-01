package musichub.Exception;
/**
 * This class handles the exception when no albums are found
 */
public class NoAlbumFoundException extends Exception {


	public NoAlbumFoundException (String msg) {
		super(msg);
	}
}