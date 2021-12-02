package Model;

/**
 * Boundaries of the map.
 * It is composed of four float numbers that correspond to:
 * <ul>
 * <li>The minimum latitude of all intersections latitude that composed this map.</li>
 * <li>The minimum longitude of all intersections longitude that composed this map.</li>
 * <li>The maximum latitude of all intersections latitude that composed this map.</li>
 * <li>The maximum longitude of all intersections longitude that composed this map.</li>
 * </ul>
 * 
 * @author H4122
 *
 * @see Model.Intersection
 * @see Model.Map#computeMapBoundaries()
 */
public class MapBoundaries {

	private float minLat;
	private float maxLat;
	private float minLong;
	private float maxLong;
	
	/**
	 * Class constructor.
	 * Creates MapBoundaries with the coordinates in parameters.
	 * 
	 * @param minLat The minimum latitude of all intersections latitude that composed this map.
	 * @param maxLat The maximum latitude of all intersections latitude that composed this map.
	 * @param minLong The minimum longitude of all intersections longitude that composed this map.
	 * @param maxLong The maximum longitude of all intersections longitude that composed this map.
	 * 
	 * @see Model.Intersection
	 * @see Model.Map#computeMapBoundaries()
	 */
	public MapBoundaries(float minLat, float maxLat, float minLong, float maxLong) {
		super();
		this.minLat = minLat;
		this.maxLat = maxLat;
		this.minLong = minLong;
		this.maxLong = maxLong;
	}
	
	/**
	 * @return The minimum latitude.
	 */
	public float getMinLat() {
		return minLat;
	}
	
	/**
	 * @return The maximum latitude.
	 */
	public float getMaxLat() {
		return maxLat;
	}
	
	/**
	 * @return The minimum longitude.
	 */
	public float getMinLong() {
		return minLong;
	}
	
	/**
	 * @return The maximum longitude.
	 */
	public float getMaxLong() {
		return maxLong;
	}
}
