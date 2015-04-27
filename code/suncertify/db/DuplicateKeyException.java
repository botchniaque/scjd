package suncertify.db;

/**
 * Exception signalizes an attempt to store in the database a row with already
 * existing key .
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class DuplicateKeyException extends Exception {

	/**
	 * Generated version of the class.
	 */
	private static final long serialVersionUID = 4906931810062308615L;

	/**
	 * Default constructor.
	 */
	public DuplicateKeyException() {
	}

	/**
	 * Creates an exception instance with a given message.
	 * 
	 * @param message
	 *            message of the exception.
	 */
	public DuplicateKeyException(String message) {
		super(message);
	}
}
