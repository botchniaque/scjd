package suncertify.db.file;

import java.io.Serializable;

/**
 * Class represents description of the database field read from metadata of
 * database file.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 */
public class DbFieldDescription implements Serializable {

	/**
	 * Generated version of the class.
	 */
	private static final long serialVersionUID = 5223567665545802958L;
	private String name;
	private int length;

	/**
	 * Constructs database field descriptor.
	 * 
	 * @param name
	 *            name of the field.
	 * @param length
	 *            length of the field.
	 */
	public DbFieldDescription(String name, int length) {
		this.name = name;
		this.length = length;
	}

	/**
	 * Name of the database field.
	 * 
	 * @return Name of the database field.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name of the field.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Length of the database field.
	 * 
	 * @return Length of the database field.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Sets length of the field.
	 * 
	 * @param length
	 */
	public void setLength(int length) {
		this.length = length;
	}
}
