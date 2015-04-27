package suncertify.ui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.common.AppProperties;

/**
 * Implementation class of a server startup dialog containing a 'port' input
 * field telling on which port the server is going to be started on. Upon the
 * dialog submit the application properties are updated with the field value.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class ServerDetailsDialog extends AbstractConnectionDetailsDialog {

	/**
	 * Generated version of a class.
	 */
	private static final long serialVersionUID = 4004645728976776194L;

	private JLabel portLabel;
	private JTextField portField;

	private int port;

	/**
	 * Default constructor.
	 */
	public ServerDetailsDialog() {
		super("Server startup");
	}

	protected JPanel getFieldsPanel() {
		portField = new JTextField();
		portField.setText(AppProperties.get(AppProperties.SERVER_PORT));
		portField.setInputVerifier(new NotEmptyNumberVerifier("Port"));
		portField.addActionListener(this);
		portLabel = new JLabel("Port");

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		JPanel p = new JPanel(layout);
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 3, 3, 3);
		c.weightx = 0;
		layout.addLayoutComponent(portLabel, c);
		p.add(portLabel);
		c.gridx++;
		c.weightx = 1;
		layout.addLayoutComponent(portField, c);
		p.add(portField);
		c.gridx--;
		c.gridy++;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1;
		layout.addLayoutComponent(errorLabel, c);
		p.add(errorLabel);
		return p;
	}

	@Override
	protected void resetFields() {
		portField.setText(null);
	}

	@Override
	protected void resetValues() {
		port = -1;
	}

	protected void setValues() {
		port = Integer.parseInt(portField.getText());
		AppProperties.put(AppProperties.SERVER_PORT, "" + port);
	}

	/**
	 * Port number input by the user.
	 * 
	 * @return port number input by the user.
	 */
	public int getPort() {
		return port;
	}

}
