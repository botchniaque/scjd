package suncertify.db;

/**
 * Inner database interface exposing create, update, delete find functionalities
 * as well as lock / unlock for data modification.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public interface DBAccess {

	/**
	 * Reads a record from the file. Returns an array where each element is a
	 * record value.
	 * 
	 * @param recNo
	 *            number of record to be read.
	 * @return an array where each element is a record value.
	 * @throws RecordNotFoundException
	 *             when record at position recNo does not exist, or is marked as
	 *             deleted.
	 */
	public String[] readRecord(long recNo) throws RecordNotFoundException;

	/**
	 * Modifies the fields of a record. The new value for field n appears in
	 * data[n]. If data[n] is null then value at this position stays unchanged.
	 * 
	 * @param recNo
	 *            number of the record to be updated.
	 * @param data
	 *            data array to be stored in the database
	 * @param lockCookie
	 *            cookie which row was locked with.
	 * @throws RecordNotFoundException
	 *             if there is no record at recNo position, or is marked as
	 *             deleted.
	 * @throws SecurityException
	 *             if the record is locked with a cookie other than lockCookie.
	 */
	public void updateRecord(long recNo, String[] data, long lockCookie)
			throws RecordNotFoundException, SecurityException;

	/**
	 * Deletes a record, making the record number and associated disk storage
	 * available for reuse.
	 * 
	 * @param recNo
	 *            number of record to be deleted.
	 * @param lockCookie
	 *            cookie which row was locked with.
	 * @throws RecordNotFoundException
	 *             if there is no record at recNo position, or it is already
	 *             marked as deleted.
	 * @throws SecurityException
	 *             if the record is locked with a cookie other than lockCookie.
	 */
	public void deleteRecord(long recNo, long lockCookie)
			throws RecordNotFoundException, SecurityException;

	/**
	 * Returns an array of record numbers that match the specified criteria.
	 * Field n in the database file is described by criteria[n]. A null value in
	 * criteria[n] matches any field value. A non-null value in criteria[n]
	 * matches any field value that begins with criteria[n]. (For example,
	 * "Fred" matches "Fred" or "Freddy".)
	 * 
	 * @param criteria
	 *            match criteria to filter data with.
	 * @return an array of record numbers that match the specified criteria.
	 */
	public long[] findByCriteria(String[] criteria);

	/**
	 * Creates a new record in the database (possibly reusing a deleted entry).
	 * Inserts the given data, and returns the record number of the new record.
	 * 
	 * @param data
	 *            data to fill new record with.
	 * @return record number of the new record.
	 * @throws DuplicateKeyException
	 *             not implemented
	 */
	public long createRecord(String[] data) throws DuplicateKeyException;

	/**
	 * Locks a record so that it can only be updated or deleted by this client.
	 * Returned value is a cookie that must be used when the record is unlocked,
	 * updated, or deleted. If the specified record is already locked by a
	 * different client, the current thread gives up the CPU and consumes no CPU
	 * cycles until the record is unlocked.
	 * 
	 * @param recNo
	 *            number of record to lock
	 * @return cookie that must be used when the record is unlocked, updated, or
	 *         deleted.
	 * @throws RecordNotFoundException
	 *             if there is no record at recNo position, or it is marked as
	 *             deleted.
	 */
	public long lockRecord(long recNo) throws RecordNotFoundException;

	/**
	 * Releases the lock on a record. Cookie must be the cookie returned when
	 * the record was locked; otherwise throws SecurityException.
	 * 
	 * @param recNo
	 *            number of record to unlock
	 * @param cookie
	 *            cookie returned when the record was locked
	 * @throws SecurityException
	 *             if row recNo was locked with different cookie.
	 */
	public void unlock(long recNo, long cookie) throws SecurityException;
}
