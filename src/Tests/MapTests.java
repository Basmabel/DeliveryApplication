package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.*;

import Controller.Controller;
import Model.Path;
import Model.Request;
import Model.Segment;
import Model.Stop;

/**
 * MapTests is a class that tests different scenarios for the
 * computeShortestPath function of the Map class.
 * 
 * A MapTests is composed of:
 * <ul>
 * <li>A Controller</li>
 * </ul>
 * 
 * @author H4122
 * @see Model.Map#computeShortestPath(Stop, ArrayList)
 */

class MapTests {

	Controller controller;

	/**
	 * Before any test the controller attribute is reset, and the test files of map
	 * and requests are loaded.
	 */
	@BeforeEach
	void initController() throws Exception {
		controller = new Controller();
		controller.parseFile(
				System.getProperty("user.dir").toString() + "\\src\\tests\\fichiersXML2020\\smallMapDijkstraTest.xml");// DijkstraTest
		controller.setCurrentState(controller.getMapLoadedState());
		controller.parseFile(System.getProperty("user.dir").toString()
				+ "\\src\\tests\\fichiersXML2020\\requestsSmallDijkstraTest.xml");

		controller.setCurrentState(controller.getRequestsLoadedState());
	}

	/**
	 * Nature of test : computeShortestPath with tourDeparture as dStop -> should
	 * compute all shortest paths from the tour departure to all the pickup stops
	 */
	@Test
	void computeShortestPathTourDepartureTest() {

		ArrayList<Path> shrtPaths = new ArrayList<Path>();

		ArrayList<Segment> pathSegments1 = new ArrayList<Segment>();
		pathSegments1.add(controller.getMap().getIntersectionById(1)
				.getIntersectionSegment(controller.getMap().getIntersectionById(2)));

		Path p1 = new Path(controller.getMap().getTour().getTourDeparture(),
				controller.getMap().getTour().getStopById(2), pathSegments1, 1 * (float) (3600.0 / 15000.0));
		shrtPaths.add(p1);

		ArrayList<Segment> pathSegments2 = new ArrayList<Segment>();
		pathSegments2.add(controller.getMap().getIntersectionById(1)
				.getIntersectionSegment(controller.getMap().getIntersectionById(2)));
		pathSegments2.add(controller.getMap().getIntersectionById(2)
				.getIntersectionSegment(controller.getMap().getIntersectionById(3)));
		pathSegments2.add(controller.getMap().getIntersectionById(3)
				.getIntersectionSegment(controller.getMap().getIntersectionById(4)));

		Path p2 = new Path(controller.getMap().getTour().getTourDeparture(),
				controller.getMap().getTour().getStopById(4), pathSegments2, 3 * (float) (3600.0 / 15000.0));
		shrtPaths.add(p2);

		Stop dstop = controller.getMap().getTour().getTourDeparture();
		ArrayList<Stop> listStop = new ArrayList<Stop>();
		for (Request r : controller.getMap().getTour().getTourRequests()) {
			listStop.add(r.getPickupStop());
		}
		ArrayList<Path> shortestPaths = controller.getMap().computeShortestPath(dstop, listStop);

		assertTrue(shortestPaths.equals(shrtPaths));
	}

	/**
	 * Nature of test : computeShortestPath with a pickup as dStop -> should compute
	 * all shortest paths from this pickup Stop to all the stops (except
	 * tourDeparture)
	 */
	@Test
	void computeShortestPathPickUpStopTest() {
		ArrayList<Path> shrtPaths = new ArrayList<Path>();

		ArrayList<Segment> pathSegments6 = new ArrayList<Segment>();
		pathSegments6.add(controller.getMap().getIntersectionById(2)
				.getIntersectionSegment(controller.getMap().getIntersectionById(3)));
		pathSegments6.add(controller.getMap().getIntersectionById(3)
				.getIntersectionSegment(controller.getMap().getIntersectionById(5)));
		pathSegments6.add(controller.getMap().getIntersectionById(5)
				.getIntersectionSegment(controller.getMap().getIntersectionById(6)));
		Path p6 = new Path(controller.getMap().getTour().getStopById(2), controller.getMap().getTour().getStopById(6),
				pathSegments6, 3 * (float) (3600.0 / 15000.0));
		shrtPaths.add(p6);

		ArrayList<Segment> pathSegments4 = new ArrayList<Segment>();
		pathSegments4.add(controller.getMap().getIntersectionById(2)
				.getIntersectionSegment(controller.getMap().getIntersectionById(3)));
		pathSegments4.add(controller.getMap().getIntersectionById(3)
				.getIntersectionSegment(controller.getMap().getIntersectionById(4)));
		Path p4 = new Path(controller.getMap().getTour().getStopById(2), controller.getMap().getTour().getStopById(4),
				pathSegments4, 2 * (float) (3600.0 / 15000.0));
		shrtPaths.add(p4);

		ArrayList<Segment> pathSegments8 = new ArrayList<Segment>();
		pathSegments8.add(controller.getMap().getIntersectionById(2)
				.getIntersectionSegment(controller.getMap().getIntersectionById(1)));
		pathSegments8.add(controller.getMap().getIntersectionById(1)
				.getIntersectionSegment(controller.getMap().getIntersectionById(8)));
		Path p8 = new Path(controller.getMap().getTour().getStopById(2), controller.getMap().getTour().getStopById(8),
				pathSegments8, 4 * (float) (3600.0 / 15000.0));
		shrtPaths.add(p8);

		Stop dstop = controller.getMap().getTour().getStopById(2);
		ArrayList<Stop> listStop = new ArrayList<Stop>();
		for (Request r : controller.getMap().getTour().getTourRequests()) {
			if (r.getPickupStop().getId() != dstop.getId()) {
				listStop.add(r.getPickupStop());
			}
			listStop.add(r.getDeliveryStop());
		}
		ArrayList<Path> shortestPaths = controller.getMap().computeShortestPath(dstop, listStop);
		assertTrue(shortestPaths.equals(shrtPaths));
	}

	/**
	 * Nature of test : computeShortestPath with a deliveryStop as dStop -> should
	 * compute all shortest paths from this delivery Stop to all the stops (except
	 * its pickup stop) + tourDeparture
	 */
	@Test
	void computeShortestPathDeliveryStopTest() {
		ArrayList<Path> shrtPaths = new ArrayList<Path>();

		ArrayList<Segment> pathSegments8 = new ArrayList<Segment>();
		pathSegments8.add(controller.getMap().getIntersectionById(6)
				.getIntersectionSegment(controller.getMap().getIntersectionById(5)));
		pathSegments8.add(controller.getMap().getIntersectionById(5)
				.getIntersectionSegment(controller.getMap().getIntersectionById(3)));
		pathSegments8.add(controller.getMap().getIntersectionById(3)
				.getIntersectionSegment(controller.getMap().getIntersectionById(2)));
		pathSegments8.add(controller.getMap().getIntersectionById(2)
				.getIntersectionSegment(controller.getMap().getIntersectionById(1)));
		Path p8 = new Path(controller.getMap().getTour().getStopById(6),
				controller.getMap().getTour().getTourDeparture(), pathSegments8, 4 * (float) (3600.0 / 15000.0));
		shrtPaths.add(p8);

		ArrayList<Segment> pathSegments4 = new ArrayList<Segment>();
		pathSegments4.add(controller.getMap().getIntersectionById(6)
				.getIntersectionSegment(controller.getMap().getIntersectionById(5)));
		pathSegments4.add(controller.getMap().getIntersectionById(5)
				.getIntersectionSegment(controller.getMap().getIntersectionById(3)));
		pathSegments4.add(controller.getMap().getIntersectionById(3)
				.getIntersectionSegment(controller.getMap().getIntersectionById(4)));
		Path p4 = new Path(controller.getMap().getTour().getStopById(6), controller.getMap().getTour().getStopById(4),
				pathSegments4, 3 * (float) (3600.0 / 15000.0));
		shrtPaths.add(p4);

		ArrayList<Segment> pathSegments6 = new ArrayList<Segment>();
		pathSegments6.add(controller.getMap().getIntersectionById(6)
				.getIntersectionSegment(controller.getMap().getIntersectionById(7)));
		pathSegments6.add(controller.getMap().getIntersectionById(7)
				.getIntersectionSegment(controller.getMap().getIntersectionById(8)));
		Path p6 = new Path(controller.getMap().getTour().getStopById(6), controller.getMap().getTour().getStopById(8),
				pathSegments6, 3 * (float) (3600.0 / 15000.0));
		shrtPaths.add(p6);

		Stop dstop = controller.getMap().getTour().getStopById(6);
		ArrayList<Stop> listStop = new ArrayList<Stop>();
		listStop.add(controller.getMap().getTour().getTourDeparture());
		for (Request r : controller.getMap().getTour().getTourRequests()) {
			if (r.getDeliveryStop().getId() != dstop.getId()) {
				listStop.add(r.getPickupStop());
				listStop.add(r.getDeliveryStop());
			}
		}
		ArrayList<Path> shortestPaths = controller.getMap().computeShortestPath(dstop, listStop);
		assertTrue(shortestPaths.equals(shrtPaths));
	}
}
