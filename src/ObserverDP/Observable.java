package ObserverDP;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Observable is a class part of Observer-Observable design pattern. Model
 * classes can inherit of it and notify their Observers when there is an update
 * in their data.
 * 
 * An Observable is composed of:
 * <ul>
 * <li>A collection of Observer</li>
 * </ul>
 * 
 * @author H4122
 * @see ObserverDP.Observer
 */
public class Observable {
	private Collection<Observer> obs;

	/**
	 * Default Class constructor. the collection of Observer is created and is empty
	 * at this point
	 */
	public Observable() {
		obs = new ArrayList<Observer>();
	}

	/**
	 * Subscribes an Observer to this Observable by adding it to the collection.
	 * Before hand, checks if this Observer is not already subscribed.
	 * 
	 * @param o An Observer
	 * 
	 */
	public void addObserver(Observer o) {
		if (!obs.contains(o)) {
			obs.add(o);
		}
	}

	/**
	 * Notifies all the Observers subscribed to update their view.
	 * 
	 * @param arg An Object. It usually is a String describing what has changed in
	 *            the Observable's data.
	 *            
	 * @see Observer#update(Observable, Object)
	 */
	public void notifyObservers(Object arg) {
		for (Observer o : obs) {
			o.update(this, arg);
		}
	}

}
