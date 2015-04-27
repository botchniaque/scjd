package suncertify.api;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Class representing an entity in contractors database.
 * 
 * @author Szymon Bochniak (bochniak.szymon@gmail.com)
 * 
 */
public class Contractor implements Serializable {

	/**
	 * Generated version of this class.
	 */
	private static final long serialVersionUID = -94126319377458466L;
	private static final int NAME = 0;
	private static final int LOC = 1;
	private static final int SPECS = 2;
	private static final int SIZE = 3;
	private static final int RATE = 4;
	private static final int OWNER = 5;

	private String name;
	private String location;
	private String specialities;
	private String size;
	private String rate;
	private String owner;

	private final long id;

	/**
	 * Default constructor. Creates an entity with id -1 meaning that it is not
	 * stored in the database.
	 */
	public Contractor() {
		this.id = -1l;
	}

	/**
	 * Constructor that creates an entity with data given as String array. This
	 * is a convenience method of wrapping database records retrieved from lower
	 * level database API returning records as String[]. Order of elements in
	 * the array will map properties as follows: [name, location, specialities,
	 * size, rate, owner].
	 * 
	 * @param array
	 * @param id
	 */
	public Contractor(String[] array, long id) {
		if (array == null || array.length != 6) {
			throw new IllegalArgumentException(
					"Cannot create Contractor entity from array " + array);
		}
		this.name = array[NAME];
		this.location = array[LOC];
		this.specialities = array[SPECS];
		this.size = array[SIZE];
		this.rate = array[RATE];
		this.owner = array[OWNER];
		this.id = id;
	}

	/**
	 * The name of the subcontractor this record relates to.
	 * 
	 * @return name of the subcontractor.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the subcontractor.
	 * 
	 * @param name
	 *            name of the subcontractor.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The locality in which this contractor works.
	 * 
	 * @return locality in which this contractor works.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the contractors locality.
	 * 
	 * @param location
	 *            locality in which this contractor works.
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Comma separated list of types of work this contractor can perform.
	 * 
	 * @return comma separated list of types of work this contractor can
	 *         perform.
	 */
	public String getSpecialities() {
		return specialities;
	}

	/**
	 * Sets type of work contractor can preform.
	 * 
	 * @param specialities
	 *            comma separated list of types of work this contractor can
	 *            perform.
	 */
	public void setSpecialities(String specialities) {
		this.specialities = specialities;
	}

	/**
	 * The number of workers available when this record is booked.
	 * 
	 * @return The number of workers available.
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Sets the number of workers available.
	 * 
	 * @param size
	 *            number of workers available.
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * Charge per hour for the subcontractor. This field includes the currency
	 * symbol.
	 * 
	 * @return charge per hour for the subcontractor.
	 */
	public String getRate() {
		return rate;
	}

	/**
	 * Sets charge per hour for the subcontractor. This field includes the
	 * currency symbol.
	 * 
	 * @param rate
	 *            charge per hour for the subcontractor including currency
	 *            symbol.
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}

	/**
	 * The id value (an 8 digit number) of the customer who has booked this. If
	 * this field is all blanks, the record is available for sale.
	 * 
	 * @return id value of the customer who has booked this record.
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * The id value (an 8 digit number) of the customer who has booked this
	 * record.
	 * 
	 * @param owner
	 *            id value (an 8 digit number) of the customer.
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * Row number of this entity. Value -1 means that this value is not stored
	 * in the database.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Returns this record as a String array. This is a utility method used when
	 * calling lower level API which takes records as String arrays. Row id is
	 * not included into the array. The elements of this array contain
	 * contractor class properties in the following order: [name, location,
	 * specialities, size, rate, owner].
	 * 
	 * @return record fields as an array.
	 */
	public String[] toArray() {
		String[] array = new String[6];
		array[NAME] = this.name;
		array[LOC] = this.location;
		array[SPECS] = this.specialities;
		array[SIZE] = this.size;
		array[RATE] = this.rate;
		array[OWNER] = this.owner;
		return array;
	}

	/**
	 * Prints readable value of the Contractor record.
	 * 
	 * @return readable value of the Contractor record.
	 */
	@Override
	public String toString() {
		return Arrays.toString(toArray());
	}
}
