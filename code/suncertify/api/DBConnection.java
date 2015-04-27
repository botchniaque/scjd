package suncertify.api;

import java.io.IOException;
import java.util.List;

import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;

/**
 * Main database interface exposing create/update/delete/find functionalities.
 * It also exposes access to metadata of the database contained in the database
 * file.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public interface DBConnection {

	/**
	 * Stores a {@link Contractor} entity in the database.
	 * 
	 * @param contractor
	 *            entity to be stored
	 * @return row number of the stored entity.
	 * @throws DuplicateKeyException
	 *             if an attempt was made to store an entity with already
	 *             existing key.
	 * @throws IOException
	 *             if an I/O error occurred while accessing the database.
	 */
	long createRecord(Contractor contractor) throws DuplicateKeyException,
			IOException;

	/**
	 * Reads an entity from the database for a given row number.
	 * 
	 * @param recNo
	 *            row number of an entity to read.
	 * @return entity from the database in recNo position.
	 * @throws RecordNotFoundException
	 *             if the database has no row on position recNo, or that row was
	 *             marked as deleted.
	 * @throws IOException
	 *             if an I/O error occurred while accessing the database.
	 */
	Contractor readRecord(long recNo) throws RecordNotFoundException,
			IOException;

	/**
	 * Updates an entity in the database a the given position with a given data.
	 * Null values in updating record will not change the database values.
	 * 
	 * @param recNo
	 *            row number of an entity to update.
	 * @param record
	 *            data to update row with.
	 * @throws RecordNotFoundException
	 *             if the database has no row on position recNo, or that row was
	 *             marked as deleted.
	 * @throws SecurityException
	 *             if there was a problem with locking the row for update.
	 * @throws IOException
	 *             if an I/O error occurred while accessing the database.
	 */
	void updateRecord(long recNo, Contractor record)
			throws RecordNotFoundException, SecurityException, IOException;

	/**
	 * Deletes an entity at the given position from the database.
	 * 
	 * @param recNo
	 *            row number of an entity to delete.
	 * @throws RecordNotFoundException
	 *             if the database has no row on position recNo, or that row was
	 *             marked as deleted.
	 * @throws SecurityException
	 *             if there was a problem with locking the row for delete.
	 * @throws IOException
	 *             if an I/O error occurred while accessing the database.
	 */
	void deleteRecord(long recNo) throws RecordNotFoundException,
			SecurityException, IOException;

	/**
	 * Finds entities by the given search criteria. Null value in criteria
	 * object matches any text string. There are two search modes possible:
	 * <ul>
	 * <b>exact match</b> - Each field of database entity needs to be either
	 * equal to corresponding criteria field or null to match the criteria;
	 * </ul>
	 * <ul>
	 * <b>contain match</b> Each field of database entity needs to either
	 * contain a value of corresponding criteria field or be null to match the
	 * criteria.
	 * </ul>
	 * 
	 * @param criteria
	 *            search criteria
	 * @param exactMatch
	 *            true if search is to be done in 'exact match' mode. false for
	 *            'contain match' mode.
	 * @return List of entities matching the criteria.
	 * @throws IOException
	 *             if an I/O error occurred while accessing the database.
	 */
	List<Contractor> findByCriteria(Contractor criteria, boolean exactMatch)
			throws IOException;
}
