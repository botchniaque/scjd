package suncertify.ui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.common.AppProperties;

/**
 * Abstract dialog class displaying connection details form, and gathering users
 * input.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public abstract class AbstractConnectionDetailsDialog extends JDialog implements
		ActionListener, PropertyChangeListener {

	/**
	 * Generated version of a class.
	 */
	private static final long serialVersionUID = 3436054016869664513L;

	/**
	 * Name of the option submitting the dialog.
	 */
	protected static final String OK_OPTION = "Ok";

	/**
	 * Name of the option aborting the dialog.
	 */
	protected static final String EXIT_OPTION = "Exit";

	/**
	 * Label showing the error messages set by input verifiers.
	 */
	protected JLabel errorLabel;

	private boolean isOkOption = false;

	private JOptionPane pane;

	/**
	 * Create a dialog object with a give title. Created dialog is made modal.
	 * It has no parent object set as it's displayed as first in the
	 * application. The dialog in displayed upon creation.
	 * 
	 * @param title
	 *            title of the dialog to be shown.
	 */
	public AbstractConnectionDetailsDialog(String title) {
		super((JFrame) null, title, true);
		initComponents();
	}

	/**
	 * Initiates dialog components.
	 */
	private void initComponents() {
		JLabel infoLabel = new JLabel();
		infoLabel.setText("Enter connection details");
		errorLabel = new JLabel();

		Object[] components = { infoLabel, getFieldsPanel() };
		Object[] options = { OK_OPTION, EXIT_OPTION };
		pane = new JOptionPane(components, JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION, null, options, options[0]);
		setContentPane(pane);

		pane.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, this);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				pane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
	}

	/**
	 * Sets properly styled error message into the error label object.
	 * 
	 * @param message
	 */
	private void setErrorMessage(String message) {
		if (message == null) {
			errorLabel.setText(null);
		} else {
			errorLabel.setText("<html><span style='color : red'>" + message
					+ "</spen></html>");
		}
		this.pack();
	}

	/**
	 * Sets the value of an OK option.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * @param e
	 *            event of the action.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		pane.setValue(OK_OPTION);
	}

	/**
	 * Check whether the dialog was submitted. If yes store the properties
	 * otherwise just hide the dialog.
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 * @param evt
	 *            change event.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (isVisible() && evt.getSource() == pane) {
			Object value = pane.getValue();
			if (value.equals(JOptionPane.UNINITIALIZED_VALUE)) {
				return;
			}
			pane.setValue(JOptionPane.UNINITIALIZED_VALUE);

			if (OK_OPTION.equals(value)) {
				// check if the inputs are valid
				if (errorLabel.getText() == null) {
					setValues();
					AppProperties.store();
					hideAndReset(true);
				}
			} else {
				hideAndReset(false);
			}
		}
	}

	/**
	 * Tells whether OK option was chosen.
	 * 
	 * @return <code>true</code> if OK option was chosen. <code>false</code> if
	 *         EXIT was chosen or dialog was closed.
	 */
	public boolean isOkOption() {
		return isOkOption;
	}

	/**
	 * Hides the dialog and resets the fields. If dialog was aborted (other the
	 * OK option was chosen) return values are reset.
	 * 
	 * @param isOk
	 *            tells whether dialog was confirmed or aborted.
	 */
	protected void hideAndReset(boolean isOk) {
		isOkOption = isOk;
		if (!isOkOption) {
			resetValues();
		}
		resetFields();
		errorLabel.setText(null);
		setVisible(false);
		dispose();
	}

	/**
	 * Creates a panel containing fields for gathering user input. errorLabel
	 * component should be added to the panel to display error messages. This
	 * method needs to be implemented by extending classes.
	 * 
	 * @return {@link JPanel} containing fields for gathering user input.
	 */
	protected abstract JPanel getFieldsPanel();

	/**
	 * Sets the values form the fields to proper value properties. This method
	 * needs to be implemented by extending classes.
	 */
	protected abstract void setValues();

	/**
	 * Resets the value properties to their default values. This method needs to
	 * be implemented by extending classes.
	 */
	protected abstract void resetValues();

	/**
	 * Resets the input fields of the dialog. This method needs to be
	 * implemented by extending classes.
	 */
	protected abstract void resetFields();

	/**
	 * Verifier checking whether field is given a value, and the value is a
	 * number. Verifier sets proper error message if any of the constraints was
	 * violated.
	 * 
	 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
	 * 
	 */
	class NotEmptyNumberVerifier extends NotEmptyVerifier {

		public NotEmptyNumberVerifier(String propertyName) {
			super(propertyName);
		}

		/**
		 * Checks whether verified text is a non empty numer.
		 * 
		 * @see suncertify.ui.dialog.AbstractConnectionDetailsDialog.
		 *      NotEmptyInputVerifier#verifyText(java.lang.String)
		 * 
		 * @return true if input is valid, false otherwise.
		 */
		@Override
		public boolean verifyText(String text) {
			boolean result = super.verifyText(text);
			if (result) {
				try {
					Integer.valueOf(text);
				} catch (NumberFormatException e) {
					setErrorMessage(String.format("'%s' is not a number", text));
					result = false;
				}
			}
			return result;
		}
	}

	/**
	 * Verifier checking whether field is given a value. Verifier sets proper
	 * error message if the constraint was violated.
	 * 
	 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
	 * 
	 */
	class NotEmptyVerifier extends InputVerifier {

		private String propertyName;

		public NotEmptyVerifier(String propertyName) {
			this.propertyName = propertyName;
		}

		/**
		 * Checks whether the component holds a valid value.
		 * 
		 * @see javax.swing.InputVerifier#verify(javax.swing.JComponent)
		 * @param input
		 *            component to be validated.
		 * @return true if the component is valid, false otherwise.
		 */
		public boolean verify(JComponent input) {
			setErrorMessage(null);
			if (input instanceof JTextField) {
				JTextField textField = (JTextField) input;
				String text = textField.getText();
				return verifyText(text);
			}
			return true;
		}

		/**
		 * Verifies an inner text of a {@link JTextField} component.
		 * 
		 * @param text
		 *            text to be verified.
		 * @return <code>true</code> if the input is valid, <code>false</code>
		 *         otherwise.
		 */
		protected boolean verifyText(String text) {
			if (text == null || "".equals(text)) {
				setErrorMessage(String.format("%s field must be set",
						propertyName));
				return false;
			} else {
				return true;
			}
		}
	}

}
