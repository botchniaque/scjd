package suncertify;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;

import suncertify.api.DBConnection;
import suncertify.api.DBConnectionFactory;
import suncertify.common.AppProperties;
import suncertify.impl.DBConnectionImpl;
import suncertify.impl.RemoteDBConnection;
import suncertify.ui.DataTableFrame;
import suncertify.ui.ErrorHandler;
import suncertify.ui.dialog.ClientConnectionDialog;
import suncertify.ui.dialog.ServerDetailsDialog;

/**
 * Main class of the database server/clinet. Depending on the arguments used the
 * application can be ran as server, as client or in stand-alone mode database
 * as a local file based database.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class Main {

	private final static String MODE_SERVER = "server";
	private final static String MODE_ALONE = "alone";

	/**
	 * Starts the application in one of three modes: <li><b>(no arguments)</b> -
	 * client mode</li> <li><b>server</b> - server mode</li> <li><b>alone</b> -
	 * stand alone mode</li> if more than one argument, or an argument with name
	 * other than allowed is passed then the usage instructions are printed.
	 * 
	 * @param args
	 *            application arguments. No argument or one of 'server' or
	 *            'alone' is allowed.
	 */
	public static void main(String[] args) {
		AppProperties.load();
		try {
			final DBConnection conn;
			if (args.length == 0) {
				System.out.println("Starting GUI client");
				ClientConnectionDialog dialog = new ClientConnectionDialog();
				if (dialog.isOkOption()) {
					conn = DBConnectionFactory.getInstance().getRemote(
							dialog.getHost(), dialog.getPort());
					startGuiClient(conn);
				}
			} else if (args.length == 1) {
				if (args[0].equals(MODE_ALONE)) {
					System.out
							.println("Starting GUI client in stand-alone mode");
					conn = DBConnectionFactory.getInstance().getLocal();
					startGuiClient(conn);
				} else if (args[0].equals(MODE_SERVER)) {
					System.out.println("Starting DB server");
					ServerDetailsDialog dialog = new ServerDetailsDialog();
					if (dialog.isOkOption()) {
						startDbServer(dialog.getPort());
					}
				} else {
					printUsage();
				}
			} else {
				printUsage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ErrorHandler.handleException(e, null);
		}
	}

	private static void printUsage() {
		System.out.println("Usage: java -jar <path_and_filename> [<mode>]");
		System.out.println("<mode>\tserver: networked mode (default)");
		System.out.println("\talone: standalone mode");
	}

	private static void startDbServer(int port) {
		try {
			System.out.println("Starting DB server on port " + port);
			RemoteDBConnection dbConn = new DBConnectionImpl();
			Registry registry = LocateRegistry.createRegistry(port);
			registry.rebind(RemoteDBConnection.REGISTRY_KEY, dbConn);
			System.out.println("Started DB server on port " + port);
			Object[] options = { "Stop DB server" };
			JOptionPane.showOptionDialog(null,
					"Server started on port " + port, "DB Server",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			registry.unbind(RemoteDBConnection.REGISTRY_KEY);
			registry = null;
		} catch (Exception e) {
			e.printStackTrace();
			ErrorHandler.handleException(e, "failed to start DB server", null);
		} finally {
			// make sure that all threads of the application finish.
			System.exit(0);
		}
	}

	private static void startGuiClient(DBConnection conn) {
		DataTableFrame tFrame = new DataTableFrame(conn);
		tFrame.createAndShowGui();
	}

}
