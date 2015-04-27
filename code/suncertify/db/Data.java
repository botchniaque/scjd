package suncertify.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import suncertify.common.AppProperties;
import suncertify.db.file.DbFileHandler;
import suncertify.db.file.MetaData;
import suncertify.db.file.Record;

/**
 * Singleton implementation of Database interface. Internally uses FileHandler
 * for reading and writing database file.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class Data implements DBAccess {

	private final DbFileHandler fileHandler = new DbFileHandler(AppProperties
			.get(AppProperties.DB_FILENAME));
	private final Map<Long, Long> lockedRows = new HashMap<Long, Long>();
	private final Lock lock = new ReentrantLock();
	private final Condition lockReleased = lock.newCondition();

	/*
	 * protected access for test purposes
	 */
	protected static Data instance;

	/**
	 * Hidden constructor. Protected access for test purposes.
	 */
	protected Data() {
	}

	/**
	 * Instance of database accessor.
	 * 
	 * @return singleton instance of this class.
	 */
	public static Data getInstance() {
		if (instance == null) {
			instance = new Data();
		}
		return instance;
	}

	@Override
	public long createRecord(String[] data) throws DuplicateKeyException {
		return fileHandler.writeRecord(data);
	}

	@Override
	public void deleteRecord(long recNo, long lockCookie)
			throws RecordNotFoundException, SecurityException {
		checkLock(recNo, lockCookie);
		if (!fileHandler.markAsDeleted(recNo)) {
			throw new RecordNotFoundException("Failed to delete record "
					+ recNo);
		}
	}

	@Override
	public long[] findByCriteria(String[] criteria) {
		List<Long> result = new ArrayList<Long>();
		long i = 0;
		Record r = null;
		// read all
		do {
			r = fileHandler.readRecord(i);
			if (r != null && !r.isDeleted()) {
				boolean matches = true;
				for (int j = 0; j < criteria.length; j++) {
					String crit = criteria[j];
					String value = r.getData()[j];
					if (crit != null && !value.startsWith(crit)) {
						matches = false;
						break;
					}
				}
				if (matches) {
					result.add(i);
				}
			}
			i++;
		} while (r != null);
		return toArray(result);
	}

	/**
	 * Transforms a {@link List}<{@link Long}> to an array of primitive long
	 * values.
	 * 
	 * @param list
	 *            list of {@link Long} values.
	 * @return an array of primitive long values.
	 */
	private long[] toArray(List<Long> list) {
		int size = list.size();
		long[] array = new long[size];
		for (int i = 0; i < size; i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	@Override
	public long lockRecord(long recNo) throws RecordNotFoundException {
		try {
			lock.lock();
			while (lockedRows.containsKey(recNo)) {
				try {
					lockReleased.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// tests if record still exists
			readRecord(recNo);
			final Long cookie = System.nanoTime();
			lockedRows.put(recNo, cookie);
			return cookie;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public String[] readRecord(long recNo) throws RecordNotFoundException {
		Record rec = fileHandler.readRecord(recNo);
		if (rec == null) {
			throw new RecordNotFoundException(
					"Could not find record at position " + recNo);
		}
		if (rec.isDeleted()) {
			throw new RecordNotFoundException("Record " + recNo
					+ " was deleted");
		}
		return rec.getData();
	}

	@Override
	public void unlock(long recNo, long cookie) throws SecurityException {
		checkLock(recNo, cookie);
		try {
			lock.lock();
			lockedRows.remove(recNo);
			lockReleased.signalAll();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void updateRecord(long recNo, String[] updateData, long lockCookie)
			throws RecordNotFoundException, SecurityException {
		// throws security exception
		checkLock(recNo, lockCookie);
		// throws record not found exception
		String[] record = readRecord(recNo);
		for (int i = 0; i < updateData.length; i++) {
			if (updateData[i] != null) {
				record[i] = updateData[i];
			}
		}
		fileHandler.writeRecord(recNo, record);
	}

	/**
	 * Checks whether there is a lock with specific cookie on a specific row.
	 * 
	 * @param recNo
	 *            row whose lock is checked
	 * @param cookie
	 *            with which lock was supposedly acquired
	 * @throws SecurityException
	 *             if there is no lock on rowNo or the row is locked with
	 *             differnet cookie.
	 */
	private void checkLock(long recNo, long cookie) throws SecurityException {
		Long realCookie = lockedRows.get(recNo);
		if (realCookie == null) {
			throw new SecurityException("Record " + recNo + " not locked");
		}
		if (cookie != realCookie) {
			throw new SecurityException("Cookies do not match " + cookie
					+ " != " + realCookie + " for record " + recNo);
		}
	}

	/**
	 * Gets the metadata of the database. Meta data information is read from the
	 * database file.
	 * 
	 * @return {@link MetaData} object with database information.
	 * @throws IOException
	 *             if there was a problem reading database file.
	 */
	public MetaData getMetadata() throws IOException {
		return fileHandler.getMetaData();
	}
}
