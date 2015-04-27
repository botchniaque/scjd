package suncertify.impl;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import suncertify.api.Contractor;
import suncertify.db.Data;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;

/**
 * Implementation of the public database interface. Calls inner database API,
 * translates objects to/from Contractor beans, locks/unlocks rows before/after
 * modification.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class DBConnectionImpl extends UnicastRemoteObject implements
		RemoteDBConnection {

	/**
	 * Default constructor of this remote object.
	 * 
	 * @throws RemoteException
	 */
	public DBConnectionImpl() throws RemoteException {
		super();
	}

	/**
	 * Generated version of a class.
	 */
	private static final long serialVersionUID = -5037852383806979937L;

	/*
	 * protected access for test purposes
	 */
	protected Data data = Data.getInstance();

	@Override
	public long createRecord(Contractor contractor)
			throws DuplicateKeyException, IOException {
		System.out
				.println(String.format("Creating record %s", "" + contractor));
		return data.createRecord(contractor.toArray());
	}

	@Override
	public void deleteRecord(long recNo) throws RecordNotFoundException,
			SecurityException, IOException {
		System.out.println(String.format("Deleting record %s", recNo));
		long cookie = data.lockRecord(recNo);
		data.deleteRecord(recNo, cookie);
		data.unlock(recNo, cookie);
	}

	@Override
	public List<Contractor> findByCriteria(Contractor criteria,
			boolean exactMatch) throws IOException {
		System.out.println(String
				.format("Searching by criteria %s exact-match=%s", criteria,
						exactMatch));
		long[] contractorIds = data.findByCriteria(criteria.toArray());
		List<Contractor> result = new ArrayList<Contractor>(
				contractorIds.length);
		for (long id : contractorIds) {
			try {
				String[] row = data.readRecord(id);
				if (exactMatch) {
					if (!matchesCriteria(row, criteria.toArray())) {
						// skip row
						continue;
					}
				}
				result.add(new Contractor(row, id));
			} catch (RecordNotFoundException e) {
				// skip record which was not found
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public Contractor readRecord(long recNo) throws RecordNotFoundException,
			IOException {
		System.out.println(String.format("Reading record %s", recNo));
		return new Contractor(data.readRecord(recNo), recNo);
	}

	@Override
	public void updateRecord(long recNo, Contractor contractor)
			throws RecordNotFoundException, SecurityException, IOException {
		System.out.println(String.format("Updating record %s with data %s",
				recNo, contractor));
		long cookie = data.lockRecord(recNo);
		data.updateRecord(recNo, contractor.toArray(), cookie);
		data.unlock(recNo, cookie);
	}

	private boolean matchesCriteria(String[] row, String[] criteria) {
		for (int i = 0; i < row.length; i++) {
			if (criteria[i] != null && !criteria[i].equals(row[i])) {
				return false;
			}
		}
		return true;
	}

}
