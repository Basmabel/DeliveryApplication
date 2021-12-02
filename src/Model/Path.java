package Model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Path is a list of segments that forms a road from a stop to another one.
 * 
 * A path is composed of:
 * <ul>
 * <li>A departure stop.</li>
 * <li>An arrival stop.</li>
 * <li>A list of consecutive segments such as the departure intersection of the first segment 
 * is at the same location as the departure stop of the path and the arrival intersection 
 * is at the same location as the arrival stop of the path.</li>
 * <li>The duration in seconds that is spent by the deliverer to travel the whole path 
 * from departure to arrival.</li>
 * </ul>
 * 
 * Two segments are consecutive if the arrival intersection of the first one is the departure 
 * intersection of the second.
 * 
 * @author H4122
 *
 *@see Model.Intersection
 * @see Model.Stop
 * @see Model.Segment
 */
public class Path {
	private Stop departureStop;
	private Stop arrivalStop;
	private ArrayList<Segment> pathSegments = new ArrayList<Segment>();
	private float pathDuration;
	
	/**
	 * Default class constructor.
	 * Stops are initialized with default Stop constructor.
	 * The list of segments that constitutes the path is empty and path duration is set to zero.
	 * 
	 * @see Model.Stop#Stop()
	 * @see Model.Segment
	 */
	public Path() {
		super();
		departureStop = new Stop();
		arrivalStop = new Stop();
		pathDuration = 0;
	}

	/**
	 * Class constructor. 
	 * Creates this path with the two stops, the list of segment and the duration given in parameters.
	 * @param departureStop The departure stop of the path.
	 * @param arrivalStop The arrival stop of the path.
	 * @param pathSegments The list of consecutive segments that constitutes the path.
	 * @param pathDuration The duration in seconds spent to travel the path.
	 * 
	 * @see Model.Stop
	 * @see Model.Segment
	 */
	public Path(Stop departureStop, Stop arrivalStop, ArrayList<Segment> pathSegments, float pathDuration) {
		super();
		this.departureStop = departureStop;
		this.arrivalStop = arrivalStop;
		this.pathSegments = pathSegments;
		this.pathDuration = pathDuration;
	}
	
	/**
	 * @return The departure stop of the path.
	 * 
	 * @see Model.Stop
	 */
	public Stop getDepartureStop() {
		return departureStop;
	}
	
	/**
	 * @param departureStop The new departure stop of the path.
	 * 
	 * @see Model.Stop
	 */
	public void setDepartureStop(Stop departureStop) {
		this.departureStop = departureStop;
	}
	
	/**
	 * @return The arrival stop of the path.
	 * 
	 * @see Model.Stop
	 */
	public Stop getArrivalStop() {
		return arrivalStop;
	}
	
	/**
	 * @param arrivalStop The new arrival stop of the path.
	 * 
	 * @see Model.Stop
	 */
	public void setArrivalStop(Stop arrivalStop) {
		this.arrivalStop = arrivalStop;
	}
	
	/**
	 * @return The list of consecutive segments that constitutes the path.
	 * 
	 * @see Model.Segment
	 */
	public ArrayList<Segment> getPathSegments() {
		return pathSegments;
	}
	
	/**
	 * @param pathSegments The new list of consecutive segments that constitutes the path.
	 * 
	 * @see Model.Segment
	 */
	public void setPathSegments(ArrayList<Segment> pathSegments) {
		this.pathSegments = pathSegments;
	}
	
	/**
	 * @return The duration in seconds spent to travel the path.
	 */
	public float getPathDuration() {
		return pathDuration;
	}
	
	/**
	 * @param pathDuration The new duration in seconds spent to travel the path.
	 */
	public void setPathDuration(float pathDuration) {
		this.pathDuration = pathDuration;
	}

	/**
	 * Overrides toString method in class Object.
	 * 
	 * @return A String with the information of this path, that can be printed.
	 * 
	 * @see Model.Stop#toString()
	 * @see Model.Segment#toString()
	 */
	@Override
	public String toString() {
		return "Path [departureStop=" + departureStop + ", arrivalStop=" + arrivalStop + ", pathSegments="
				+ pathSegments + ", pathDuration=" + pathDuration + "]";
	}

	/**
	 * Overrides equals method in Object class. 
	 * Compares the object in parameter to this path.
	 * The specified object is identical to this path if it is a path, 
	 * and it has the same departure and arrival stops, same path duration and 
	 * same list of segments as this path.
	 * 
	 * @param obj The object to be compared with.
	 * 
	 * @return TRUE if the specified object and this path are identical, FALSE otherwise.
	 * 
	 * @see Model.Stop#equals(Object)
	 * @see Model.Segment#equals(Object)
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
		Path other = (Path) obj;
		return (Objects.equals(arrivalStop, other.arrivalStop) && Objects.equals(departureStop, other.departureStop)
				&& (Float.floatToIntBits(pathDuration) == Float.floatToIntBits(other.pathDuration))
				&& Objects.equals(pathSegments, other.pathSegments));
	}
	
}
