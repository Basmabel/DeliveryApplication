package Model;

import java.util.Objects;

/**
 * Segment is the class representing a road segment.<br/>
 * 
 * A segment is characterized by:
 * <ul>
 * <li>A departure intersection which is the origin of the segment.</li>
 * <li>An arrival intersection which is the end of the segment.</li>
 * <li>A length which is the length of the segment in meters.</li>
 * <li>A name which is the name of the segment.</li>
 * </ul>
 * 
 * Segments are created when loading a map by parsing an XML map file.<br/>
 * 
 * @see Controller.Controller#parseMap(org.w3c.dom.Document)
 * 
 * @author H4122
 */
public class Segment {
	
	private Intersection departureIntersection;
	private Intersection arrivalIntersection;
	private float length;
	private String name;
	
	/**
     * Constructor of Segment.
     * When the segment is created, the departureIntersection, the length, the arrivalIntersection 
     * and the name are initialized with the corresponding parameters.   
     * 
     * @param departureIntersection
     *            The departure intersection of the segment.
     * @param arrivalIntersection
     *            The arrival intersection of the segment.
     * @param length
     * 			  The length of the segment.
     * @param name
     * 			  The name of the segment.
     * 
     * @see Segment#departureIntersection
     * @see Segment#arrivalIntersection
     * @see Segment#length
     * @see Segment#name
     */
	public Segment(Intersection departureIntersection, Intersection arrivalIntersection, float length,
			String name) {
		
		this.departureIntersection = departureIntersection;
		this.arrivalIntersection = arrivalIntersection;
		this.length = length;
		this.name = name;
	}
	
	public Intersection getDepartureIntersection() {
		return departureIntersection;
	}

	public Intersection getArrivalIntersection() {
		return arrivalIntersection;
	}
	
	public float getLength() {
		return length;
	}

	public String getName() {
		return name;
	}
	
	/**
	 * Returns a String, which is the complete description of the segment that can be printed.
	 * 
	 * @return the string description of the segment 
	 */
	@Override
	public String toString() {
		return "Segment [departureIntersection=" + departureIntersection + ", arrivalIntersection="
		+ arrivalIntersection + ", length=" + length + ", name=" + name + "]";
	}

	/**
     * Overrides equals method in Object class. 
     * Compares the object in parameter to this segment.
     * The specified object is identical to this segment if it is a segment 
     * and it has the same departure intersection id, same arrival intersection id,  
     * the same length and the same name as this segment.
     * 
     * @param obj The object to be compared with.
     * 
     * @return TRUE if the specified object and this segment are identical, FALSE otherwise.
     */
	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Segment other = (Segment) obj;
        return Objects.equals(arrivalIntersection.getId(), other.arrivalIntersection.getId())
                && Objects.equals(departureIntersection.getId(), other.departureIntersection.getId())
                && Float.floatToIntBits(length) == Float.floatToIntBits(other.length)
                && Objects.equals(name, other.name);
    }
}
