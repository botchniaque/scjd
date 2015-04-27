package suncertify.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import suncertify.api.Contractor;

/**
 * Table model for contractors data retrieved from the database. Table model
 * contains columns representing contractors data as following: "Name",
 * "Location", "Specialities", "Size", "Rate", "Owner".
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class ContractorTableModel extends AbstractTableModel {

	/**
	 * Generated version of a class.
	 */
	private static final long serialVersionUID = -1223801813160213736L;

	/**
	 * Names of the table columns.
	 */
	public static final String[] COLUMNS = { "Name", "Location",
			"Specialities", "Size", "Rate", "Owner" };
	private List<Contractor> data = new ArrayList<Contractor>();

	/**
	 * Returns String for each column.
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 * @param columnIndex
	 *            index of the column.
	 * @return String class for each column.
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	/**
	 * Returns the count of COLUMNS array.
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 * @return size of COLUMNS array.
	 */
	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}

	/**
	 * Return name from the COLUMNS table at the given index.
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 * @param columnIndex
	 *            index of the column.
	 * @return name of the column.
	 */
	@Override
	public String getColumnName(int columnIndex) {
		return COLUMNS[columnIndex];
	}

	/**
	 * Get count of rows of presented data.
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 * @return count of rows in table model.
	 */
	@Override
	public int getRowCount() {
		return data.size();
	}

	/**
	 * Returns a value of the table cell at the specified position.
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 * @param rowIndex
	 *            index of the row to read value from.
	 * @param columnIndex
	 *            index of the column to read value from.
	 * @return value of the cell at the specified position.
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex).toArray()[columnIndex];
	}

	/**
	 * Returns false for each cell in the table.
	 * 
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 * @param rowIndex
	 *            index of the row.
	 * @param columnIndex
	 *            index of the column.
	 * @return false for each cell.
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/**
	 * Gets the table row at the given index.
	 * 
	 * @param rowIndex
	 *            index of the row to be returned.
	 * @return Contractor object for the given index.
	 */
	public Contractor getRow(int rowIndex) {
		return data.get(rowIndex);
	}

	/**
	 * Adds a row to the table.
	 * 
	 * @param row
	 *            data row to be added to the table
	 */
	public void insertRow(Contractor row) {
		data.add(row);
		fireTableRowsInserted(data.size(), data.size());
	}

	/**
	 * Updates a row in the table.
	 * 
	 * @param rowIndex
	 *            index of the row to be updated.
	 * @param updatedRow
	 *            data to update the row with.
	 */
	public void updateRow(int rowIndex, Contractor updatedRow) {
		data.set(rowIndex, updatedRow);
		fireTableRowsUpdated(rowIndex, rowIndex);
	}

	/**
	 * Deletes the row from the table.
	 * 
	 * @param rowIndex
	 *            index of the row to be deleted.
	 */
	public void deleteRow(int rowIndex) {
		data.remove(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
	}

	/**
	 * Updates the whole table with new data.
	 * 
	 * @param newData
	 *            data to update the table with.
	 */
	public void updateRows(List<Contractor> newData) {
		data.clear();
		data.addAll(newData);
		fireTableDataChanged();
	}
}
