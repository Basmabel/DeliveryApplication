package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import Controller.Controller;
import Model.Intersection;
import Model.MapBoundaries;
import Model.Request;
import Model.Segment;
import Model.Stop;

/**
 * DrawnMap is the panel where the map is drawn. Inside this Panel are drawn the
 * different pickup and delivery point. The tour computed is also displayed
 * inside this panel.
 * 
 * A DrawnMap is characterized by:
 * <ul>
 * <li>The controller that links the Back to GUI</li>
 * <li>A boolean that allows to know when to repaint the panel</li>
 * <li>A boolean that allows to know when a click is performed either in the
 * RoadMap or in the DrawnMap</li>
 * <li>A boolean that allows to know when a Tour is calculated</li>
 * <li>A boolean that allows to know when are adding a Request to the tour</li>
 * <li>A boolean that allows to know when are deleting a Request from the
 * tour</li>
 * <li>A boolean that allows to know if we selected a point by either clicking
 * on the drawnMap or the RoadMap</li>
 * <li>A boolean that allows to know if we selected an intersection when adding
 * a request to the tour</li>
 * <li>A boolean that allows to know if we selected an intersection for the
 * first stop (the pickup) when adding a request to the tour</li>
 * <li>A boolean that allows to know if we selected an existing stop as the
 * predecessor of the pickup previously selected adding a request to the
 * tour</li>
 * <li>A Stop that stocks the last stop on which we clicked</li>
 * <li>A Point that stocks the coordinates on the map of the point clicked</li>
 * <li>An intersection that stocks the last intersection clicked</li>
 * <li>An intersection that stocks an intersection when it's clicked</li>
 * <li>A Request that stocks the request clicked when the tour is not yet
 * computed</li>
 * <li>A GraphicalIntersection that Allows to show the order in the tour of a
 * stop</li>
 * <li>A list of GraphicalSegments that allow to draw the segments of map</li>
 * <li>A list of GraphicalIntersections that allow to draw the intersections of
 * map</li>
 * <li>A list of Colors that are used to paint our requests on the Map</li>
 * <li>A mapBounderies that sets the maximum and minimum coordinates of the
 * map</li>
 * <li>A variable that represents the zoom factor of the map</li>
 * <li>A MouseState that controls the mouse for the drawnMap panel</li>
 * </ul>
 * 
 * DrawnMap is created when after loading a map by parsing an XML map file. Once
 * the DrawnMap created, the intersection and segments are drawn.<br>
 * 
 * DrawnMap inherits observer in order to repaint easily when the map or the
 * tour have been modified
 * 
 * @author H4122
 *
 * @see MouseState
 * @see View.RoadMap
 * @see View.GraphicalIntersection
 * @see View.GraphicalSegment
 * @see Controller.Controller
 * @see Model.Map
 * @see Model.Tour
 * @see Model.Intersection
 * @see Model.Stop
 * @see Model.Segment
 * @see Model.Request
 * @see Model.MapBoundaries
 */

public class DrawnMap extends JPanel implements ObserverDP.Observer {

	private static final long serialVersionUID = 1L;

	private Controller controller;
	public boolean repaint = false;
	public boolean clicked = false;
	public boolean tourCalculated = false;
	public boolean add = false;
	public boolean delete = false;
	public boolean pointSelected = false;
	public boolean selectIntersection = true;
	public boolean selectFirstIntersection = true;
	public boolean selectFirstStop = true;
	public Stop lastStopClicked = new Stop();
	public Point pointClicked = new Point();
	public Intersection lastIntersectionClicked = new Intersection();
	public Intersection intersectionClicked = new Intersection();
	public Request requestClicked = new Request();
	GraphicalIntersection stopOver;
	private ArrayList<GraphicalSegment> graphicalSegments = new ArrayList<>();
	private ArrayList<GraphicalIntersection> graphicalIntersections = new ArrayList<>();
	private ArrayList<Color> colors = new ArrayList<>();
	private MapBoundaries mapBoundaries;
	private double zoomFactor = 1;

	/**
	 * MouseState is MouseListener class that defines new classes for the methods
	 * used
	 * 
	 * A MouseState is composed of:
	 * <ul>
	 * <li>A boolean that indicates if the the mouse is pressed</li>
	 * <li>A boolean that indicates if the the mouse is released</li>
	 * <li>A float that indicate the x-axis position of the mouse on the screen</li>
	 * <li>A float that indicate the y-axis position of the mouse on the screen</li>
	 * <li>A boolean that indicates if a segment is selected</li>
	 * <li>A boolean that indicates if an intersection is selected</li>
	 * <li>A boolean that indicates if we are performing a zoom</li>
	 * </ul>
	 * 
	 * MouseState is created to be an attribute of the DrawnMap class, and serve as
	 * it's own MouseListener.
	 * 
	 * MouseState inherits MouseInputListener and MouseWheelListener, in order to
	 * get the behavior of the mouse in the panel.
	 * 
	 * @author H4122
	 * 
	 * @see Model.Intersection
	 * @see Model.Segment
	 * @see View.DrawnMap
	 * @see Controller.Controller#parseMap(org.w3c.dom.Document)
	 *
	 */
	public class MouseState implements MouseInputListener, MouseWheelListener {

		private boolean pressed;
		private boolean released;
		private float positionX;
		private float positionY;
		private boolean segmentSelected = false;
		private boolean interSelected = false;
		@SuppressWarnings("unused")
		private boolean zoomer = false;

		/**
		 * Default Class Constructor
		 */
		public MouseState() {

		}

		public boolean getPressed() {
			return pressed;
		}

		public boolean getReleased() {
			return released;
		}

		public void resetReleased() {
			released = false;
		}

		/**
		 * Overrides mouseDragged method in class MouseInputListener.
		 * 
		 * Describes the behavior of the mouse when it's dragged
		 * 
		 * @param e The mouse event
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			pressed = true;
		}

		/**
		 * Overrides mouseMoved method in class MouseInputListener.
		 * 
		 * Describes the behavior of the mouse when it's moved
		 * 
		 * @param e The mouse event
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			segmentSelected = false;
			interSelected = false;
			positionX = e.getX();
			positionY = e.getY();
			for (GraphicalSegment graphicalSegment : graphicalSegments) {
				if (!segmentSelected && pDistance(positionX, positionY, graphicalSegment.getCoordinatesDeparture().x,
						graphicalSegment.getCoordinatesDeparture().y, graphicalSegment.getCoordinatesArrival().x,
						graphicalSegment.getCoordinatesArrival().y) <= 2) {
					graphicalSegment.setSelected(true);
					segmentSelected = true;
				} else {
					graphicalSegment.setSelected(false);
				}
			}
			if (add) {
				for (GraphicalIntersection graphicalIntersection : graphicalIntersections) {
					if (!interSelected && Math.abs(positionX - graphicalIntersection.getCoordinates().x) <= 2
							&& Math.abs(positionY - graphicalIntersection.getCoordinates().y) <= 2) {
						graphicalIntersection.setSelected(true);
						interSelected = true;
					} else {
						graphicalIntersection.setSelected(false);
					}
				}
			}

			if (!controller.getMap().getTour().getOrderedTravel().isEmpty()) {
				boolean over = false;
				int iterator = 0;
				for (Stop stop : controller.getMap().getTour().getOrderedTravel()) {
					if (Math.abs(getPositionOnScreen(stop.getLongitude(), stop.getLatitude()).x - positionX) <= 6
							&& Math.abs(
									getPositionOnScreen(stop.getLongitude(), stop.getLatitude()).y - positionY) <= 6) {
						stopOver = new GraphicalIntersection(stop,
								new Point(getPositionOnScreen(stop.getLongitude(), stop.getLatitude()).x,
										getPositionOnScreen(stop.getLongitude(), stop.getLatitude()).y));
						stopOver.setOver(true);
						stopOver.setNumber(iterator);
						over = true;
					}
					iterator++;
				}
				if (!over) {
					stopOver = null;
				}
			}
			repaint();

		}

		/**
		 * Sources : https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
		 * 
		 * Calculates the distance between the mouse of a segment
		 * 
		 * @param x  position of the mouse in the x-axis
		 * @param y  position of the mouse in the y-axis
		 * @param x1 position of the first end of a segment in the x-axis
		 * @param y1 position of the first end of a segment in the y-axis
		 * @param x2 position of the second end of a segment in the x-axis
		 * @param y2 position of the second end of a segment in the y-axis
		 * 
		 * @return A distance
		 * 
		 * @see Model.Segment
		 */
		private double pDistance(double x, double y, double x1, double y1, double x2, double y2) {
			double A = x - x1;
			double B = y - y1;
			double C = x2 - x1;
			double D = y2 - y1;

			double dot = A * C + B * D;
			double len_sq = C * C + D * D;
			double param = -1;
			if (len_sq != 0) // in case of 0 length line
				param = dot / len_sq;

			double xx, yy;

			if (param < 0) {
				xx = x1;
				yy = y1;
			} else if (param > 1) {
				xx = x2;
				yy = y2;
			} else {
				xx = x1 + param * C;
				yy = y1 + param * D;
			}

			double dx = x - xx;
			double dy = y - yy;
			return Math.sqrt(dx * dx + dy * dy);
		}

		/**
		 * Overrides mouseClicked method in class MouseInputListener.
		 * 
		 * Describes the behavior of the mouse when it's clicked
		 * 
		 * @param e The mouse event
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			pointClicked = new Point(e.getX(), e.getY());
			clicked = true;
			pointSelected = true;
			controller.getMenu().getRoadMap().repaint();
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		/**
		 * Overrides mousePressed method in class MouseInputListener.
		 * 
		 * Describes the behavior of the mouse when it's pressed
		 * 
		 * @param e The mouse event
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			pressed = true;
			released = false;
		}

		/**
		 * Overrides mouseReleased method in class MouseInputListener.
		 * 
		 * Describes the behavior of the mouse when it's released
		 * 
		 * @param e The mouse event
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			pressed = false;
			released = true;
		}

		/**
		 * Overrides mouseWheelMoved method in class MouseWheelListener.
		 * 
		 * Describes the behavior of the mouse when its wheel is moved
		 * 
		 * @param e The mouse event
		 */
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			zoomer = true;
			// Zoom in
			if (e.getWheelRotation() < 0 && zoomFactor < 3) {
				zoomFactor *= 1.1;
				repaint();
			}
			// Zoom out
			if (e.getWheelRotation() > 0 && zoomFactor > 1) {
				zoomFactor /= 1.1;
				repaint();
			}
		}
	}

	public MouseState mouseState = new MouseState();

	/**
	 * Class constructor. Creates the JPanel DrawnMap with a Controller given as a
	 * parameter. The constructor also initiates some attributes of the class such
	 * as mapBounderies graphicalIntersection, graphicalSegments and colors. In the
	 * constructor we also add MouseMotionListener and MouseWheelListener to the
	 * panel in order to detect the behaviors of the mouse
	 * 
	 * @param controller The controller of the application.
	 * 
	 * @see Controller.Controller
	 * @see Model.Segment
	 */
	public DrawnMap(Controller controller) {
		this.setSize(600, 600);
		this.controller = controller;
		this.controller.getMap().getTour().addObserver(this);
		this.controller.getMap().addObserver(this);
		mapBoundaries = controller.computeMapBoundaries();
		this.addMouseListener(mouseState);
		this.addMouseMotionListener(mouseState);
		this.addMouseWheelListener(mouseState);
		boolean sameSegment = false;

		for (Intersection intersection : controller.getMapIntersections().values()) {
			Point intersectionPoint = getPositionOnScreen(intersection.getLongitude(), intersection.getLatitude());
			graphicalIntersections.add(new GraphicalIntersection(intersection, intersectionPoint));
			for (Segment segment : intersection.getOriginatedSegments()) {
				sameSegment = false;
				Point startPoint = getPositionOnScreen(segment.getDepartureIntersection().getLongitude(),
						segment.getDepartureIntersection().getLatitude());
				Point endPoint = getPositionOnScreen(segment.getArrivalIntersection().getLongitude(),
						segment.getArrivalIntersection().getLatitude());
				for (GraphicalSegment graphicalSegment : graphicalSegments) {
					if (graphicalSegment.getCoordinatesDeparture().equals(endPoint)
							&& graphicalSegment.getCoordinatesArrival().equals(startPoint)) {
						sameSegment = true;
					}
				}
				if (!sameSegment) {
					graphicalSegments.add(new GraphicalSegment(segment, startPoint, endPoint, false));
				}
			}
		}
		colors.add(new Color(44, 117, 255));
		colors.add(new Color(223, 109, 20));
		colors.add(new Color(153, 122, 144));
		colors.add(new Color(146, 109, 39));
		colors.add(new Color(208, 192, 122));
		colors.add(new Color(253, 108, 158));
		colors.add(new Color(75, 0, 130));
		colors.add(new Color(223, 115, 255));
		colors.add(new Color(231, 168, 84));
		colors.add(new Color(96, 80, 220));
		colors.add(new Color(194, 247, 50));
		colors.add(new Color(102, 0, 255));
		colors.add(new Color(172, 30, 68));
		colors.add(new Color(64, 130, 109));
		colors.add(new Color(151, 223, 198));
		colors.add(new Color(148, 129, 43));
		colors.add(new Color(231, 62, 1));
		colors.add(new Color(127, 221, 76));
		colors.add(new Color(248, 142, 85));
		colors.add(new Color(115, 194, 251));
		colors.add(new Color(38, 196, 236));
		colors.add(new Color(157, 62, 12));
		colors.add(new Color(204, 85, 0));
		colors.add(new Color(90, 94, 107));
		colors.add(new Color(153, 122, 144));
		colors.add(new Color(239, 209, 83));
		colors.add(new Color(195, 180, 112));
		colors.add(new Color(223, 109, 20));
		colors.add(new Color(109, 7, 26));
		colors.add(new Color(129, 20, 83));
		colors.add(new Color(27, 79, 8));
	}

	public ArrayList<Color> getColors() {
		return colors;
	}

	public void setColors(ArrayList<Color> colors) {
		this.colors = colors;
	}

	/**
	 * Paints the different component into the panel. This method allows to paint
	 * the intersections and the segments of the map when there is no Request
	 * loaded. It paints, over the previously painted map, the requests when they
	 * are loaded. It paints the tour once it's computed. A repaint is called every
	 * time the mouse moves in the panel in order to print the name of the roads as
	 * well as the the order of the stops in the tour.
	 * 
	 * 
	 * @param g the Graphics that allow us to draw
	 * @see Model.Map
	 * @see Model.Request
	 * @see Model.Intersection
	 * @see Model.Tour
	 * @see Controller.Controller
	 * 
	 */
	public void paint(Graphics g) {
		super.paint(g);

		if (!pointClicked.equals(new Point())) {
			pointSelected = true;
		}

		Graphics2D graphics2D = (Graphics2D) g;

		// Set anti-alias!
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		this.setBackground(Color.white);

		graphics2D.translate(mouseState.positionX, mouseState.positionY);
		graphics2D.scale(zoomFactor, zoomFactor);
		graphics2D.translate(-mouseState.positionX, -mouseState.positionY);

		g.setColor(Color.black);

		for (GraphicalIntersection graphicalIntersection : graphicalIntersections) {
			graphicalIntersection.draw(g);
		}

		for (GraphicalSegment graphicalSegment : graphicalSegments) {
			graphicalSegment.draw(g);
		}

		if (stopOver != null) {
			stopOver.drawOver(g);
		}

		if (!controller.getMap().getTour().getOrderedTravel().isEmpty()) {
			for (Stop stop : controller.getMap().getTour().getOrderedTravel()) {
				for (Segment segment : stop.getNextPath().getPathSegments()) {

					Point startPoint = getPositionOnScreen(segment.getDepartureIntersection().getLongitude(),
							segment.getDepartureIntersection().getLatitude());
					Point endPoint = getPositionOnScreen(segment.getArrivalIntersection().getLongitude(),
							segment.getArrivalIntersection().getLatitude());

					GraphicalSegment graphicalSegment = new GraphicalSegment(segment, startPoint, endPoint, false);
					graphicalSegment.drawPath(g);

				}
			}
		}

		if (repaint) {
			g.setColor(Color.black);
			Point DeparturePoint = getPositionOnScreen(controller.getMap().getTour().getTourDeparture().getLongitude(),
					controller.getMap().getTour().getTourDeparture().getLatitude());
			GraphicalIntersection graphicalIntersection = new GraphicalIntersection(
					controller.getMap().getTour().getTourDeparture(), DeparturePoint);
			graphicalIntersection.drawDeparture(g, this);
			int iterator = 0;
			for (Request request : controller.getMap().getTour().getTourRequests()) {
				Color randomColor = colors.get(iterator);
				iterator++;
				if (iterator >= controller.getMap().getTour().getTourRequests().size()) {
					iterator = 0;
				}

				Point requestPickUpPoint = getPositionOnScreen(request.getPickupStop().getLongitude(),
						request.getPickupStop().getLatitude());
				GraphicalIntersection graphicalIntersectionPickup = new GraphicalIntersection(request.getPickupStop(),
						requestPickUpPoint);
				graphicalIntersectionPickup.drawStopRequest(g, "Pickup", randomColor);

				Point requestDeliveryPoint = getPositionOnScreen(request.getDeliveryStop().getLongitude(),
						request.getDeliveryStop().getLatitude());
				GraphicalIntersection graphicalIntersectionDelivery = new GraphicalIntersection(
						request.getDeliveryStop(), requestDeliveryPoint);
				graphicalIntersectionDelivery.drawStopRequest(g, "Delivery", randomColor);

				if ((clicked || pointSelected) && (!add && !delete && !tourCalculated)) {

					if ((Math.abs(getPositionOnScreen(request.getPickupStop().getLongitude(),
							request.getPickupStop().getLatitude()).x - pointClicked.x) < 6)
							&& (Math.abs(getPositionOnScreen(request.getPickupStop().getLongitude(),
									request.getPickupStop().getLatitude()).y - pointClicked.y) < 6)) {
						clicked = false;
						pointSelected = false;
						intersectionClicked = request.getPickupStop();
						requestClicked = request;
						controller.getMenu().getRoadMap().clickedOnMap(request);
						GraphicalIntersection graphicalIntersectionPickupBigger = new GraphicalIntersection(
								intersectionClicked, pointClicked);
						graphicalIntersectionPickupBigger.drawBiggerPoint(g, "Pickup");
						GraphicalIntersection graphicalIntersectionDeliveryBigger = new GraphicalIntersection(
								request.getDeliveryStop(), getPositionOnScreen(request.getDeliveryStop().getLongitude(),
										request.getDeliveryStop().getLatitude()));
						graphicalIntersectionDeliveryBigger.drawBiggerPoint(g, "Delivery");

					} else if ((Math.abs(getPositionOnScreen(request.getDeliveryStop().getLongitude(),
							request.getDeliveryStop().getLatitude()).x - pointClicked.x) < 6)
							&& (Math.abs(getPositionOnScreen(request.getDeliveryStop().getLongitude(),
									request.getDeliveryStop().getLatitude()).y - pointClicked.y) < 6)) {
						clicked = false;
						pointSelected = false;
						intersectionClicked = request.getDeliveryStop();
						requestClicked = request;
						controller.getMenu().getRoadMap().clickedOnMap(request);
						GraphicalIntersection graphicalIntersectionDeliveryBigger = new GraphicalIntersection(
								intersectionClicked, pointClicked);
						graphicalIntersectionDeliveryBigger.drawBiggerPoint(g, "Delivery");
						GraphicalIntersection graphicalIntersectionPickupBigger = new GraphicalIntersection(
								request.getPickupStop(), getPositionOnScreen(request.getPickupStop().getLongitude(),
										request.getPickupStop().getLatitude()));
						graphicalIntersectionPickupBigger.drawBiggerPoint(g, "Pickup");
					} else {
						for (Component tmp : this.controller.getMenu().getRoadMap().panel.getComponents()) {
							if (!controller.getMap().getTour().getOrderedTravel().isEmpty()) {
								int index = 0;
								Color c = controller.getMenu().getRoadMap().panel.getBackground();
								if (tmp.getBackground() == Color.red) {
									for (Request r : controller.getMap().getTour().getTourRequests()) {
										if (controller.getMenu().getRoadMap().getStopPanel().get(tmp)
												.equals(r.getPickupStop())
												|| controller.getMenu().getRoadMap().getStopPanel().get(tmp)
														.equals(r.getDeliveryStop())) {
											c = colors.get(index);
											tmp.setBackground(c);
										}
										index++;
									}

								}

							} else {
								int index = 0;
								Color c = controller.getMenu().getRoadMap().getBackground();
								if (tmp.getBackground() == Color.red) {
									for (Request r : controller.getMap().getTour().getTourRequests()) {
										if (controller.getMenu().getRoadMap().getRequestsPanel().get(tmp).equals(r)) {
											c = colors.get(index);
											tmp.setBackground(c);
										}
										index++;
									}
								}

							}

						}
					}
				}

				if ((clicked || pointSelected) && (!add && !delete && tourCalculated)) {

					if ((Math.abs(getPositionOnScreen(request.getPickupStop().getLongitude(),
							request.getPickupStop().getLatitude()).x - pointClicked.x) < 6)
							&& (Math.abs(getPositionOnScreen(request.getPickupStop().getLongitude(),
									request.getPickupStop().getLatitude()).y - pointClicked.y) < 6)) {
						clicked = false;
						pointSelected = false;
						intersectionClicked = request.getPickupStop();
						requestClicked = request;
						controller.getMenu().getRoadMap().clickedOnMap(request);
						GraphicalIntersection graphicalIntersectionPickupBigger = new GraphicalIntersection(
								intersectionClicked, pointClicked);
						graphicalIntersectionPickupBigger.drawBiggerPoint(g, "Pickup");
						@SuppressWarnings("unused")
						Stop stopClicked = (Stop) intersectionClicked;

					} else if ((Math.abs(getPositionOnScreen(request.getDeliveryStop().getLongitude(),
							request.getDeliveryStop().getLatitude()).x - pointClicked.x) < 6)
							&& (Math.abs(getPositionOnScreen(request.getDeliveryStop().getLongitude(),
									request.getDeliveryStop().getLatitude()).y - pointClicked.y) < 6)) {
						clicked = false;
						pointSelected = false;
						intersectionClicked = request.getDeliveryStop();
						requestClicked = request;
						controller.getMenu().getRoadMap().clickedOnMap(request);
						GraphicalIntersection graphicalIntersectionDeliveryBigger = new GraphicalIntersection(
								intersectionClicked, pointClicked);
						graphicalIntersectionDeliveryBigger.drawBiggerPoint(g, "Delivery");
						@SuppressWarnings("unused")
						Stop stopClicked = (Stop) intersectionClicked;

					} else {
						for (Component tmp : this.controller.getMenu().getRoadMap().panel.getComponents()) {
							if (!controller.getMap().getTour().getOrderedTravel().isEmpty()) {
								int index = 0;
								Color c = controller.getMenu().getRoadMap().panel.getBackground();
								if (tmp.getBackground() == Color.red) {
									for (Request r : controller.getMap().getTour().getTourRequests()) {
										if (controller.getMenu().getRoadMap().getStopPanel().get(tmp)
												.equals(r.getPickupStop())
												|| controller.getMenu().getRoadMap().getStopPanel().get(tmp)
														.equals(r.getDeliveryStop())) {
											c = colors.get(index);
											tmp.setBackground(c);
										}
										index++;
									}

								}

							} else {
								int index = 0;
								Color c = controller.getMenu().getRoadMap().getBackground();
								if (tmp.getBackground() == Color.red) {
									for (Request r : controller.getMap().getTour().getTourRequests()) {
										if (controller.getMenu().getRoadMap().getRequestsPanel().get(tmp).equals(r)) {
											c = colors.get(index);
											tmp.setBackground(c);
										}
										index++;
									}
								}

							}
						}
					}
				}
			}

			if (clicked && add) {
				Stop stopClicked = new Stop();
				if (selectIntersection) {
					for (Intersection inter : controller.getMap().getIntersections().values()) {
						if (Math.abs(
								getPositionOnScreen(inter.getLongitude(), inter.getLatitude()).x - pointClicked.x) < 6
								&& Math.abs(getPositionOnScreen(inter.getLongitude(), inter.getLatitude()).y
										- pointClicked.y) < 6) {
							intersectionClicked = inter;
							if (controller.getMap().getTour().getStopById(intersectionClicked.getId())
									.getLongitude() == 0) {
								if (intersectionClicked.getLatitude() != controller.getMap().getTour()
										.getTourDeparture().getLatitude()
										&& intersectionClicked.getLongitude() != controller.getMap().getTour()
												.getTourDeparture().getLongitude()) {
									if (selectFirstIntersection) {
										lastIntersectionClicked = intersectionClicked;
										selectFirstIntersection = false;
										selectIntersection = false;
									} else if (!lastIntersectionClicked.equals(intersectionClicked)) {
										lastIntersectionClicked = null;
										selectFirstIntersection = true;
										selectIntersection = false;
									} else {
										controller.getMenu().instructionsSetText("<html><body>The pickup stop and the delivery stop can't be at the same intersection.<br>"
												+ "Select another intersection. </html></body>");
									}
									break;
								} else {
									controller.getMenu().instructionsSetText("<html><body>The new stop can't be the tour departure.<br>"
											+ "Select another intersection. </html></body>");
								}
							}
						}
					}
					if (!selectIntersection) {
						controller.getMenu().instructionsSetText("Enter the stop duration");
						int duration = 180;
						do {
							JFrame frame = new JFrame();
							Object popUpDuration = JOptionPane.showInputDialog(frame, "Enter duration:", duration);
							if (popUpDuration != null) {
								duration = Integer.valueOf(popUpDuration.toString());
								if (duration < 180) {
									controller.getMenu()
											.instructionsSetText("The duration must be superior to 180 (3 minutes)");
								}
							}
						} while (duration < 180);
						stopClicked = new Stop(intersectionClicked.getId(), intersectionClicked.getLatitude(),
								intersectionClicked.getLongitude(), duration);
						stopClicked.setOriginatedSegments(intersectionClicked.getOriginatedSegments());
						controller.addRequestFinal(stopClicked);
					}
				} else {
					for (Stop stop : controller.getMap().getTour().getOrderedTravel()) {
						if (Math.abs(
								getPositionOnScreen(stop.getLongitude(), stop.getLatitude()).x - pointClicked.x) < 6
								&& Math.abs(getPositionOnScreen(stop.getLongitude(), stop.getLatitude()).y
										- pointClicked.y) < 6) {
							intersectionClicked = stop;
							if (controller.getMap().getTour().getStopById(intersectionClicked.getId())
									.getLongitude() != 0) {
								stopClicked = controller.getMap().getTour().getStopById(intersectionClicked.getId());
								if (selectFirstStop) {
									lastStopClicked = stopClicked;
									selectIntersection = true;
									selectFirstStop = false;
								} else {
									int indexFirst = 0;
									int indexSecond = 0;
									int iter = 0;
									for (Stop stopBis : controller.getMap().getTour().getOrderedTravel()) {
										if (stopBis.equals(lastStopClicked)) {
											indexFirst = iter;
										}
										if (stopBis.equals(stopClicked)) {
											indexSecond = iter;
										}
										iter++;
									}
									if (indexFirst <= indexSecond) {
										lastStopClicked = null;
										selectIntersection = true;
										selectFirstStop = true;
									} else {
										controller.getMenu().instructionsSetText("<html><body>The delivery predecessor can't be after the pickup predecessor.<br>"
												+ "Select a new delivery predecessor. </html></body>");
									}
								}
								break;
							}
						}
					}
					if (selectIntersection) {
						controller.addRequestFinal(stopClicked);
					}
				}

				clicked = false;
			}

			if (clicked && delete) {
				Request deleteRequest = new Request();
				int nbStop = 0;
				for (Request request : controller.getMap().getTour().getTourRequests()) {
					if ((Math.abs(getPositionOnScreen(request.getPickupStop().getLongitude(),
							request.getPickupStop().getLatitude()).x - pointClicked.x) < 6)
							&& (Math.abs(getPositionOnScreen(request.getPickupStop().getLongitude(),
									request.getPickupStop().getLatitude()).y - pointClicked.y) < 6)) {
						clicked = false;
						requestClicked = request;
						controller.getMenu().getRoadMap().clickedOnMap(request);
						GraphicalIntersection graphicalIntersectionPickupBigger = new GraphicalIntersection(
								intersectionClicked, pointClicked);
						graphicalIntersectionPickupBigger.drawBiggerPoint(g, "Pickup");
						deleteRequest = request;
						controller.getMenu().instructionsSetText("");
						nbStop++;
					}
					if ((Math.abs(getPositionOnScreen(request.getDeliveryStop().getLongitude(),
							request.getDeliveryStop().getLatitude()).x - pointClicked.x) < 6)
							&& (Math.abs(getPositionOnScreen(request.getDeliveryStop().getLongitude(),
									request.getDeliveryStop().getLatitude()).y - pointClicked.y) < 6)) {
						clicked = false;
						intersectionClicked = request.getDeliveryStop();
						requestClicked = request;
						controller.getMenu().getRoadMap().clickedOnMap(request);
						GraphicalIntersection graphicalIntersectionDeliveryBigger = new GraphicalIntersection(
								intersectionClicked, pointClicked);
						graphicalIntersectionDeliveryBigger.drawBiggerPoint(g, "Delivery");
						deleteRequest = request;
						nbStop++;
					}
				}
				if (nbStop == 1) {
					controller.deleteRequest(deleteRequest);
					controller.getMenu().instructionsSetText("");
				} else {
					controller.getMenu().instructionsSetText("<html><body>You selected multiple stops.<br>"
							+ "Select the other stop associate to the request you want to delete to confirm your choice. </html></body>");
				}
			}

			clicked = false;
		}

	}

	/**
	 * Allows to transform the longitude and the latitude of a stop to on screen
	 * coordinates
	 * 
	 * @param longitude the longitude of a stop
	 * @param latitude  the latitude of a stop
	 * 
	 * @return A Point with the coordinates on screen of the stop whose attributes
	 *         where given in parameters
	 */
	public Point getPositionOnScreen(double longitude, double latitude) {

		int mapWidth = 600;
		int mapHeight = 600;
		// offsets

		float mapLongitudeStart = mapBoundaries.getMinLong();
		float mapLatitudeStart = mapBoundaries.getMaxLat();

		float mapLongitude = mapBoundaries.getMaxLong() - mapLongitudeStart;
		float mapLatitude = mapLatitudeStart - mapBoundaries.getMinLat();

		longitude -= mapLongitudeStart;
		latitude = mapLatitudeStart - latitude;

		// set x & y using conversion
		int x = (int) (mapWidth * (longitude / mapLongitude));
		int y = (int) (mapHeight * (latitude / mapLatitude));

		return new Point(x, y);
	}

	/**
	 * Method linked to the pattern Observable Observer.
	 * 
	 * Allows the update of the view drawnMap whenever the observable ( Map or Tour)
	 * is modified.
	 * 
	 * @param observed An Observable to which Observer is subscribed.
	 * @param arg      An Object. Usually a String describing what has changed in
	 *                 the Observable's data.
	 */
	@Override
	public void update(ObserverDP.Observable observed, Object arg) {

		this.repaint();

	}

}
