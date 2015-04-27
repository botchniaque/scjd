package suncertify.db.file;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class represents meta data of database file structure.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 */
public class MetaData implements Serializable {

	/**
	 * Generated version of the class.
	 */
	private static final long serialVersionUID = -880664152299602601L;
	private int magicCookie;
	private int offset;
	private int numberOfFields;
	final private List<DbFieldDescription> fields;

	/**
	 * Default constructor.
	 */
	MetaData() {
		fields = new ArrayList<DbFieldDescription>();
	}

	/**
	 * Magic cookie value identifies this as a data file.
	 * 
	 * @return magic cookie.
	 */
	public int getMagicCookie() {
		return magicCookie;
	}

	/**
	 * Sets magic cookie.
	 * 
	 * @param magicCookie
	 */
	public void setMagicCookie(int magicCookie) {
		this.magicCookie = magicCookie;
	}

	/**
	 * Gets offset to start of record zero.
	 * 
	 * @return Offset to start of record zero.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Sets offset to start of record zero.
	 * 
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Number of fields in each record.
	 * 
	 * @return number of fields in each record.
	 */
	public int getNumberOfFields() {
		return numberOfFields;
	}

	/**
	 * Sets number of fields.
	 * 
	 * @param numberOfFields
	 */
	public void setNumberOfFields(int numberOfFields) {
		this.numberOfFields = numberOfFields;
	}

	/**
	 * List of field descriptions.
	 * 
	 * @return List of field descriptions.
	 */
	public List<DbFieldDescription> getFields() {
		return fields;
	}

	/**
	 * Number of bytes of each row of data.
	 * 
	 * @return Number of bytes of each row of data.
	 */
	public int getRowLength() {
		// 2 byte flag
		int length = 2;
		for (DbFieldDescription df : getFields()) {
			length += df.getLength();
		}
		return length;
	}
}
