package Controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Model.CompleteGraph;
import Model.Path;
import Model.Intersection;
import Model.Map;
import Model.MapBoundaries;
import Model.Request;
import Model.Segment;
import Model.Stop;
import Model.TSP;
import Model.TSP1;
import Model.Tour;
import View.DrawnMap;
import View.LandingPage;
import View.Menu;

/**
 * Controller is the conductor of the application.
 * It binds the View package and the Model package and does the right calls to methods and class.
 * 
 * The controller is composed of:
 * <ul>
 * <li>A map.</li>
 * <li>A matrix with shortest paths between two stops.</li>
 * <li>A class that helps to compute the tour using the TSP algorithm.</li>
 * <li>A list of commands.</li>
 * </ul>
 * A list of states:
 * <ul>
 * <li>InitialState</li>
 * <li>MapLoadedState</li>
 * <li>RequestsLoadedState</li>
 * <li>ComputedTourState</li>
 * <li>TimeoutState</li>
 * <li>DeleteRequestState</li>
 * <li>AddPickupStopState</li>
 * <li>AddPickupPredecessorState</li>
 * <li>AddDeliveryStopState</li>
 * <li>AddDeliveryPredecessorState</li>
 * </ul>
 * GUI components:
 * <ul>
 * <li>A landing page.</li>
 * <li>A menu.</li>
 * </ul>
 * 
 * @author H4122
 * 
 * @see Controller.ListOfCommands
 * @see Controller.State
 * @see Model.Map
 * @see Model.Path
 * @see Model.TSP
 * @see View.LandingPage
 * @see View.Menu
 */
public class Controller {
	
	/**
	 * The map of the controller. It can be modified.
	 * 
	 * @see Controller#getMap()
	 * @see Controller#setMap()
	 */
	private Map map;
	
	/**
	 * The current state of the controller. It can be modified.
	 * 
	 * @see Controller#getCurrentState()
	 * @see Controller#setCurrentState()
	 */
	private State currentState;
	
	/**
	 * The initial state of the controller.
	 * 
	 * @see Controller#getInitialState()
	 */
	private State initialState;
	
	/**
	 * The state of the controller in which its map is loaded.
	 * 
	 * @see Controller#getMapLoadedState()
	 */
	private State mapLoadedState;
	
	/**
	 * The state of the controller in which the tour requests of its map are loaded.
	 * 
	 * @see Controller#getRequestsLoadedState()
	 */
	private State requestsLoadedState;
	
	/**
	 * The state of the controller in which the tour is computed.
	 * 
	 * @see Controller#getComputedTourState()
	 */
	private State computedTourState;
	
	/**
	 * The state of the controller when we choose to add a new request (by clicking
	 * on the 'add a request' button) and we are ready to choose the new pickup stop
	 * to add to the tour of its map.
	 * 
	 * @see Controller#getAddPickupStopState()
	 */
	private State addPickupStopState;
	
	/**
	 * The state of the controller when a new pickup stop is chosen to be added to
	 * the tour of its map. From this state, we can select the predecessor of the
	 * new pickup stop to add.
	 * 
	 * @see Controller#getAddPickupPredecessorState()
	 */
	private State addPickupPredecessorState;
	
	/**
	 * The state of the controller when the predecessor of the new pickup stop to
	 * add has been selected and we can now add a new delivery stop to the tour.
	 * 
	 * @see Controller#getAddDeliveryStopState()
	 */
	private State addDeliveryStopState;
	
	/**
	 * The state of the controller when a new delivery stop is chosen to be added to
	 * the tour. From this state, we can select the predecessor of the new delivery
	 * stop to add.
	 * 
	 * @see Controller#getAddDeliveryPredecessorState()
	 */
	private State addDeliveryPredecessorState;
	
	/**
	 * The state of the controller when we choose to delete a request (by clicking
	 * on the button 'delete a request')
	 * 
	 * @see Controller#getAddDeliveryPredecessorState()
	 */
	private State deleteRequestState;
	
	/**
	 * This state of the controller is entered if <code>searchSolution</code> and
	 * <code>continueTSP</code> methods for computing the tour reached <code>timeLimit</code>.
	 * 
	 * @see Controller#getTimeoutState()
	 */
	private State timeoutState;
	
	/**
	 * The landing page of the application GUI
	 * 
	 * @see Controller#getLandingPage()
	 * @see Controller#setLandingPage()
	 */
	private LandingPage landingPage;
	
	/**
	 * The menu of the application GUI
	 * 
	 * @see Controller#getMenu()
	 * @see Controller#setMenu()
	 * @see Menu
	 */
	private Menu menu;
	
	/**
	 * The list of commands of the controller. It contains the add request command
	 * and the remove request command and is necessary to apply the Command design
	 * pattern and therefore to manage the undo redo functionalities on these two
	 * commands.
	 * 
	 * @see Controller#getListOfCommands()
	 */
	private ListOfCommands listOfCommands;
	
	/**
	 * A class that helps to compute the tour using the TSP algorithm.
	 * 
	 * @see Model.TSP
	 */
	private TSP tsp;

	/**
	 * A matrix with shortest paths between two stops.
	 * 
	 * @see Model.Path
	 * @see Model.Map#computeShortestPath(Stop, ArrayList)
	 */
	private Path[][] paths;

	// ------------------------------ CONSTRUCTORS
	/**
	 * Default class constructor.
	 * All attributes are initialized.
	 */
	public Controller() {
		super();
		this.map = new Map();
		this.initialState = new InitialState(this);
		this.mapLoadedState = new MapLoadedState(this);
		this.requestsLoadedState = new RequestsLoadedState(this);
		this.computedTourState = new ComputedTourState(this);
		this.addPickupStopState = new AddPickupStopState(this);
		this.addPickupPredecessorState = new AddPickupPredecessorState(this);
		this.addDeliveryStopState = new AddDeliveryStopState(this);
		this.addDeliveryPredecessorState = new AddDeliveryPredecessorState(this);
		this.deleteRequestState = new DeleteRequestState(this);
		this.timeoutState = new TimeoutState(this);
		this.currentState = initialState;
		this.tsp = new TSP1();
		this.landingPage = new LandingPage(this);
		this.menu = null;
		this.listOfCommands = new ListOfCommands();
	}

	/**
	 * Class constructor with an existing map and an existing tour to use.
	 * Other attributes are also initialized.
	 * @param map The map to use.
	 * @param tour The tour to use.
	 */
	public Controller(Map map, Tour tour) {
		super();
		this.map = map;
		this.map.setTour(tour);
		this.initialState = new InitialState(this);
		this.mapLoadedState = new MapLoadedState(this);
		this.requestsLoadedState = new RequestsLoadedState(this);
		this.computedTourState = new ComputedTourState(this);
		this.addPickupStopState = new AddPickupStopState(this);
		this.addPickupPredecessorState = new AddPickupPredecessorState(this);
		this.addDeliveryStopState = new AddDeliveryStopState(this);
		this.addDeliveryPredecessorState = new AddDeliveryPredecessorState(this);
		this.deleteRequestState = new DeleteRequestState(this);
		this.timeoutState = new TimeoutState(this);
		this.currentState = initialState;
		this.tsp = new TSP1();
		this.landingPage = new LandingPage(this);
		this.menu = null;
		this.listOfCommands = new ListOfCommands();
	}
	// ------------------------------ CONSTRUCTORS END

	// ------------------------------ GETTERS AND SETTERS
	public LandingPage getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(LandingPage landingPage) {
		this.landingPage = landingPage;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public TSP getTsp() {
		return tsp;
	}

	public Path[][] getPaths() {
		return paths;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public State getInitialState() {
		return initialState;
	}

	public State getMapLoadedState() {
		return mapLoadedState;
	}

	public State getRequestsLoadedState() {
		return requestsLoadedState;
	}

	public State getComputedTourState() {
		return computedTourState;
	}

	public State getAddPickupStopState() {
		return addPickupStopState;
	}

	public State getAddPickupPredecessorState() {
		return addPickupPredecessorState;
	}

	public State getAddDeliveryStopState() {
		return addDeliveryStopState;
	}

	public State getAddDeliveryPredecessorState() {
		return addDeliveryPredecessorState;
	}

	public State getDeleteRequestState() {
		return deleteRequestState;
	}
	
	public State getTimeoutState() {
		return timeoutState;
	}

	public SortedMap<Long, Intersection> getMapIntersections() {
		return map.getIntersections();
	}

	public MapBoundaries computeMapBoundaries() {
		return map.computeMapBoundaries();
	}
	
	public ListOfCommands getListOfCommands() {
		return listOfCommands;
	}
	// ------------------------------ GETTERS AND SETTERS END

	// ------------------------------STATE DESIGN PATTERN METHODS

	/**
	 * Calls the same-name method of current state.
	 * 
	 * @see Controller.State#loadNewMap()
	 */
	public void loadNewMap() {
		currentState.loadNewMap();
	}

	/**
	 * Calls the same-name method of current state.
	 * 
	 * @param xmlFile The name of the XML file that contains the map to parse.
	 * 
	 * @see Controller.State#loadMap(String)
	 */
	public void loadMap(String xmlFile) {
		currentState.loadMap(xmlFile);
	}

	/**
	 * Calls the same-name method of current state.
	 * 
	 * @param xmlFile The name of the XML file that contains the requests to parse.
	 * @param drawnMap The GUI class to draw and interact with the map.
	 * 
	 * @see Controller.State#loadRequests(String, DrawnMap)
	 * @see View.DrawnMap
	 */
	public void loadRequests(String xmlFile, DrawnMap drawnMap) {
		currentState.loadRequests(xmlFile, drawnMap);
	}

	/**
	 * Calls the same-name method of current state.
	 * 
	 * @see Controller.State#computeTour()
	 */
	public void computeTour() {
		currentState.computeTour();
	}
	
	/**
	 * Calls the same-name method of current state.
	 * 
	 * @param stop The stop that has been selected on the map.
	 * 
	 * @see Controller.State#clicStop(Stop)
	 * @see Model.Stop
	 */
	public void clicStop(Stop stop) {
		currentState.clicStop(stop);
	}
	
	/**
	 * Calls the same-name method of current state.
	 * 
	 * @param stopCreated The stop that has been created by clicking on an intersection on the map.
	 * 
	 * @see Controller.State#clicIntersection(Stop)
	 * @see Model.Stop
	 */
	public void clicIntersection(Stop stopCreated) {
		currentState.clicIntersection(stopCreated);
	}
	
	/**
	 * Calls the same-name method of current state.
	 * 
	 * @param requestToDelete The request to delete.
	 * 
	 * @see Controller.State#deleteRequest(Request, ListOfCommands)
	 * @see Controller.ListOfCommands
	 * @see Model.Request
	 */
	public void deleteRequest(Request requestToDelete) {
		currentState.deleteRequest(requestToDelete,this.listOfCommands);
	}
	
	/**
	 * Calls the same-name method of current state.
	 * 
	 * @param deliveryPredecessor The stop that has been selected on the map to be the predecessor 
	 * of the new delivery stop.
	 * 
	 * @see Controller.State#addRequest(Stop, ListOfCommands)
	 * @see Controller.ListOfCommands
	 * @see Model.Stop
	 */
	public void addRequest(Stop deliveryPredecessor) {
		currentState.addRequest(deliveryPredecessor, this.listOfCommands);
		
	}
	
	/**
	 * Calls the right method of the controller according to the current state.
	 * 
	 * @param stop The stop that has been selected on the map. 
	 * 
	 * @see Controller.Controller#clicStop(Stop)
	 * @see Controller.Controller#clicIntersection(Stop)
	 * @see Controller.Controller#addRequest(Stop)
	 */
	public void addRequestFinal(Stop stop) {
		if(currentState.equals(addPickupStopState)) {
			clicIntersection(stop);
			return;
		}else if(currentState.equals(addPickupPredecessorState)) {
			clicStop(stop);
			return;
		}else if(currentState.equals(addDeliveryStopState)) {
			clicIntersection(stop);
			return;
		}else if(currentState.equals(addDeliveryPredecessorState)){
			addRequest(stop);
			return;
		}		
	}
	
	/**
	 * Calls the same-name method of current state.
	 * 
	 * @see Controller.State#changeStateDelete()
	 */
	public void changeStateDelete () {
		currentState.changeStateDelete();
	}
	
	/**
	 * Calls the same-name method of current state.
	 * 
	 * @see Controller.State#changeStateAdd()
	 */
	public void changeStateAdd() {
		currentState.changeStateAdd();
	}
	
	/**
	 * Calls the same-name method of current state.
	 * 
	 * @see Controller.State#undo(ListOfCommands)
	 * @see Controller.ListOfCommands
	 */
	public void undo() {
		currentState.undo(listOfCommands);
	}
	
	/**
	 * Calls the same-name method of current state.
	 * 
	 * @see Controller.State#redo(ListOfCommands)
	 * @see Controller.ListOfCommands
	 */
	public void redo() {
		currentState.redo(listOfCommands);
	}
	
	/**
	 * Calls the same-name method of current state.
	 * 
	 * @see Controller.State#cancel()
	 */
	public void cancel() {
		currentState.cancel();
	}
	
	// ------------------------------STATE DESIGN PATTERN METHODS END
	

	/**
	 * Parses the xml file with a given path and calls parseMap if the file is a map file
	 * or parseRequests if the file is a requests file
	 * 
	 * @param fileName the path to the location of the file
	 * 
	 * @throws Exception when the file cannot be parsed for a given reason
	 * 
	 * @see Controller.Controller#parseMap(org.w3c.dom.Document)
	 * @see Controller.Controller#parseRequests(org.w3c.dom.Document)
	 */
	public void parseFile(String fileName) throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			// process XML securely
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			// parse XML file
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(new File(fileName));

			doc.getDocumentElement().normalize();

			if (doc.getDocumentElement().getNodeName() == "map" && currentState.equals(initialState))
				parseMap(doc);
			else if (doc.getDocumentElement().getNodeName() == "planningRequest" && (currentState.equals(mapLoadedState)
					|| currentState.equals(requestsLoadedState) || currentState.equals(computedTourState) ||
					currentState.equals(addDeliveryPredecessorState) || currentState.equals(addDeliveryStopState)
					|| currentState.equals(addPickupPredecessorState) || currentState.equals(addPickupStopState)
					|| currentState.equals(deleteRequestState)
					))
				parseRequests(doc);
			else
				throw new Exception("Unknown root tag in xml file Exception");

		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new Exception("Unknown file type Exception");
		}
	}

	/**
     * Parses a document that has been generated from a map XML file by parseFile().
     * As a result, the intersections and the segments of the file are set in map.
     * @param doc The document that has been generated from a map XML file by parseFile()
     * @throws Exception when the map document cannot be parsed for a given reason
     * @see Controller.Controller#parseFile(String)
     */ 
	public void parseMap(Document doc) throws Exception {
		map.setIntersections(new TreeMap<>());

		// get intersection nodes
		NodeList list = doc.getElementsByTagName("intersection");

		for (int temp = 0; temp < list.getLength(); temp++) {
			Node node = list.item(temp);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;

				String id = element.getAttribute("id");
				String latitude = element.getAttribute("latitude");
				String longitude = element.getAttribute("longitude");

				if (id.isEmpty() || latitude.isEmpty() || longitude.isEmpty()) {
					throw new Exception("missing attribute in MapFile Exception");
				}
				if (Float.parseFloat(latitude) < -180 || Float.parseFloat(latitude) > 180
						|| Float.parseFloat(longitude) < -180 || Float.parseFloat(longitude) > 180) {
					throw new Exception("Aberrant value in MapFile Exception");
				}
				Intersection intersection = new Intersection(Long.parseLong(id), Float.parseFloat(latitude),
						Float.parseFloat(longitude));
				map.addIntersection(intersection);
			}
		}

		// get segment nodes
		list = doc.getElementsByTagName("segment");

		for (int temp = 0; temp < list.getLength(); temp++) {
			Node node = list.item(temp);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;

				String idDestination = element.getAttribute("destination");

				String length = element.getAttribute("length");
				String name = element.getAttribute("name");
				String idOrigin = element.getAttribute("origin");

				if (idDestination.isEmpty() || length.isEmpty() || idOrigin.isEmpty()) {// name can be empty (cf
																						// smallMap.xml)
					throw new Exception("missing attribute in MapFile Exception");
				}
				if (Float.parseFloat(length) < 0) {
					throw new Exception("Aberrant value in MapFile Exception");
				}
				if (!map.getIntersections().containsKey(Long.parseLong(idOrigin))
						|| !map.getIntersections().containsKey(Long.parseLong(idDestination))) {
					throw new Exception("Non existent intersection in MapFile Exception");
				}
				Intersection origin = map.getIntersectionById(Long.parseLong(idOrigin));
				Intersection destination = map.getIntersectionById(Long.parseLong(idDestination));

				Segment segment = new Segment(origin, destination, Float.parseFloat(length), name);
				map.getIntersectionById(Long.parseLong(idOrigin)).addSegment(segment);

			}
		}
		map.endLoadMap();
	}

	/**
     * Parses a document that has been generated from a requests XML file by parseFile().
     * As a result, the tour requests, the tour departure and the departure time are set in map's tour.
     * @param doc The document that has been generated from a requests XML file by parseFile()
     * @throws Exception when the requests document cannot be parsed for a given reason
     * @see Controller.Controller#parseFile(String)
     */
	public void parseRequests(Document doc) throws Exception {
		map.getTour().setTourRequests(new ArrayList<>());

		// get depot
		NodeList list = doc.getElementsByTagName("depot");
		Node node = list.item(0);
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			try {
				Element depotElement = (Element) node;

				String depotAddress = depotElement.getAttribute("address");
				String departureTimeStr = depotElement.getAttribute("departureTime");

				if (depotAddress.isEmpty() || departureTimeStr.isEmpty()) {
					throw new Exception("missing attribute in RequestsFile Exception");
				}

				if (!map.getIntersections().containsKey(Long.parseLong(depotAddress))) {
					throw new Exception("Non existant intersection Exception");
				}

				Intersection departureIntersection = map.getIntersectionById(Long.parseLong(depotAddress));

				Stop departureStop = new Stop(Long.parseLong(depotAddress), departureIntersection.getLatitude(),
						departureIntersection.getLongitude(), (float) 0.0);
				departureStop.setOriginatedSegments(departureIntersection.getOriginatedSegments());
				map.getTour().setTourDeparture(departureStop);

				Date departureTime = new SimpleDateFormat("H:m:s").parse(departureTimeStr);

				map.getTour().setDepartureTime(departureTime);

			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		// get requests

		list = doc.getElementsByTagName("request");

		for (int temp = 0; temp < list.getLength(); temp++) {
			node = list.item(temp);

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element element = (Element) node;

				String pickup = element.getAttribute("pickupAddress").trim();
				String delivery = element.getAttribute("deliveryAddress").trim();
				String pickupDuration = element.getAttribute("pickupDuration");
				String deliveryDuration = element.getAttribute("deliveryDuration");

				if (pickup.isEmpty() || delivery.isEmpty() || pickupDuration.isEmpty() || deliveryDuration.isEmpty()) {
					throw new Exception("missing attribute in RequestsFile Exception");
				}

				if (Float.parseFloat(pickupDuration) < 0.0 || Float.parseFloat(deliveryDuration) < 0.0) {
					throw new Exception("Aberrant value in requestsFile Exception");
				}

				if (!map.getIntersections().containsKey(Long.parseLong(pickup))
						|| !map.getIntersections().containsKey(Long.parseLong(delivery))) {
					throw new Exception("Non existant intersection Exception");
				}

				Stop pickupStop = new Stop(Long.parseLong(pickup),
						map.getIntersectionById(Long.parseLong(pickup)).getLatitude(),
						map.getIntersectionById(Long.parseLong(pickup)).getLongitude(),
						Float.parseFloat(pickupDuration));
				pickupStop
						.setOriginatedSegments(map.getIntersectionById(Long.parseLong(pickup)).getOriginatedSegments());
				Stop deliveryStop = new Stop(Long.parseLong(delivery),
						map.getIntersectionById(Long.parseLong(delivery)).getLatitude(),
						map.getIntersectionById(Long.parseLong(delivery)).getLongitude(),
						Float.parseFloat(deliveryDuration));
				deliveryStop.setOriginatedSegments(
						map.getIntersectionById(Long.parseLong(delivery)).getOriginatedSegments());
				map.getTour().addRequest(new Request(pickupStop, deliveryStop));

			}
		}
		map.getTour().getOrderedTravel().clear();
		map.getTour().endLoadRequests();
	}

	/**
	 * Computes a tour. Using the map and the Stops to visit, the shortest paths 
	 * between all the allowed stops are computed with Dijkstra. A complete graph
	 * is generated using the costs calculated previously. This graph is used to
	 * create tsp and search a solution with a time limit fixed to 20 seconds.
	 * This method returns if the computation of the tsp solution managed to compute
	 * an optimal tour during the time limit or not.
	 * 
	 * @param tourMap The current map
	 * 
	 * @see Controller#computeStopPaths(ArrayList, ArrayList)
	 * @see Controller#generateCompleteGraph(ArrayList, Path[][])
	 * @see Model.TSP1
	 * @see Model.TemplateTSP#searchSolution(int, Model.Graph)
	 */
	public int computeOptimisedTour(Map tourMap) {

		// initialization of parameters
		Tour ourTour = this.getMap().getTour();
		ArrayList<Stop> stopList = new ArrayList<Stop>();
		ArrayList<Stop> pickupList = new ArrayList<Stop>();

		stopList.add(ourTour.getTourDeparture());

		for (Request request : ourTour.getTourRequests()) {
			stopList.add(request.getPickupStop());
			pickupList.add(request.getPickupStop());
			stopList.add(request.getDeliveryStop());
		}

		this.paths = computeStopPaths(stopList, pickupList);
		CompleteGraph stopsGraph = generateCompleteGraph(stopList, paths);
		tsp = new TSP1();
		return tsp.searchSolution(20000, stopsGraph);
		
	}
	
	/**
	 * Computes the shortest paths between all the allowed stops with Dijkstra.
	 * the list of stops a stop can visit is generated for each stop
	 * the list depends if the stop is the stop is the departure stop, a pickup or delivery stop
	 * the following stop and their list are used for the Dijkstra calls
	 * the method returns a matrix of paths between the stops of the tour
	 * 
	 * @param stopList a list of all the stops of the requests including the departure stop
	 * @param pickupList a list of all the pickup stops of the requests

	 * @see Model.Map#computeShortestPaths()
	 */
	private Path[][] computeStopPaths(ArrayList<Stop> stopList, ArrayList<Stop> pickupList) {

		int nbStops = stopList.size();
		Path[][] paths = new Path[nbStops][nbStops];
		Tour ourTour = this.getMap().getTour();

		// for the tour Departure

		int departureIndex = stopList.indexOf(ourTour.getTourDeparture());
		int arrivalIndex;
		// Dijkstra Call
		ArrayList<Path> dijkstraPathList = this.getMap().computeShortestPath(ourTour.getTourDeparture(), pickupList);
		for (Path path : dijkstraPathList) {
			arrivalIndex = stopList.indexOf(path.getArrivalStop());
			paths[departureIndex][arrivalIndex] = path;
		}

		ArrayList<Stop> dijkstraStopList = new ArrayList<Stop>();

		for (Request request : ourTour.getTourRequests()) {

			// pickup

			// dijkstraStopList
			dijkstraStopList.clear();
			dijkstraStopList.addAll(stopList);
			dijkstraStopList.remove(request.getPickupStop());
			dijkstraStopList.remove(ourTour.getTourDeparture());

			dijkstraPathList = this.getMap().computeShortestPath(request.getPickupStop(), dijkstraStopList);
			departureIndex = stopList.indexOf(request.getPickupStop());

			for (Path path : dijkstraPathList) {
				arrivalIndex = stopList.indexOf(path.getArrivalStop());
				paths[departureIndex][arrivalIndex] = path;
			}

			// delivery

			// dijkstraStopList
			dijkstraStopList.remove(request.getDeliveryStop());
			dijkstraStopList.add(ourTour.getTourDeparture());

			// dijkstra
			dijkstraPathList = this.getMap().computeShortestPath(request.getDeliveryStop(), dijkstraStopList);
			departureIndex = stopList.indexOf(request.getDeliveryStop());

			for (Path path : dijkstraPathList) {
				arrivalIndex = stopList.indexOf(path.getArrivalStop());
				paths[departureIndex][arrivalIndex] = path;

			}
		}

		return paths;
	}

	/**
	 * Generates a graph with the stops of the tour as vertices, the arches as paths
	 * and the cost as path durations
	 * 
	 * @param stopList a list of all the stops of the requests including the departure stop
	 * @param paths a matrix of all the paths between the stops of the tour

	 * @see Model.CompleteGraph
	 */
	private CompleteGraph generateCompleteGraph(ArrayList<Stop> stopList, Path[][] paths) {
		Tour ourTour = this.getMap().getTour();
		int nbVertices = ourTour.getTourRequests().size() * 2 + 1;
		float[][] cost = new float[nbVertices][nbVertices];
		int departureIndex;
		int arrivalIndex;

		Path temporaryPath = new Path();
		for (Stop departureStop : stopList) {
			for (Stop arrivalStop : stopList) {
				departureIndex = stopList.indexOf(departureStop);
				arrivalIndex = stopList.indexOf(arrivalStop);
				temporaryPath = paths[departureIndex][arrivalIndex];

				if (temporaryPath != null) {
					cost[departureIndex][arrivalIndex] = temporaryPath.getPathDuration();
				} else {
					cost[departureIndex][arrivalIndex] = -1;
				}
			}
		}

		return new CompleteGraph(nbVertices, cost);
	}

	/**
	 * Resumes computation of tsp after a timeout with the parameters calculated at the last iteration
	 *
	 * @see Model.TSP#continueTSP(int)
	 */
	public int resumeComputing() {
		return tsp.continueTSP(20000);
	}

	/**
	 * Saves the solution of the hamiltonian path calculated with TSP
	 * either the computation is finished or not, the current solution is interpreted
	 * the nextPath attributes of each stop are updated
	 * the orderedTravel attribute is updated with the ordered stops to visit
	 * the arrival times to each stop are therefore calculated
	 * 
	 * @param tsp The tsp used since the beginning of the tour computation
	 * @param paths a matrix of all the paths between the stops of the tour
	 * 
	 * @see Model.TSP#getSolution(int)
	 * @see Model.Map
	 * @see Model.Tour#computeArrivalTimes()
	 */
	public void saveTSPSolution(TSP tsp, Path[][] paths) {
		int nbVertices = map.getTour().getTourRequests().size() * 2 + 1;
		Path temporaryPath;
		int pathDepartureIndex;
		int pathArrivalIndex;

		for (int i = 0; i < nbVertices - 1; i++) {
			pathDepartureIndex = tsp.getSolution(i);
			pathArrivalIndex = tsp.getSolution(i + 1);

			temporaryPath = paths[pathDepartureIndex][pathArrivalIndex];
			temporaryPath.getDepartureStop().setNextPath(temporaryPath);
			map.getTour().addInOrderedTravel(temporaryPath.getDepartureStop());
		}

		pathDepartureIndex = tsp.getSolution(nbVertices - 1);
		temporaryPath = paths[pathDepartureIndex][0];

		temporaryPath.getDepartureStop().setNextPath(temporaryPath);
		map.getTour().addInOrderedTravel(temporaryPath.getDepartureStop());
		map.getTour().addInOrderedTravel(map.getTour().getTourDeparture());
		map.getTour().computeArrivalTimes();
	}
}
