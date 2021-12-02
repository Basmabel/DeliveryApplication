package Model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Visited enumeration indicates if an intersection was visited when computing the shortest path
 * (Dijkstra algorithm).
 * 
 * An intersection can be:
 * <ul>
 * <li>WHITE: the intersection is not visited yet.</li>
 * <li>GREY: the intersection is visited and the shortest path to it is not found yet.</li>
 * <li>BLACK: the intersection was visited and the shortest path to it was found yet.</li>
 * </ul>
 * 
 * @see Intersection#getIntersectionVisited()
 * @see Intersection#setIntersectionVisited(Visited)
 * @see Map#computeShortestPath(Stop, ArrayList)
 */
enum Visited {WHITE, GREY, BLACK};

/**
 * Intersection is the class that defines a location on the map.
 * 
 * An intersection is composed of:
 * <ul>
 * <li>A unique id.</li>
 * <li>A latitude.</li>
 * <li>A longitude.</li>
 * <li>The list of all segments that are originated from this intersection.</li>
 * <li>An indication about if the intersection was visited when computing the shortest path
 * (Dijkstra algorithm), updated at each call of Dijkstra.</li>
 * </ul>
 * 
 * Intersections are created when loading a map by parsing an XML map file.<br/>
 * 
 * Intersection is the ancestor of Stop class.
 * 
 * @author H4122
 * 
 * @see Model.Stop
 * @see Model.Segment
 * @see Model.Map#computeShortestPath(Stop, ArrayList)
 * @see Controller.Controller#parseMap(org.w3c.dom.Document)
 */

public class Intersection {

	protected long id;
	protected float latitude;
	protected float longitude;
	protected ArrayList<Segment> originatedSegments;
	protected Visited intersectionVisited;

	/**
	 * Default Class constructor.
	 * Id and coordinates are set to zero.
	 * The list of segments originated from this intersection is empty.
	 * The intersection is not visited.
	 */
	public Intersection() {
		super();
		this.id = 0;
		this.latitude = 0;
		this.longitude = 0;
		this.originatedSegments = new ArrayList<>();
		this.intersectionVisited = Visited.WHITE;
	}

	/**
	 * Class constructor.
	 * Creates this intersection with the id and the coordinates in parameters.
	 * The list of segments originated from this intersection is empty.
	 * The intersection is not visited.
	 * 
	 * @param id The unique id of this intersection.
	 * @param latitude Its latitude on the map.
	 * @param longitude Its longitude on the map.
	 */
	public Intersection(long id, float latitude, float longitude) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.originatedSegments = new ArrayList<>();
		this.intersectionVisited = Visited.WHITE;
	}
	
	/**
	 * @return The id of this intersection.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id The new id of this intersection.
	 * It musts be an Long integer.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return The latitude of this intersection.
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude The new latitude of this intersection.
	 * It musts be a float number.
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return The longitude of this intersection.
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude The new longitude of this intersection.
	 * It musts be a float number.
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * intersectionVisited indicates if an intersection was visited when computing the shortest path
	 * (Dijkstra algorithm).
	 * 
	 * This intersection can be:
	 * <ul>
	 * <li>WHITE: the intersection is not visited yet.</li>
	 * <li>GREY: the intersection is visited and the shortest path to it is not found yet.</li>
	 * <li>BLACK: the intersection was visited and the shortest path to it was found yet.</li>
	 * </ul>
	 * 
	 * @return The status of this intersection during the Dijkstra algorithm.
	 *
	 * @see Model.Visited
	 * @see Map#computeShortestPath(Stop, ArrayList)
	 */
	public Visited getIntersectionVisited() {
		return intersectionVisited;
	}

	/**
	 * intersectionVisited indicates if an intersection was visited when computing the shortest path
	 * (Dijkstra algorithm).
	 * 
	 * This intersection can be:
	 * <ul>
	 * <li>WHITE: the intersection is not visited yet.</li>
	 * <li>GREY: the intersection is visited and the shortest path to it is not found yet.</li>
	 * <li>BLACK: the intersection was visited and the shortest path to it was found yet.</li>
	 * </ul>
	 * 
	 * @param intersectionVisited The status of this intersection during the Dijkstra algorithm.
	 * 
	 * @see Model.Visited
	 * @see Map#computeShortestPath(Stop, ArrayList)
	 */
	public void setIntersectionVisited(Visited intersectionVisited) {
		this.intersectionVisited = intersectionVisited;
	}

	/**
	 * @return The list of all segments that are originated from this intersection.
	 * 
	 * @see Model.Segment
	 */
	public ArrayList<Segment> getOriginatedSegments() {
		return originatedSegments;
	}

	/**
	 * @param originatedSegments The new list of the segments that are originated from this intersection.
	 * 
	 * @see Model.Segment
	 */
	public void setOriginatedSegments(ArrayList<Segment> originatedSegments) {
		this.originatedSegments = originatedSegments;
	}
	
	/**
	 * Adds a new segment to the list of segments that are originated from this intersection.
	 * 
	 * @param segment A segment. Its departure intersection must be this intersection.
	 * 
	 * @see #getOriginatedSegments()
	 * @see #setOriginatedSegments(ArrayList)
	 * @see Model.Segment
	 */
	public void addSegment(Segment segment) {
		this.originatedSegments.add(segment);
	}

	/**
	 * Searches the segment that is originated from this intersection 
	 * and which the arrival intersection is the one in parameters.
	 * 
	 * @param arrival Another intersection.
	 * 
	 * @return The segment that binds this intersection to the other intersection if it exists,
	 * null otherwise.
	 * 
	 * @see #getOriginatedSegments()
	 * @see #setOriginatedSegments(ArrayList)
	 * @see Model.Segment
	 */
	public Segment getIntersectionSegment(Intersection arrival) {
		Segment segmentRes = null;
		for(Segment segment : originatedSegments) {
			if(segment.getArrivalIntersection().equals(arrival)) {
				segmentRes = segment;
			}
		}
		return segmentRes;
	}
	
	/**
	 * @return A String with the information of this intersection, that can be printed.
	 */
	@Override
	public String toString() {
		String seg = "[";
		for(Segment s : originatedSegments) {
			seg += "Segment [departureIntersection=" + s.getDepartureIntersection().getId() + ", arrivalIntersection="
					+ s.getArrivalIntersection().getId() + ", length=" + s.getLength() + ", name=" + s.getName() + "], ";
		}
		return "Intersection [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", originatedSegments=[" + seg + "] ]";
	}
	
	/**
	 * Overrides equals method in Object class. 
	 * Compares the object in parameter to this intersection.
	 * The specified object is identical to this intersection if it is an intersection 
	 * and it has the same id, same coordinates (latitude and longitude) 
	 * and same list of originated segments as this intersection.
	 * 
	 * @param obj The object to be compared with.
	 * 
	 * @return TRUE if the specified object and this intersection are identical, FALSE otherwise.
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
		Intersection other = (Intersection) obj;
		return (id == other.id)
				&& (Float.floatToIntBits(latitude) == Float.floatToIntBits(other.latitude))
				&& (Float.floatToIntBits(longitude) == Float.floatToIntBits(other.longitude))
				&& (Objects.equals(originatedSegments, other.originatedSegments));
	}
}
