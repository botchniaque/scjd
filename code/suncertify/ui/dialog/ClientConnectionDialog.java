package suncertify.ui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.common.AppProperties;

/**
 * Implementation class of a client connection dialog containing a 'host' and a
 * 'port' input fields. Upon the dialog submit the application properties are
 * updated with fields values.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class ClientConnectionDialog extends AbstractConnectionDetailsDialog {

	/**
	 * Generated version of a class.
	 */
	private static final long serialVersionUID = -2775892559972429235L;

	private JLabel hostLabel;
	private JLabel portLabel;
	private JTextField hostField;
	private JTextField portField;
	private String host;
	private int port;

	/**
	 * Default constructor.
	 */
	public ClientConnectionDialog() {
		super("Server connection");
	}

	protected JPanel getFieldsPanel() {
		hostField = new JTextField();
		hostField.setText(AppProperties.get(AppProperties.HOST));
		hostField.setInputVerifier(new NotEmptyVerifier("Host"));
		hostField.addActionListener(this);
		hostLabel = new JLabel("Host");

		portField = new JTextField();
		portField.setText(AppProperties.get(AppProperties.PORT));
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
		layout.addLayoutComponent(hostLabel, c);
		p.add(hostLabel);
		c.gridx++;
		c.weightx = 1;
		layout.addLayoutComponent(hostField, c);
		p.add(hostField);
		c.gridx--;
		c.gridy++;
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
		hostField.setText(null);
		portField.setText(null);
	}

	@Override
	protected void resetValues() {
		host = null;
		port = -1;
	}

	protected void setValues() {
		port = Integer.parseInt(portField.getText());
		host = hostField.getText();
		AppProperties.put(AppProperties.HOST, host);
		AppProperties.put(AppProperties.PORT, "" + port);
	}

	/**
	 * Host name input by the user.
	 * 
	 * @return host name input by the user.
	 */
	public String getHost() {
		return host;
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
