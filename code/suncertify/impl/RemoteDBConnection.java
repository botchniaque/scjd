package suncertify.impl;

import java.rmi.Remote;

import suncertify.api.DBConnection;

/**
 * Remote RMI interface of database connection.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public interface RemoteDBConnection extends Remote, DBConnection {

	/**
	 * Key the database connection in registered with in RMI registry.
	 */
	public static final String REGISTRY_KEY = "DBConnection";
}
