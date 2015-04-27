package suncertify.db.file;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Handles low level database file access.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class DbFileHandler {

	private File dbFile;
	private MetaData metadata;

	/**
	 * Constructs the database handler using given database file name. Throws
	 * IllegalArgumentException if the file does not exist.
	 * 
	 * @param fileName
	 */
	public DbFileHandler(String fileName) {
		this.dbFile = new File(fileName);

		if (!dbFile.exists()) {
			throw new IllegalArgumentException("Database file does not exist "
					+ fileName);
		}
	}

	private MetaData readMetaData() throws IOException {
		MetaData meta = new MetaData();
		RandomAccessFile raf = new RandomAccessFile(dbFile, "r");
		try {
			meta.setMagicCookie(raf.readInt());
			meta.setOffset(raf.readInt());
			meta.setNumberOfFields(raf.readUnsignedShort());

			for (int i = 0; i < meta.getNumberOfFields(); i++) {
				int nameLength = raf.readUnsignedShort();
				byte[] bytes = new byte[nameLength];
				raf.readFully(bytes);
				String name = new String(bytes);
				int length = raf.readUnsignedShort();

				DbFieldDescription f = new DbFieldDescription(name, length);
				meta.getFields().add(f);
			}
		} finally {
			if (raf != null) {
				raf.close();
			}
		}

		return meta;
	}

	/**
	 * Gets meta data of database file as specified:
	 * <p/>
	 * Start of file
	 * <ul>
	 * <li>4 byte numeric, magic cookie value identifies this as a data file</li>
	 * <li>4 byte numeric, offset to start of record zero</li>
	 * <li>2 byte numeric, number of fields in each record</li>
	 * </ul>
	 * 
	 * <p/>
	 * Schema description section. Repeated for each field in a record:
	 * <ul>
	 * <li>2 byte numeric, length in bytes of field name</li>
	 * <li>n bytes (defined by previous entry), field name</li>
	 * <li>2 byte numeric, field length in bytes</li>
	 * </ul>
	 * 
	 * File handler caches the metadata once it has read it.
	 * 
	 * @return MetaData object holding information about database file
	 *         structure.
	 * @throws IOException
	 *             when problem accessing file occurs.
	 */
	public MetaData getMetaData() throws IOException {
		if (metadata == null) {
			metadata = readMetaData();
		}
		return metadata;
	}

	/**
	 * Reads the record from the database file.
	 * 
	 * @param num
	 *            number of row to be read form the database file. First row has
	 *            number 0.
	 * @return Record of data at position num in the database file.
	 */
	public Record readRecord(long num) {
		Record r = null;
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(dbFile, "r");
			MetaData meta = getMetaData();
			r = new Record(meta);
			raf.seek(meta.getOffset() + num * meta.getRowLength());
			r.flag = raf.readShort();
			int i = 0;
			for (DbFieldDescription fd : meta.getFields()) {
				r.data[i] = readField(fd.getLength(), raf);
				i++;
			}
		} catch (EOFException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return r;

	}

	private String readField(int fieldSize, RandomAccessFile raf)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fieldSize; i++) {
			char b = (char) raf.read();
			if (b != 0) {
				sb.append(b);
			} else {
				raf.skipBytes(fieldSize - sb.length() - 1);
				break;
			}
		}
		return sb.toString().trim();
	}

	private void writeField(String data, int fieldSize, RandomAccessFile raf)
			throws IOException {
		// if data == null, leave the field unchanged
		if (data == null) {
			raf.skipBytes(fieldSize);
			return;
		}
		final int diff = fieldSize - data.length();

		if (diff <= 0) {
			data = data.substring(0, fieldSize);
		}
		raf.writeBytes(data);
		for (int i = 0; i < diff; i++) {
			raf.write(0);
		}
	}

	/**
	 * Stores given data in the database file at the specified position. Used
	 * for adding new records as well as updating existing ones.
	 * 
	 * @param recordNum
	 *            position at which record it to be written. -1 means that a new
	 *            record needs to be added.
	 * @param data
	 *            data to be stored.
	 * @return position at which record was stored.
	 */
	public long writeRecord(long recordNum, final String[] data) {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(dbFile, "rw");
			MetaData meta = getMetaData();
			if (recordNum == -1) {
				recordNum = getFirstEmptySlot(raf);
			}
			raf.seek(meta.getOffset() + recordNum * meta.getRowLength());

			raf.writeShort(Record.VALID);
			int i = 0;
			for (DbFieldDescription fd : meta.getFields()) {
				writeField(data[i], fd.getLength(), raf);
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (raf != null) {
					raf.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return recordNum;
	}

	/**
	 * Stores given data in the database file as a new row. Call to this method
	 * is equivalent to writeRecord(-1, data);
	 * 
	 * @param data
	 *            data to be stored
	 * @return position at which record was stored.
	 */
	public long writeRecord(final String[] data) {
		return writeRecord(-1, data);
	}

	private long getFirstEmptySlot(RandomAccessFile input) throws IOException {
		long index = 0;
		long offset = getMetaData().getOffset();
		do {
			input.seek(offset);
			short flag = input.readShort();
			if (flag == Record.DELETED) {
				return index;
			} else {
				offset += getMetaData().getRowLength();
				index++;
			}
		} while (offset < input.length());
		return index;
	}

	/**
	 * Marks as deleted row in database file at the given position.
	 * 
	 * @param num
	 *            number of row to be marked as deleted.
	 * @return true if row was marked as deleted. false if the record is already
	 *         marked as deleted or does not exist.
	 */
	public boolean markAsDeleted(long num) {
		RandomAccessFile raf = null;
		try {
			MetaData meta = getMetaData();
			long rowPos = num * meta.getRowLength() + meta.getOffset();
			if (rowPos >= dbFile.length()) {
				System.out.println("markAsDeleted() num exceeds rows count");
				return false;
			}
			raf = new RandomAccessFile(dbFile, "rw");
			raf.seek(rowPos);
			boolean isDeleted = raf.readShort() == Record.DELETED;
			if (isDeleted) {
				return false;
			}
			raf.seek(rowPos);
			raf.writeShort(Record.DELETED);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (raf != null) {
					raf.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
