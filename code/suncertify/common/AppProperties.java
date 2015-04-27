package suncertify.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import suncertify.ui.ErrorHandler;

/**
 * Helper class providing access to the functionality of persistent application
 * properties with default values.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class AppProperties {

	/**
	 * Key for database host name client connects to.
	 */
	public static final String HOST = "db.host";

	/**
	 * Key for database port number client connects to.
	 */
	public static final String PORT = "db.port";

	/**
	 * Key for database port number server is ran at.
	 */
	public static final String SERVER_PORT = "db.server.port";

	/**
	 * Key for database file name.
	 */
	public static final String DB_FILENAME = "db.filename";

	private static final String DEFAULT_HOST = "localhost";
	private static final String DEFAULT_PORT = "1099";
	private static final String DEFAULT_DB_FILENAME = "db-2x2.db";
	private static final Properties DEFAULTS = new Properties();
	private static final File PROPERTIES_FILE = new File(
			"suncertify.properties");

	static {
		DEFAULTS.put(HOST, DEFAULT_HOST);
		DEFAULTS.put(PORT, DEFAULT_PORT);
		DEFAULTS.put(SERVER_PORT, DEFAULT_PORT);
		DEFAULTS.put(DB_FILENAME, DEFAULT_DB_FILENAME);
	}

	private static Properties properties = new Properties(DEFAULTS);

	/**
	 * Loads stored properties from the file suncertify.properties. If the file
	 * does not exist, or some properties are missing the default values are
	 * used.
	 */
	public static void load() {
		try {
			properties.clear();
			properties.load(new FileInputStream(PROPERTIES_FILE));
		} catch (FileNotFoundException e) {
			// ignore
		} catch (IOException e) {
			e.printStackTrace();
			ErrorHandler.handleException(e, "Failed to load properties", null);
		}
	}

	/**
	 * Stores the properties to suncertify.properties file. File is created if
	 * it does not exist yet.
	 */
	public static void store() {
		try {
			properties.store(new FileOutputStream(PROPERTIES_FILE), "");
		} catch (Exception e) {
			e.printStackTrace();
			ErrorHandler.handleException(e, "Failed to store properties", null);
		}
	}

	/**
	 * Writes value of a property into the preferences. Current value is
	 * overwritten.
	 * 
	 * @param key
	 *            key of the property to be stored.
	 * @param value
	 *            new value of the property.
	 */
	public static void put(String key, String value) {
		properties.put(key, value);
	}

	/**
	 * Reads value of a property from preferences.
	 * 
	 * @param key
	 *            key of the property to read.
	 * @return value of the property.
	 */
	public static String get(String key) {
		return properties.getProperty(key);
	}

}
