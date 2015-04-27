package suncertify.db;

/**
 * Exception signalizes that the requested row either does not exist or is
 * marked as deleted.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class RecordNotFoundException extends Exception {

	/**
	 * Generated version of the class.
	 */
	private static final long serialVersionUID = 2823045322464184130L;

	/**
	 * Default constructor.
	 */
	public RecordNotFoundException() {
	}

	/**
	 * Creates an exception instance with a given message.
	 * 
	 * @param message
	 *            message of the exception.
	 */
	public RecordNotFoundException(String message) {
		super(message);
	}
}
