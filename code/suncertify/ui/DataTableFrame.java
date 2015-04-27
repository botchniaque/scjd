package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import suncertify.api.Contractor;
import suncertify.api.DBConnection;

/**
 * Main frame of the graphic user interface. It consists of the menu bar, data
 * table, and control panel with data manipulation and filtering actions.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class DataTableFrame extends JFrame {

	/**
	 * Generated version of a class.
	 */
	private static final long serialVersionUID = 6397380497625427048L;
	private ContractorTableModel model;
	private UIController controller;
	private JTable table;
	private JTextField filterNameField;
	private JTextField filterLocationField;

	/**
	 * Creates a frame object with the proper database connector.
	 * 
	 * @param connection
	 *            database connector.
	 */
	public DataTableFrame(DBConnection connection) {
		super("Bodgitt and Scarper, LLC");
		model = new ContractorTableModel();
		controller = new UIController(model, connection);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Creates and displays frames content.
	 */
	public void createAndShowGui() {
		Action clearFilterAction = new ClearFilterAction();
		Action bookRowAction = new BookRowAction();
		Action filterAction = new FilterAction();

		table = new JTable(model);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane(table);

		filterLocationField = new JTextField();
		filterNameField = new JTextField();

		JButton filterButton = new JButton(filterAction);
		JButton clearButton = new JButton(clearFilterAction);

		JLabel nameLabel = new JLabel(ContractorTableModel.COLUMNS[0]);
		JLabel locationLabel = new JLabel(ContractorTableModel.COLUMNS[1]);
		JButton bookButton = new JButton(bookRowAction);

		JPanel filterPanel = new JPanel();
		filterPanel.setBorder(BorderFactory.createTitledBorder("Search"));
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		filterPanel.setLayout(layout);
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(3, 3, 3, 3);
		layout.addLayoutComponent(nameLabel, c);
		filterPanel.add(nameLabel);
		c.gridx++;
		layout.addLayoutComponent(locationLabel, c);
		filterPanel.add(locationLabel);
		c.gridx--;
		c.gridy++;
		c.fill = GridBagConstraints.HORIZONTAL;
		layout.addLayoutComponent(filterNameField, c);
		filterPanel.add(filterNameField);
		c.gridx++;
		layout.addLayoutComponent(filterLocationField, c);
		filterPanel.add(filterLocationField);
		c.gridx--;
		c.gridy++;
		layout.addLayoutComponent(filterButton, c);
		filterPanel.add(filterButton);
		c.gridx++;
		layout.addLayoutComponent(clearButton, c);
		filterPanel.add(clearButton);

		JPanel controlPanel = new JPanel();
		GridBagLayout l1 = new GridBagLayout();
		GridBagConstraints c1 = new GridBagConstraints();
		c1.insets = new Insets(3, 3, 3, 3);
		c1.gridy = 0;
		c1.weighty = 0;
		c1.fill = GridBagConstraints.BOTH;
		controlPanel.setLayout(l1);
		controlPanel.add(filterPanel);
		l1.addLayoutComponent(filterPanel, c1);

		JPanel bookPanel = new JPanel();
		bookPanel.setBorder(new TitledBorder("Book"));
		bookPanel.add(bookButton);
		controlPanel.add(bookPanel);
		c1.gridy++;
		l1.addLayoutComponent(bookPanel, c1);

		Component filler = Box.createVerticalStrut(1);
		controlPanel.add(filler);
		c1.weighty = 1;
		c1.gridy++;
		l1.addLayoutComponent(filler, c1);

		JMenuBar menu = new JMenuBar();
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(new JMenuItem(new ShowHtmlFrameAction(
				"about.html", "About", KeyEvent.VK_O)));
		helpMenu.add(new JMenuItem(new ShowHtmlFrameAction(
				"userguide.html", "Help", KeyEvent.VK_H)));
		helpMenu.setMnemonic(KeyEvent.VK_H);
		JMenu actionsMenu = new JMenu("Actions");
		actionsMenu.setMnemonic(KeyEvent.VK_T);
		actionsMenu.add(new JMenuItem(bookRowAction));
		actionsMenu.add(new JMenuItem(clearFilterAction));
		menu.add(actionsMenu);
		menu.add(helpMenu);

		getContentPane().add(scroll, BorderLayout.CENTER);
		getContentPane().add(controlPanel, BorderLayout.EAST);
		getContentPane().add(menu, BorderLayout.NORTH);

		// show all db items
		clearFilterAction.actionPerformed(null);
		pack();
		setVisible(true);
	}

	/**
	 * UI action booking a row in the contractors table.
	 * 
	 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
	 * 
	 */
	private class BookRowAction extends AbstractAction {

		/**
		 * Generated version of a class.
		 */
		private static final long serialVersionUID = -2270641505998134749L;

		/**
		 * Default constructor.
		 */
		public BookRowAction() {
			super("Book selected row");
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_B));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			final int rowIndex = table.getSelectedRow();
			if (rowIndex != -1) {
				String currentValue = model.getRow(rowIndex).getOwner();
				JTextField textField = new JTextField(currentValue);
				textField.selectAll();
				Object[] options = { "Book", "Cancel" };
				Object[] message = { "Enter owner ID", textField };
				int option = JOptionPane
						.showOptionDialog(table, message, "Book contractor",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[0]);
				if (option == JOptionPane.OK_OPTION) {
					String ownerId = textField.getText();
					try {
						controller.bookRow(rowIndex, ownerId);
					} catch (Exception e1) {
						e1.printStackTrace();
						ErrorHandler.handleException(e1,
								"Failed to book the row", table);
					}
				}
			}

		}

	}

	/**
	 * UI action clearing table filtering.
	 * 
	 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
	 * 
	 */
	private class ClearFilterAction extends FilterAction {

		/**
		 * Generated version of a class.
		 */
		private static final long serialVersionUID = -8055690044575976185L;

		/**
		 * Default constructor.
		 */
		public ClearFilterAction() {
			super("Show all", new Integer(KeyEvent.VK_A));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			filterLocationField.setText(null);
			filterNameField.setText(null);
		}

		@Override
		protected Contractor getCriteria() {
			return new Contractor();
		}

	}

	/**
	 * UI action filtering table data with the given criteria.
	 * 
	 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
	 * 
	 */
	private class FilterAction extends AbstractAction {

		/**
		 * Generated version of a class.
		 */
		private static final long serialVersionUID = -2841435338961556149L;

		/**
		 * Default constructor.
		 */
		public FilterAction() {
			this("Search", new Integer(KeyEvent.VK_S));
		}

		/**
		 * Creates filter action with the actions name and the mnemonic.
		 * 
		 * @param name
		 *            name of the action.
		 * @param mnemonic
		 *            mnemonic for the action.
		 */
		public FilterAction(String name, Integer mnemonic) {
			super(name);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				controller.filter(getCriteria());
			} catch (IOException e1) {
				e1.printStackTrace();
				ErrorHandler.handleException(e1, "Failed to filter the data",
						DataTableFrame.this);
			}
		}

		/**
		 * Filtering criteria input by the user.
		 * 
		 * @return filtering criteria input by the user.
		 */
		protected Contractor getCriteria() {
			Contractor criteria = new Contractor();
			criteria.setName(getContents(filterNameField.getText()));
			criteria.setLocation(getContents(filterLocationField.getText()));
			return criteria;
		}

		private String getContents(String value) {
			if (value != null) {
				value = value.trim();
			}
			return "".equals(value) ? null : value;
		}

	}

	/**
	 * UI action displays content of a html file in a separate frame.
	 * 
	 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
	 * 
	 */
	private class ShowHtmlFrameAction extends AbstractAction {

		/**
		 * Generated version of a class.
		 */
		private static final long serialVersionUID = 6034607772515697408L;

		private String htmlFile;
		private String title;

		/**
		 * Create an action with a given mnemonic, displaying a given html file
		 * in the frame with a given title.
		 * 
		 * @param htmlFile
		 *            name of the file to be displayed in the frame.
		 * @param title
		 *            new frames title.
		 * @param mnemonic
		 *            mnenonic assigned to the action.
		 */
		public ShowHtmlFrameAction(String htmlFile, String title,
				Integer mnemonic) {
			super(title);
			this.htmlFile = htmlFile;
			this.title = title;
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JEditorPane editorPane = new JEditorPane();
			editorPane.setContentType("text/html");
			editorPane.setEditable(false);
			try {
				InputStream is = this.getClass().getClassLoader().getResourceAsStream(htmlFile);
				if (is == null) {
					throw new FileNotFoundException("File " + htmlFile
							+ " does not exist");
				}
				//read a file to a string
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[1024];
				do {
					baos.write(buffer, 0, bytesRead);
					bytesRead = is.read(buffer);
				} while (bytesRead != -1);
				editorPane.setText(new String(baos.toByteArray(), "UTF-8"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				ErrorHandler.handleException(e1, "Failed to read help file",
						DataTableFrame.this);
				return;
			} catch (IOException e1) {
				e1.printStackTrace();
				ErrorHandler.handleException(e1, "Failed to read help file",
						DataTableFrame.this);
				return;
			}

			final JFrame frame = new JFrame(title);
			frame.setLocationRelativeTo(DataTableFrame.this);
			frame.getContentPane().add(new JScrollPane(editorPane));
			frame.pack();
			frame.setVisible(true);
		}

	}
}
