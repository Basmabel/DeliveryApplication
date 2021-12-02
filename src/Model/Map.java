package Model;

import java.util.*;
import java.util.Map.Entry;

import ObserverDP.Observable;

/**
 * Map is the class that defines the city map.
 * 
 * A map is composed of:
 * <ul>
 * <li>A SortedMap of intersection ids and intersections (a TreeMap).</li>
 * <li>A tour.</li>
 * </ul>
 * 
 * Map is created when loading a map by parsing an XML map file.
 * When loading a map, each intersection is added to the TreeMap with its id 
 * and the segments of road are saved in their respective intersection they are
 * originated from.<br/>
 * 
 * Map inherits Observable class in order to repaint it more easily when it has been modified.
 * 
 * @author H4122
 * 
 * @see Model.Intersection
 * @see Model.Segment
 * @see Model.Tour
 * @see Controller.Controller#parseMap(org.w3c.dom.Document)
 *
 */
public class Map extends Observable {
	private SortedMap<Long, Intersection> intersections;
	private Tour tour;

	/**
	 * Default class constructor.
	 * The TreeMap of intersections is empty and the tour initialized with its default constructor.
	 * 
	 * @see Model.Tour#Tour()
	 */
	public Map() {
		intersections = new TreeMap<>();
		tour = new Tour();
	}

	/* ------------- GETTERS AND SETTERS ----------------- */
	
	/**
	 * @return The tour associated to this map.
	 * 
	 * @see Model.Tour
	 */
	public Tour getTour() {
		return tour;
	}

	/**
	 * @param tour The new tour of this map.
	 * 
	 * @see Model.Tour
	 */
	public void setTour(Tour tour) {
		this.tour = tour;
	}
	
	/**
	 * @return The SortedMap of ids and intersections of this map.
	 */
	public SortedMap<Long, Intersection> getIntersections() {
		return intersections;
	}
	
	/**
	 * @param id The id of the intersection to get.
	 * @return The intersection which the id was given in parameter if it exists, 
	 * null otherwise.
	 * 
	 * @see Model.Intersection
	 */
	public Intersection getIntersectionById(long id) {
		return intersections.get(id);
	}

	/**
	 * @param intersections The new SortedMap of ids and intersections of this map.
	 * 
	 * @see Model.Intersection
	 */
	public void setIntersections(SortedMap<Long, Intersection> intersections) {
		this.intersections = intersections;
	}

	/* ------------ END GETTERS AND SETTERS --------------- */
	
	/**
	 * Adds an intersection to this map.
	 * 
	 * @param intersection The intersection to be added to the TreeMap of this map.
	 * 
	 * @see Model.Intersection
	 */
	public void addIntersection(Intersection intersection) {
		intersections.put(intersection.getId(), intersection);
	}
	
	/**
	 * Notify the observers of this map to repaint the latter when it is done loading.
	 * 
	 * @see ObserverDP.Observable#notifyObservers(Object)
	 */
	public void endLoadMap() {
		notifyObservers("loadMap");
	}

	/**
	 * Calculates the boundaries of this map that are:
	 * <ul>
	 * <li>The minimum latitude of all intersections latitude that composed this map.</li>
	 * <li>The minimum longitude of all intersections longitude that composed this map.</li>
	 * <li>The maximum latitude of all intersections latitude that composed this map.</li>
	 * <li>The maximum longitude of all intersections longitude that composed this map.</li>
	 * </ul>
	 * 
	 * @return The boundaries of this map.
	 * 
	 * @see Model.Intersection
	 * @see Model.MapBoundaries
	 */
	public MapBoundaries computeMapBoundaries() {
		float minLat = intersections.get(intersections.firstKey()).getLatitude();
		float maxLat = intersections.get(intersections.firstKey()).getLatitude();
		float minLong = intersections.get(intersections.firstKey()).getLongitude();
		float maxLong = intersections.get(intersections.firstKey()).getLongitude();

		for (Entry<Long, Intersection> entry : intersections.entrySet()) {
			if (entry.getValue().getLatitude() > maxLat)
				maxLat = entry.getValue().getLatitude();
			if (entry.getValue().getLatitude() < minLat)
				minLat = entry.getValue().getLatitude();

			if (entry.getValue().getLongitude() > maxLong)
				maxLong = entry.getValue().getLongitude();
			if (entry.getValue().getLongitude() < minLong)
				minLong = entry.getValue().getLongitude();
		}

		return new MapBoundaries(minLat, maxLat, minLong, maxLong);

	}

	/**
	 * Computes the shortest path from the given departure stop to each stop of the list in parameter.
	 * Uses an adaptation of Dijkstra algorithm:
	 * This method saves and updates the shortest distance between the departure stop and another intersection 
	 * such as it was found until this iteration, and the predecessor of each intersection that corresponds to
	 * the distance found.<br/>
	 * 
	 * First, distances are set to infinite and predecessors to null. 
	 * All intersections of this map are set to WHITE (not visited yet). 
	 * Departure stop is GREY (visited and in treatment).
	 * At each iteration, the algorithm selects a grey intersection and visits all its successors 
	 * (the arrival intersection of its originated segments).
	 * If a shorter distance to reach the successor is found, distances predecessors are updated (release procedure).
	 * When the algorithm visits a new intersection, the latter is set to GREY.
	 * An intersection is set to BLACK when the algorithm has visited all its successors 
	 * and so the shortest path to it has been found.<br/>
	 * 
	 * The algorithm is completed when the shortest distance to reach all stops in the list has been found.
	 * Then the path from the departure stop to each stops is computed using predecessors list. 
	 * 
	 * @param dstop The stop from where the shortest paths must be originated.
	 * @param listStop The list of stop to compute the shortest path to.
	 * 
	 * @return The list of each shortest path from the departure stop to each stop of the list.
	 * 
	 * @see Model.Map#getLowestDistanceIntersection(ArrayList, SortedMap)
	 * @see Model.Intersection
	 * @see Model.Stop
	 * @see Model.Visited
	 * @see Model.Segment
	 * @see Model.Path
	 */
	public ArrayList<Path> computeShortestPath(Stop dstop, ArrayList<Stop> listStop) {
		// d = the minimal distance between dstop and the current astop
		// pi = the previous stop of the current stop

		// Initialization of d and pi maps
		SortedMap<Long, Float> d = new TreeMap<Long, Float>();
		SortedMap<Long, Long> pi = new TreeMap<Long, Long>();
		for (Entry<Long, Intersection> i : this.intersections.entrySet()) {
			d.put(i.getValue().getId(), Float.MAX_VALUE);
			pi.put(i.getValue().getId(), null);
			i.getValue().setIntersectionVisited(Visited.WHITE);
		}
		// Beginning of the algorithm
		d.put(dstop.getId(), (float) 0);
		dstop.setIntersectionVisited(Visited.GREY);

		ArrayList<Long> greyIntersection = new ArrayList<Long>();
		greyIntersection.add(dstop.getId());
		while (!greyIntersection.isEmpty()) {
			Intersection nearestIntersection = getLowestDistanceIntersection(greyIntersection, d);

			for (Segment segment : nearestIntersection.getOriginatedSegments()) {
				Intersection currentArrival = segment.getArrivalIntersection();

				if ((currentArrival.getIntersectionVisited() == Visited.WHITE)
						|| (currentArrival.getIntersectionVisited() == Visited.GREY)) {
					// release

					if (d.get(currentArrival.getId()) > (d.get(nearestIntersection.getId()) + segment.getLength())) {
						d.put(currentArrival.getId(), (d.get(nearestIntersection.getId()) + segment.getLength()));
						pi.put(currentArrival.getId(), nearestIntersection.getId());
					}

					if (currentArrival.getIntersectionVisited() == Visited.WHITE) {
						currentArrival.setIntersectionVisited(Visited.GREY);
						greyIntersection.add(currentArrival.getId());
					}
				}
			}
			nearestIntersection.setIntersectionVisited(Visited.BLACK);
			Intersection i = new Intersection(nearestIntersection.getId(), nearestIntersection.getLatitude(),
					nearestIntersection.getLongitude());
			i.setOriginatedSegments(nearestIntersection.getOriginatedSegments());
			i.setIntersectionVisited(nearestIntersection.getIntersectionVisited());

			greyIntersection.remove(Long.valueOf(i.getId()));
			// stops the exploration when all the distances to the stops are calculated
			boolean allBlack = true;
			for (Stop stop : listStop) {
				if (stop.getIntersectionVisited() != Visited.BLACK) {
					allBlack = false;
				}
			}
			if (allBlack) {
				break;
			}
		}
		// Creates the paths
		ArrayList<Path> listPath = new ArrayList<Path>();
		for (Stop astop : listStop) {
			Path path = new Path();
			path.setDepartureStop(dstop);
			path.setArrivalStop(astop);
			float pathDuration = d.get(astop.getId()) * (float) (3600.0 / 15000.0);
			path.setPathDuration(pathDuration);
			Intersection currentIntersection = astop;
			while (!currentIntersection.equals(dstop)) {
				if (pi.get(currentIntersection.getId()) == null) {
					break;
				}
				Segment segment = this.getIntersectionById(pi.get(currentIntersection.getId()))
						.getIntersectionSegment(this.getIntersectionById(currentIntersection.getId()));
				ArrayList<Segment> pathSegments = path.getPathSegments();
				pathSegments.add(0, segment);
				path.setPathSegments(pathSegments);
				currentIntersection = this.getIntersectionById(pi.get(currentIntersection.getId()));
			}
			listPath.add(path);
		}
		return listPath;
	}

	/**
	 * Finds the id of the intersection that has the lowest associated distance among a list of intersections.
	 * 
	 * @param greyIntersection The list of ids of the intersections among which the lowest distance has to be found.
	 * @param d The SortedMap of all intersection ids and corresponding distances.
	 * @return The id of the intersection that has the lowest distance.
	 * 
	 * @see Model.Map#computeShortestPath(Stop, ArrayList)
	 * @see Model.Intersection
	 */
	public Intersection getLowestDistanceIntersection(ArrayList<Long> greyIntersection, SortedMap<Long, Float> d) {
		// get lowest distance intersection
		Intersection lowestDistanceIntersection = new Intersection();
		float lowestDistance = Float.MAX_VALUE;
		for (Long id : greyIntersection) {
			float intersectionDistance = d.get(id);
			if (intersectionDistance < lowestDistance) {
				lowestDistance = intersectionDistance;
				lowestDistanceIntersection = this.getIntersectionById(id);
			}
		}
		return lowestDistanceIntersection;
	}
	
	/**
	 * Searches if the given list of stops contains a stop located on the intersection in parameter.
	 * 
	 * @param listStop The list of stop to look into.
	 * @param i The intersection to search in the list.
	 * 
	 * @return TRUE if the list contains a stop located on the given intersection
	 * 
	 * @see Model.Intersection
	 * @see Model.Stop
	 */
	public boolean containsStop(ArrayList<Stop> listStop, Intersection i) {
		boolean res = false;
		for (Stop s : listStop) {
			if (s.getId() == i.getId()) {
				res = true;
				break;
			}
		}
		return res;
	}

	/**
	 * Adds a request in the computed tour.
	 * Tests if the request and the predecessors of its stop are correct and consistent.
	 * Then adds the pickup stop and the delivery stop of the request at the right place in the computed tour,
	 * and updates the tour (new paths to and from new stop + recalculate the arrival times).<br/>
	 * 
	 * The four stops needed are attributes of the tour.
	 * 
	 * @throws Exception If the request or the predecessors are not valid.
	 * 
	 * @see Model.Map#addStopInTour(int, Stop)
	 * @see Model.Stop
	 * @see Model.Tour
	 * @see Model.Tour#getOrderedTravel()
	 * @see Model.Tour#computeArrivalTimes()
	 */
	public void addRequest() throws Exception {

		// managing of limit cases

		if (tour.getPickupStop().equals(tour.getTourDeparture())
				|| tour.getDeliveryStop().equals(tour.getTourDeparture())) {
			throw new Exception("the tour departure cannot be a pickup or delivery point");
		}

		if (tour.getOrderedTravel().indexOf(tour.getPickupStopPredecessor()) > tour.getOrderedTravel()
				.indexOf(tour.getPickupStopPredecessor())) {
			throw new Exception("the departure stop needs to be before the delivery stop");
		}

		if (this.tour.getTourDeparture().equals(tour.getPickupStop())
				|| this.tour.getTourDeparture().equals(tour.getDeliveryStop())
				|| this.tour.getTourDeparture().equals(tour.getDeliveryStopPredecessor())) {
			throw new Exception("the tour departure cannot be a pickup or delivery point");
		}
		
		tour.getTourRequests().add(new Request(tour.getPickupStop(), tour.getDeliveryStop()));

		int indexPickupPredecessor = tour.getOrderedTravel().indexOf(tour.getPickupStopPredecessor());
		int indexDeliveryPredecessor = tour.getOrderedTravel().indexOf(tour.getDeliveryStopPredecessor());
		// DELIVERY
		addStopInTour(indexDeliveryPredecessor, tour.getDeliveryStop());
		// PICKUP
		addStopInTour(indexPickupPredecessor, tour.getPickupStop());

		tour.computeArrivalTimes();
	}

	/**
	 * Adds a stop to the computed tour.
	 * The shortest path from the predecessor to the new stop is computed, 
	 * then the shortest path from the new stop to the former successor of the predecessor.
	 * Finally, the stop in parameter is added in the tour after its given predecessor.
	 * 
	 * @param stopPredecessorIndex The index in the tour of predecessor of the stop to add to the computed tour.
	 * @param newStop The stop to add to the computed tour.
	 * 
	 * @see Model.Map#computeShortestPath(Stop, ArrayList)
	 * @see Model.Stop
	 * @see Model.Path
	 * @see Model.Tour
	 * @see Model.Tour#getOrderedTravel()
	 */
	private void addStopInTour(int stopPredecessorIndex, Stop newStop) {
		int stopSuccessorIndex = stopPredecessorIndex + 1;
		// shortest Path from predecessor to new stop
		ArrayList<Stop> dijkstraStopList = new ArrayList<Stop>(Arrays.asList(newStop));

		Path pathPredecessorStop = computeShortestPath(tour.getOrderedTravel().get(stopPredecessorIndex),
				dijkstraStopList).get(0);
		tour.getOrderedTravel().get(stopPredecessorIndex).setNextPath(pathPredecessorStop);
		// shortest Path from new stop to successor

		dijkstraStopList = new ArrayList<Stop>(Arrays.asList(tour.getOrderedTravel().get(stopSuccessorIndex)));
		Path pathSuccessorStop = computeShortestPath(newStop, dijkstraStopList).get(0);
		newStop.setNextPath(pathSuccessorStop);
		// add the stop in orderedTravel
		tour.getOrderedTravel().add(stopSuccessorIndex, newStop);
	}

	/**
	 * Deletes a request from the computed tour.
	 * Tests if the request is correct.
	 * Then removes the pickup stop and the delivery stop of the request from the computed tour,
	 * and updates the tour (new paths instead of the former stops + recalculate the arrival times).
	 * 
	 * @param requestToDelete The request to delete from the computed tour.
	 * 
	 * @throws Exception If the request is null.
	 * 
	 * @see Model.Map#computeShortestPath(Stop, ArrayList)
	 * @see Model.Stop
	 * @see Model.Path
	 * @see Model.Tour
	 * @see Model.Tour#getOrderedTravel()
	 * @see Model.Tour#computeArrivalTimes()
	 */
	public void deleteRequest(Request requestToDelete) throws Exception {
		if (requestToDelete == null) {
			throw new Exception("the stop you chose does not belong to a request");
		}
		ArrayList<Stop> orderedTravel = this.tour.getOrderedTravel();
		int indexPickup = orderedTravel.indexOf(requestToDelete.getPickupStop());
		int indexDelivery = orderedTravel.indexOf(requestToDelete.getDeliveryStop());

		Stop deliveryPredecessor = orderedTravel.get(indexDelivery - 1);
		Stop deliverySuccessor = orderedTravel.get(indexDelivery + 1);
		ArrayList<Stop> dijkstraStopList = new ArrayList<Stop>(Arrays.asList(deliverySuccessor));
		Path deliveryPath = computeShortestPath(deliveryPredecessor, dijkstraStopList).get(0);
		orderedTravel.get(indexDelivery - 1).setNextPath(deliveryPath);
		this.tour.getOrderedTravel().remove(indexDelivery);

		Stop pickupPredecessor = orderedTravel.get(indexPickup - 1);
		Stop pickupSuccessor = orderedTravel.get(indexPickup + 1);
		dijkstraStopList = new ArrayList<Stop>(Arrays.asList(pickupSuccessor));
		Path pickupPath = computeShortestPath(pickupPredecessor, dijkstraStopList).get(0);
		orderedTravel.get(indexPickup - 1).setNextPath(pickupPath);
		this.tour.getOrderedTravel().remove(indexPickup);

		tour.getTourRequests().remove(requestToDelete);

		tour.computeArrivalTimes();
	}
}
