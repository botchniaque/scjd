package suncertify.ui;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Applications error handler displaying an error message in a dialog.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class ErrorHandler {

	/**
	 * Displays an error information in a dialog with a set parent.
	 * 
	 * @param t
	 *            the cause of an error.
	 * @param parent
	 *            parent of the error dialog.
	 */
	public static void handleException(Throwable t, Component parent) {
		String message = t.getLocalizedMessage();
		String title = "Error " + t.getClass().getName();
		JOptionPane.showMessageDialog(parent, message, title,
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Displays an error information with a description in a dialog with a set
	 * parent.
	 * 
	 * @param t
	 *            the cause of an error.
	 * @param description
	 *            additional description of an error.
	 * @param parent
	 *            parent of the error dialog.
	 */
	public static void handleException(Throwable t, String description,
			Component parent) {
		String message = description + "\n" + t.getLocalizedMessage();
		String title = "Error " + t.getClass().getName();
		JOptionPane.showMessageDialog(parent, message, title,
				JOptionPane.ERROR_MESSAGE);
	}
}
