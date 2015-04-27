package suncertify.db;

/**
 * Exception signalizes an attempt to modify a row or release a lock by client
 * who either did not obtain a lock or is using wrong lock cookie.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class SecurityException extends Exception {

	/**
	 * Generated version of the class.
	 */
	private static final long serialVersionUID = -4398936334687205935L;

	/**
	 * Default constructor.
	 */
	public SecurityException() {
	}

	/**
	 * Creates an exception instance with a given message.
	 * 
	 * @param message
	 *            message of the exception.
	 */
	public SecurityException(String message) {
		super(message);
	}
}
