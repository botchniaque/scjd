package suncertify.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import suncertify.api.Contractor;
import suncertify.api.DBConnection;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;

/**
 * Class centralizing the logic of the database calls using a database connector
 * provided. Controller is responsible for updating the displayed UI with proper
 * data.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class UIController {
	private ContractorTableModel model;
	private DBConnection dbConn;

	/**
	 * Creates a controller with a database connector for database calls and
	 * table model for getting the current UI state.
	 * 
	 * @param model
	 *            table model of the displayed table.
	 * @param dbConn
	 *            database connector used for database calls.
	 */
	public UIController(ContractorTableModel model, DBConnection dbConn) {
		this.model = model;
		this.dbConn = dbConn;
	}

	/**
	 * Updates the database row with the ID of the customer who is booking the
	 * row.
	 * 
	 * @param rowIndex
	 *            index of the row being booked.
	 * @param ownerId
	 *            ID of the customer booking the row.
	 * @throws RecordNotFoundException
	 *             if the row being booked cannot be found in the database.
	 * @throws SecurityException
	 *             if there was a problem with locking/unlocking the row.
	 * @throws IOException
	 *             if there is an error connecting to the database.
	 */
	public void bookRow(int rowIndex, String ownerId)
			throws RecordNotFoundException, SecurityException, IOException {
		Contractor row = model.getRow(rowIndex);
		row.setOwner(ownerId);
		updateRow(rowIndex, row);
	}

	/**
	 * Inserts new row into the database.
	 * 
	 * @param newRow
	 *            data of the row to be inserted.
	 * @throws DuplicateKeyException
	 *             if the row with the same id already exists.
	 * @throws IOException
	 *             if there is an error connecting to the database.
	 * @throws RecordNotFoundException
	 *             if the row does not exist right after the insert operation.
	 */
	public void insertRow(Contractor newRow) throws DuplicateKeyException,
			IOException, RecordNotFoundException {
		long id = dbConn.createRecord(newRow);
		// inserting the row may truncate the value
		Contractor insertedRow = dbConn.readRecord(id);
		model.insertRow(insertedRow);
	}

	/**
	 * Deletes the row from the database.
	 * 
	 * @param tableIndex
	 *            index of the row to be deleted.
	 * @throws RecordNotFoundException
	 *             if the row being deleted does not exist.
	 * @throws SecurityException
	 *             if there was a problem with locking/unlocking the row.
	 * @throws IOException
	 *             if there is an error connecting to the database.
	 */
	public void deleteRow(int tableIndex) throws RecordNotFoundException,
			SecurityException, IOException {
		Contractor row = model.getRow(tableIndex);
		dbConn.deleteRecord(row.getId());
		model.deleteRow(tableIndex);

	}

	/**
	 * Updates the database row.
	 * 
	 * @param rowIndex
	 *            index of the row to be updated.
	 * @param newRow
	 *            updated data of the row.
	 * @throws RecordNotFoundException
	 *             if the row to be updated does not exist.
	 * @throws SecurityException
	 *             if there was a problem with locking/unlocking the row.
	 * @throws IOException
	 *             if there is an error connecting to the database.
	 */
	public void updateRow(int rowIndex, Contractor newRow)
			throws RecordNotFoundException, SecurityException, IOException {
		Contractor oldRow = model.getRow(rowIndex);
		dbConn.updateRecord(oldRow.getId(), newRow);
		// updating the row may truncate the value
		Contractor updatedRow = dbConn.readRecord(oldRow.getId());
		model.updateRow(rowIndex, updatedRow);
	}

	/**
	 * Gets data from the database filtered with the criteria given, and updates
	 * the UI table with the returned list.
	 * 
	 * @param criteria
	 *            criteria applied to database call.
	 * @throws IOException
	 *             if there is an error connecting to the database.
	 */
	public void filter(Contractor criteria) throws IOException {
		List<Contractor> newData = new ArrayList<Contractor>();
		newData = dbConn.findByCriteria(criteria, true);
		model.updateRows(newData);
	}
}
