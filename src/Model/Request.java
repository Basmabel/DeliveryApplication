package Model;

/**
 * Request is a pair of stops :
 * <ul>
 * <li>A pickup stop.</li>
 * <li>A delivery stop.</li>
 * </ul>
 * 
 * From several requests, a tour can be computed respecting the constraint that 
 * a pickup stop must be visited by the deliverer before its corresponding delivery stop.
 * 
 * Requests are created when loading requests by parsing an XML requests file.
 * 
 * @author H4122
 *
 * @see Model.Stop
 * @see Model.Tour
 * @see Controller.Controller#parseRequests(org.w3c.dom.Document)
 */
public class Request {
	private Stop pickupStop;
	private Stop deliveryStop;

	/**
	 * Default class constructor. Attributes are not initialized.
	 */
	public Request() {
		super();
	}

	/**
	 * Class constructor. Creates the request with the stops given in parameters.
	 * 
	 * @param pickupStop The pickup stop of the request.
	 * @param deliveryStop The delivery stop of the request.
	 * 
	 * @see Model.Stop
	 */
	public Request(Stop pickupStop, Stop deliveryStop) {
		this.pickupStop = pickupStop;
		this.deliveryStop = deliveryStop;
	}

	/**
	 * @return The pickup stop of the request.
	 */
	public Stop getPickupStop() {
		return pickupStop;
	}

	/**
	 * @param pickupStop The new pickup stop of the request.
	 */
	public void setPickupStop(Stop pickupStop) {
		this.pickupStop = pickupStop;
	}

	/**
	 * @return The delivery stop of the request.
	 */
	public Stop getDeliveryStop() {
		return deliveryStop;
	}

	/**
	 * @param deliveryStop The new delivery stop of the request.
	 */
	public void setDeliveryStop(Stop deliveryStop) {
		this.deliveryStop = deliveryStop;
	}

	/**
	 * Overrides toString method in class Object.
	 * 
	 * @return A String with the information of this request, that can be printed.
	 * 
	 * @see Model.Stop#toString()
	 */
	@Override
	public String toString() {
		return "Request [pickupStop=" + pickupStop + ", deliveryStop=" + deliveryStop + "]";
	}
	
	/**
	 * Overrides equals method in Object class. 
	 * Compares the object in parameter to this request.
	 * The specified object is identical to this request if it is a request, 
	 * and if both pickup stops and both delivery stops of the specified object and this request are identical.
	 * 
	 * @param obj The object to be compared with.
	 * 
	 * @return TRUE if the specified object and this request are identical, FALSE otherwise.
	 * 
	 * @see Model.Stop#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Request other = (Request) obj;
		return (pickupStop.equals(other.getPickupStop()) && deliveryStop.equals(other.getDeliveryStop()));

	}

}
