package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.SortedMap;
import java.util.TreeMap;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Controller.Controller;
import Model.Intersection;
import Model.Segment;
import Model.Stop;
import Model.Request;

/**
 * ControllerTests is a class that tests different scenarios for the parseFile
 * function of the Controller class.
 * 
 * A ControllerTests is composed of:
 * <ul>
 * <li>A Controller</li>
 * </ul>
 * 
 * @author H4122
 * @see Controller.Controller#parseFile(String)
 */
class ControllerTests {

	Controller controller;

	/**
	 * Before any test the controller attribute is reset.
	 */
	@BeforeEach
	void initController() throws Exception {
		controller = new Controller();
	}

	/**
	 * Nature of test (parseMap) : The given file contains an unknown xml tag : <maperror>
	 * instead of <map>
	 */
	@Test
	void parseFileUnknownTagTest() {
		try {
			controller.parseFile(System.getProperty("user.dir").toString()
					+ "\\src\\tests\\fichiersXML2020\\smallMapUnknownTag.xml");
			fail("Unknown xml tag error not detected");
		} catch (Exception e) {
			System.out.println("success:" + e.toString());
		}
	}

	/**
	 * Nature of test (parseMap) : The given file contains a missing attribute in the xml tag :
	 * <intersection id="2129259178" latitude="45.750404"/> instead of
	 * <intersection id="2129259178" latitude="45.750404" longitude="4.8744674"/>
	 */
	@Test
	void parseMapMissingAttributeTest() {
		try {
			controller.parseFile(System.getProperty("user.dir").toString()
					+ "\\src\\tests\\fichiersXML2020\\smallMapMissingAttribute.xml");
			fail("Missing tag attribute error not detected");
		} catch (Exception e) {
			System.out.println("success:" + e.toString());
		}
	}

	/**
	 * Nature of test (parseMap) : The given file contains an aberrant value in a tag's
	 * attribute : (longitude superior to 180)
	 * <intersection id="2129259178" latitude="45.750404" longitude="200"/>
	 */
	@Test
	void parseMapAberrantValueTest() {
		try {
			controller.parseFile(System.getProperty("user.dir").toString()
					+ "\\src\\tests\\fichiersXML2020\\smallMapAberrantValue.xml");
			fail("Aberrant value error not detected");
		} catch (Exception e) {
			System.out.println("success:" + e.toString());
		}
	}

	/**
	 * Nature of test (parseMap) : The given file contains a nonexistent idIntersection for
	 * segment's tag :
	 * <segment destination="1" length="78.72686" name="Rue Danton" origin=
	 * "975886496"/>
	 */
	@Test
	void parseMapNonexistentIdIntersectionTest() {
		try {
			controller.parseFile(System.getProperty("user.dir").toString()
					+ "\\src\\tests\\fichiersXML2020\\smallMapNonexistentIdIntersection.xml");
			fail("Non existent idIntersection error not detected");
		} catch (Exception e) {
			System.out.println("success:" + e.toString());
		}
	}

	/**
	 * Nature of test (parseMap) : A basic success scenario for parsing a Map. A SortedMap with
	 * the intersections expected is created and compared to the intersections
	 * parsed from the function.
	 */
	@Test
	void parseMapTest() {
		try {
			controller.parseFile(
					System.getProperty("user.dir").toString() + "\\src\\tests\\fichiersXML2020\\ExtraSmallMap.xml");
			SortedMap<Long, Intersection> intersections = new TreeMap<>();
			intersections.put((long) 25175778, new Intersection(25175778, (float) 45.75343, (float) 4.8574653));
			intersections.put((long) 25175791, new Intersection(25175791, (float) 45.75406, (float) 4.857418));
			intersections.put((long) 2117622723, new Intersection(2117622723, (float) 45.75425, (float) 4.8591485));
			intersections.get((long) 25175791).addSegment(new Segment(intersections.get((long) 25175791),
					intersections.get((long) 25175778), (float) 69.979805, "Rue Danton"));
			intersections.get((long) 25175791).addSegment(new Segment(intersections.get((long) 25175791),
					intersections.get((long) 2117622723), (float) 136.00636, "Rue de l'Abondance"));

			if (intersections.size() != controller.getMap().getIntersections().size()) {
				fail("Unintented ParseFile error : intersections has more or less intersections than controller intersections");
			}

			for (Entry<Long, Intersection> entry : intersections.entrySet()) {
				if (!((controller.getMap().getIntersections().containsKey(entry.getKey()))
						&& (controller.getMap().getIntersections().get(entry.getKey()).equals(entry.getValue())))) {
					fail("Unintented ParseFile error");
				}
			}
		} catch (Exception e) {
			fail("Unintented ParseFile error");
		}
	}

	/**
	 * Nature of test (parseRequest) : The given file contains a missing attribute in the xml tag :
	 * <depot address="342873658"/> instead of
	 * <depot address="342873658" departureTime="8:0:0"/>
	 */
	@Test
	void parseRequestsMissingAttributeTest() {
		try {
			controller.parseFile(System.getProperty("user.dir").toString()
					+ "\\src\\tests\\fichiersXML2020\\requestsSmall1MissingAttribute.xml");
			fail("Missing tag attribute error not detected");
		} catch (Exception e) {
			System.out.println("success:" + e.toString());

		}

	}

	/**
	 * Nature of test (parseRequest) : The given file contains an aberrant value in a tag's
	 * attribute : (negative duration)
	 * <request pickupAddress="208769039" deliveryAddress="25173820" pickupDuration=
	 * "-180" deliveryDuration="240"/>
	 */
	@Test
	void parseRequestsAberrantValueTest() {
		try {
			controller.parseFile(System.getProperty("user.dir").toString()
					+ "\\src\\tests\\fichiersXML2020\\requestsSmall1AberrantValue.xml");
			fail("Aberrant value not detected");
		} catch (Exception e) {
			System.out.println("success:" + e.toString());
		}

	}

	/**
	 * Nature of test (parseRequest): The given file contains an nonexistent
	 * idIntersection : <depot address="1" departureTime="8:0:0"/>
	 */
	@Test
	void parseRequestsNonexistentIdIntersectionTest() {
		try {
			controller.parseFile(System.getProperty("user.dir").toString()
					+ "\\src\\tests\\fichiersXML2020\\requestsSmall1NonexistentIdIntersection.xml");
			fail("Non existant intersection error not detected");
		} catch (Exception e) {
			System.out.println("success:" + e.toString());
		}

	}

	/**
	 * Nature of test (parseRequest) : A basic success scenario for parsing a
	 * Request file.
	 */
	@Test
	void parseRequestsTest() {
		try {
			controller.parseFile(
					System.getProperty("user.dir").toString() + "\\src\\tests\\fichiersXML2020\\smallMap.xml");
			controller.setCurrentState(controller.getMapLoadedState());
			controller.parseFile(
					System.getProperty("user.dir").toString() + "\\src\\tests\\fichiersXML2020\\requestsSmall1.xml");
			assertNotNull(controller.getMap().getTour());
			assertNotNull(controller.getMap().getTour().getTourDeparture());
			assertNotNull(controller.getMap().getTour().getDepartureTime());
			assertFalse(controller.getMap().getTour().getTourRequests().isEmpty());

			Date departureTime = new SimpleDateFormat("H:m:s").parse("8:0:0");
			Stop depotTest = new Stop((long) 342873658, (float) 45.76038, (float) 4.8775625, (float) 0.0);
			depotTest.setOriginatedSegments(controller.getMap().getIntersectionById(342873658).getOriginatedSegments());
			Stop pickup = new Stop((long) 208769039, (float) 45.76069, (float) 4.8749375, (float) 180);
			pickup.setOriginatedSegments(controller.getMap().getIntersectionById(208769039).getOriginatedSegments());
			Stop delivery = new Stop((long) 25173820, (float) 45.749996, (float) 4.858258, (float) 240);
			delivery.setOriginatedSegments(controller.getMap().getIntersectionById(25173820).getOriginatedSegments());
			Request testRequest = new Request(pickup, delivery);
			ArrayList<Request> requestList = new ArrayList<>();
			requestList.add(testRequest);

			if (requestList.size() != controller.getMap().getTour().getTourRequests().size()) {
				fail("Unintented ParseFile error : test list has more or less requests than controller");
			}
			System.out.println(controller.getMap().getTour().getTourDeparture());
			System.out.println(depotTest);
			if (!controller.getMap().getTour().getDepartureTime().equals(departureTime)
					|| !controller.getMap().getTour().getTourDeparture().equals(depotTest)) {
				fail("Unintented ParseFile error : Departure times or departure stops or request list sizes not equal");
			}

			for (Request r : requestList) {
				if (!controller.getMap().getTour().getTourRequests().contains(r)) {
					fail("\"Unintented ParseFile error : request lists not equal");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unintented ParseFile error");
		}

	}

}
