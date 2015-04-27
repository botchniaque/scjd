package suncertify.db.file;

/**
 * Represents a row in the database.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class Record {

	/**
	 * Deleted row flag.
	 */
	public static final short DELETED = (short) 0x8000;

	/**
	 * Valid row flag.
	 */
	public static final short VALID = (short) 0x0000;

	/**
	 * Data of the row.
	 */
	String[] data;

	/**
	 * Flag telling whether the row was marked as deleted or is still valid.
	 */
	short flag;

	/**
	 * Constructs a row, and initializes data with meta information.
	 * 
	 * @param meta
	 *            meta information of data.
	 */
	Record(MetaData meta) {
		data = new String[meta.getNumberOfFields()];
	}

	/**
	 * Prints out row's data.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(isDeleted() ? "D" : "+");
		sb.append(" [");
		for (String d : data) {
			sb.append(d);
			sb.append(" ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Tells whether row is marked as deleted.
	 * 
	 * @return true if row was marked as deleted, false if row is still valid.
	 */
	public boolean isDeleted() {
		return flag == DELETED;
	}

	/**
	 * The data of the database row.
	 * 
	 * @return data of the database row.
	 */
	public String[] getData() {
		return data;
	}
}
