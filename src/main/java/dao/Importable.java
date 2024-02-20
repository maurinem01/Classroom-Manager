package dao;

import object.Person;

/**
 * This interface forces DAO objects to imlement a {@link Importable#save} method for
 * {@link transfer} classes to use
 * @author Maurine
 *
 * @param <T>
 */
public interface Importable<T extends Person> {

	/**
	 * Any DAO class that can transfer data from a .csv file to the database must 
	 * implement this method to commit the data to the dataabase.
	 * @param t
	 * @return
	 */
	int save(T t);
	
}
