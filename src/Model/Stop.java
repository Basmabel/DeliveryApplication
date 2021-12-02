package Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Stop inherits Intersection and is an intersection with a duration and will be visited by the deliverer. 
 * A stop is either the tour departure, either part of a request. 
 * It can be the pickup stop or the delivery stop of the request.
 * 
 * A stop is composed of:
 * <ul>
 * <li>An id which is the same as the intersection where the stop is located.</li>
 * <li>A latitude.</li>
 * <li>A longitude.</li>
 * <li>The list of all segments that are originated from this intersection.</li>
 * <li>An indication about if the intersection was visited when computing the shortest path
 * (Dijkstra algorithm), updated at each call of Dijkstra.</li>
 * <li>The duration in seconds spent by the deliverer at this location.</li>
 * <li>The time when the deliverer reaches the stop.</li>
 * <li>The path to follow to reach the next stop of the tour.</li>
 * </ul>
 * 
 * Stops are created when loading requests by parsing an XML requests file.<br/>
 * 
 * Stop is the descendant of Intersection class.
 * 
 * @author H4122
 *
 * @see Model.Intersection
 * @see Model.Request
 * @see Model.Segment
 * @see Model.Path
 * @see Model.Map#computeShortestPath(Stop, ArrayList)
 * @see Controller.Controller#parseRequests(org.w3c.dom.Document)
 */
public class Stop extends Intersection{

	private float stopDuration;
	private Date arrivalTime = null;
	private Path nextPath = null;

	/**
	 * Default class constructor. Calls the constructor of its ancestor Intersection.
	 * Id, coordinates and duration are set to zero.
	 * The list of segments originated from this intersection is empty.
	 * The intersection is not visited.
	 * The arrival time and the next path are set to null.
	 * 
	 * @see Model.Intersection#Intersection()
	 */
	public Stop() {
		super();
		this.stopDuration = 0;
	}

	/**
	 * Class constructor.
	 * Creates this stop with the id, the coordinates and the duration in parameters.
	 * The list of segments originated from this intersection is empty.
	 * The intersection is not visited.
	 * The arrival time and the next path are set to null.
	 * 
	 * @param id The id of this intersection.
	 * @param latitude Its latitude on the map.
	 * @param longitude Its longitude on the map.
	 * @param stopDuration The duration in seconds spent at the stop by the deliverer.
	 * 
	 * @see Model.Intersection#Intersection(long, float, float)
	 */
	public Stop(long id, float latitude, float longitude, float stopDuration) {
		super(id, latitude, longitude);
		this.stopDuration=stopDuration;
	}

	/**
	 * @return The duration in seconds spent at the stop by the deliverer.
	 */
	public float getStopDuration() {
		return stopDuration;
	}
	
	/**
	 * @param stopDuration The duration in seconds spent at the stop by the deliverer.
	 */
	public void setStopDuration(float stopDuration) {
		this.stopDuration = stopDuration;
	}
	
	/**
	 * @return The time when the deliverer reaches the stop. If null, the arrival time has not been set.
	 */
	public Date getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * @param arrivalTime The new time when the deliverer reaches the stop.
	 */
	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	/**
	 * @return The path to follow to reach the next stop of the tour. If null, the next path has not been set.
	 */
	public Path getNextPath() {
		return nextPath;
	}

	/**
	 * @param nextPath The new path to follow to reach the next stop of the tour.
	 */
	public void setNextPath(Path nextPath) {
		this.nextPath = nextPath;
	}

	/**
	 * Overrides toString method in class Intersection.
	 * 
	 * @return A String with the information of this stop, that can be printed.
	 * 
	 * @see Model.Intersection#toString()
	 */
	@Override
	public String toString() {
		String seg = "[";
		for(Segment s : this.originatedSegments) {
			seg += "Segment [departureIntersection=" + s.getDepartureIntersection().getId() + ", arrivalIntersection="
					+ s.getArrivalIntersection().getId() + ", length=" + s.getLength() + ", name=" + s.getName() + "], ";
		}
		return "Stop [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + 
				", stopDuration=" + stopDuration + ", originatedSegments=["+ seg + "] ]";
	}
	
	/**
	 * Overrides equals method in Intersection class. 
	 * Compares the object in parameter to this stop.
	 * The specified object is identical to this stop if it is a stop, 
	 * and it has the same id, same coordinates (latitude and longitude),
	 * same list of originated segments and same duration as this stop.
	 * Plus, if both of their arrival times are not null, their value are compared
	 * and if values are the same, stop are identical.
	 * 
	 * @param obj The object to be compared with.
	 * 
	 * @return TRUE if the specified object and this stop are identical, FALSE otherwise.
	 * 
	 * @see Model.Intersection#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		Stop other = (Stop) obj;
		boolean sameArrivalTime = true;
		if ((this.getArrivalTime() != null) && (other.getArrivalTime() != null)) {
			sameArrivalTime = (this.getArrivalTime() == other.getArrivalTime());
		}
		return ((super.equals(obj)) && (stopDuration == other.getStopDuration()) && sameArrivalTime);
	}
}
