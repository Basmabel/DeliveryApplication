package Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Controller.AddRequestCommand;
import ObserverDP.Observable;

/**
 * Tour implements the interface Observable in order to apply the Observer
 * design pattern. Tour is the class representing a tour.<br/>
 * 
 * A tour is characterized by:
 * <ul>
 * <li>A tour departure which is the stop where the tour begins with a zero
 * seconds stop duration.</li>
 * <li>A departure time which is the time in hh:mm:ss format when the tour
 * begins.</li>
 * <li>An arrival time which is the time in hh:mm:ss format when the tour is
 * finished.</li>
 * <li>A list of tour requests that must be accomplished during the tour.</li>
 * <li>An ordered travel that is a list of stops containing all the paths to
 * follow and the time of the departure from the arrival stop of each path in
 * order to accomplish the tour optimally.</li>
 * <li>A pickup stop that is the pickup stop that will be added to the tour when
 * calling an add request command.</li>
 * <li>A delivery stop that is the delivery stop that will be added to the tour
 * when calling an add request command.</li>
 * <li>A pickup stop predecessor that is the predecessor of the pickup stop that
 * will be added to the tour when calling an add request command.</li>
 * <li>A delivery stop predecessor that is the predecessor of the delivery stop
 * that will be added to the tour when calling an add request command.</li>
 * </ul>
 * 
 * @see ObserverDP.Observable
 * @see AddRequestCommand
 * 
 * @author H4122
 */
public class Tour extends Observable {

	private Stop tourDeparture;
	private Date departureTime;
	private Date arrivalTime;
	private ArrayList<Request> tourRequests;
	private ArrayList<Stop> orderedTravel;
	private Stop pickupStop;
	private Stop deliveryStop;
	private Stop pickupStopPredecessor;
	private Stop deliveryStopPredecessor;

	/**
	 * Default Tour constructor. Calls the constructor of Observable interface it
	 * implements. The tour requests and the order travel are initialized as empty
	 * lists. The tour departure, the pickup stop, the delivery stop, the pickup
	 * stop predecessor and the delivery stop predecessor are initialized using the
	 * default constructor of Stop.
	 * 
	 * @see Observable#Observable()
	 * @see Stop#Stop()
	 */
	public Tour() {
		super();
		this.tourRequests = new ArrayList<>();
		tourDeparture = new Stop();
		this.orderedTravel = new ArrayList<>();
		pickupStop = new Stop();
		deliveryStop = new Stop();
		pickupStopPredecessor = new Stop();
		deliveryStopPredecessor = new Stop();
	}

	/**
	 * Constructor of Tour. Calls the constructor of Observable interface it
	 * implements. When the tour is created, the tour departure, the departure time,
	 * the arrival time and the tour requests are initialized with the corresponding
	 * parameters. The ordered travel is initialized as an empty list. The pickup
	 * stop, the delivery stop, the pickup stop predecessor and the delivery stop
	 * predecessor are initialized using the default constructor of Stop.
	 * 
	 * @param tourDeparture The stop which is the tour departure
	 * @param departureTime The departure time of the tour
	 * @param arrivalTime   The arrival time of the tour
	 * @param tourRequests  The requests list of the tour
	 * 
	 * @see Observable#Observable()
	 * @see Stop#Stop()
	 * @see Tour#tourDeparture
	 * @see Tour#departureTime
	 * @see Tour#arrivalTime
	 * @see Tour#tourRequests
	 */
	public Tour(Stop tourDeparture, Date departureTime, Date arrivalTime, ArrayList<Request> tourRequests) {
		super();
		this.tourDeparture = tourDeparture;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.tourRequests = tourRequests;
		this.orderedTravel = new ArrayList<>();
		pickupStop = new Stop();
		deliveryStop = new Stop();
		pickupStopPredecessor = new Stop();
		deliveryStopPredecessor = new Stop();
	}

	public Stop getPickupStop() {
		return pickupStop;
	}

	public void setPickupStop(Stop pickupStop) {
		this.pickupStop = pickupStop;
	}

	public Stop getDeliveryStop() {
		return deliveryStop;
	}

	public void setDeliveryStop(Stop deliveryStop) {
		this.deliveryStop = deliveryStop;
	}

	public Stop getPickupStopPredecessor() {
		return pickupStopPredecessor;
	}

	public void setPickupStopPredecessor(Stop pickupStopPredecessor) {
		this.pickupStopPredecessor = pickupStopPredecessor;
	}

	public Stop getDeliveryStopPredecessor() {
		return deliveryStopPredecessor;
	}

	public void setDeliveryStopPredecessor(Stop deliveryStopPredecessor) {
		this.deliveryStopPredecessor = deliveryStopPredecessor;
	}

	public Stop getTourDeparture() {
		return tourDeparture;
	}

	public void setTourDeparture(Stop tourDeparture) {
		this.tourDeparture = tourDeparture;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public ArrayList<Request> getTourRequests() {
		return tourRequests;
	}

	public void setTourRequests(ArrayList<Request> tourRequests) {
		this.tourRequests = tourRequests;
	}

	public ArrayList<Stop> getOrderedTravel() {
		return orderedTravel;
	}

	public void setOrderedTravel(ArrayList<Stop> orderedTravel) {
		this.orderedTravel = orderedTravel;
	}

	/**
	 * Returns a Stop, which is the stop that corresponds to the given id, if it
	 * exists in the tour requests, or else to an empty stop.
	 * 
	 * @param id The id of the stop we are looking for
	 * 
	 * @return the stop corresponding to the given id.
	 * 
	 * @see Stop
	 */
	public Stop getStopById(long id) {
		for (Request r : this.tourRequests) {
			if (r.getPickupStop().getId() == id) {
				return r.getPickupStop();
			}
			if (r.getDeliveryStop().getId() == id) {
				return r.getDeliveryStop();
			}
		}
		// return an empty Stop if not Stop matches the id
		return new Stop();
	}

	/**
	 * Notifies all tour's observers when its requests are loaded in order to update the tour's display on the GUI
	 */
	public void endLoadRequests() {
		notifyObservers("loadRequest");
	}

	/**
	 * Notifies all tour's observers when the tour is computed in order to update the tour's display on the GUI
	 */
	public void endRoadCalculing() {
		notifyObservers("RoadCalculing");
	}
	
	/**
	 * Adds a stop in the ordered travel of the tour.
	 * 
	 * @param stop The stop to add to the ordered travel
	 * 
	 * @see Stop
	 */
	public void addInOrderedTravel(Stop stop) {
		this.orderedTravel.add(stop);
	}
	
	/**
	 * Adds a request to the tour requests.
	 * 
	 * @param req The new request to add to the tour requests
	 * @see Request
	 */
	public void addRequest(Request req) {
		tourRequests.add(req);
	}

	/**
	 * Computes the arrival time of each stop from ordered travel, and then computes
	 * the arrival time of the tour.
	 * 
	 */
	public void computeArrivalTimes() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(departureTime);
		for (int i = 1; i < orderedTravel.size(); i++) {
			calendar.add(Calendar.SECOND, (int) orderedTravel.get(i - 1).getNextPath().getPathDuration());
			calendar.add(Calendar.SECOND, (int) orderedTravel.get(i - 1).getStopDuration());
			orderedTravel.get(i).setArrivalTime(calendar.getTime());

		}
		arrivalTime = calendar.getTime();
		endRoadCalculing();
	}

	/**
	 * Returns a String, which is the complete description of the tour that can be
	 * printed.
	 * 
	 * @return the string description of the tour
	 */
	@Override
	public String toString() {
		return "Tour [tourDeparture=" + tourDeparture + ", departureTime=" + departureTime + ", arrivalTime="
				+ arrivalTime + ", tourRequests=" + tourRequests + ", orderedTravel=" + orderedTravel + "]";
	}
}
