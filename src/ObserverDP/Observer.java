package ObserverDP;

/**
 * Observer is an interface part of Observer-Observable design pattern. View
 * classes can implement it and subscribe to Model classes that extend
 * Observable. It allows the Views to be notified of any change in data in Model
 * classes.
 * 
 * @author H4122
 * @see ObserverDP.Observable
 */
public interface Observer {

	/**
	 * Method that has to be implemented in every class that uses this interface.
	 * Defines how to react and refresh when the Observable has been updated.
	 * 
	 * @param observed An Observable to which Observer is subscribed.
	 * @param arg      An Object. Usually a String describing what has changed in
	 *                 the Observable's data.
	 * 
	 */
	public void update(Observable observed, Object arg);
}
