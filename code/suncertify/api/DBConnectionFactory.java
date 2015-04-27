package suncertify.api;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import suncertify.impl.DBConnectionImpl;
import suncertify.impl.RemoteDBConnection;

/**
 * Singleton implementation of database connection factory. Returns either a
 * reference to a remote database server looked up in RMI registry, or a
 * reference to a database connection ran in this JVM bypassing all networking.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class DBConnectionFactory {

	private static DBConnectionFactory instance;

	private DBConnectionFactory() {
		// hidden constructor
	}

	/**
	 * Returns a reference to a remote database server connector looked up in
	 * RMI registry located on a given host listening on the given port number.
	 * 
	 * @param host
	 *            name of the host where RMI registry is running.
	 * @param port
	 *            port number which RMI registry listens on.
	 * @return reference to a remote database connection.
	 * @throws RemoteException
	 *             if an error with remote communication occurs.
	 * @throws NotBoundException
	 *             if database connection object is not bound to this RMI
	 *             registry.
	 */
	public DBConnection getRemote(String host, int port)
			throws RemoteException, NotBoundException {
		DBConnection conn = null;
		Registry registry = LocateRegistry.getRegistry(host, port);
		conn = (RemoteDBConnection) registry
				.lookup(RemoteDBConnection.REGISTRY_KEY);
		return conn;
	}

	/**
	 * Returns a reference to local database server connector running in the
	 * same JVM bypassing all networking.
	 * 
	 * @return reference to a local database connection.
	 * @throws RemoteException
	 *             if there is a problem instantiating the connector.
	 */
	public DBConnection getLocal() throws RemoteException {
		return new DBConnectionImpl();
	}

	/**
	 * Instance of database connection factory.
	 * 
	 * @return singleton instance of this class.
	 */
	public static DBConnectionFactory getInstance() {
		if (instance == null) {
			instance = new DBConnectionFactory();
		}
		return instance;
	}
}
