package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import Controller.Controller;
import Model.Request;
import Model.Stop;
/**
 * RoadMap is the class that displays the list of requests and their stops.
 * When the tour is computed, it displays all the stops in the visiting order.
 * The RoadMap is associate to the DrawnMap.
 * 
 * A RoadMap is composed of:
 * <ul>
 * <li>A Controller.</li>
 * <li>A DrawnMap which displays the map.</li>
 * <li>A JTextPane containing the texts.</li>
 * <li>A JScrollPane which contains the previous JTextPane to allow to scroll into the list.</li>
 * <li>A JPanel containing the JScrollPane.</li>
 * <li>A String array that contains the text of the list of requests/stops . </li>
 * <li>A Map&lt;JPanel, Request&gt; requestsPanel that links a panel to a request. </li>
 * <li>A  Map&lt;JPanel, Stop&gt; stopPanel that links a panel to a stop. </li>
 * </ul>
 * 
 * The menu is called when a map is loaded.<br/>
 * 
 * 
 * @author H4122
 * 
 * @see Model.Stop
 * @see Model.Request
 * @see Model.Tour
 * @see View.DrawnMap
 * @see View.RoadMap
 * @see Controller.Controller
 */

public class RoadMap extends JPanel implements MouseListener, ObserverDP.Observer {

	private static final long serialVersionUID = 1L;

	private Controller controller;
	private DrawnMap drawnMap;
	private JTextPane jTextPane = new JTextPane();
	private JScrollPane scrollPane = new JScrollPane();
	public JPanel panel = new JPanel();
	private String array = "";
	private Map<JPanel, Request> requestsPanel = new LinkedHashMap<>();
	private Map<JPanel, Stop> stopPanel = new LinkedHashMap<>();


	/**
	 * Class constructor.
	 * Creates a roadMap that can interacts with the drawnMap.
	 * Sets the dimensions of the roadmap and its components (scrollPane and jTextPane).
	 * Adds a mouse listener in order to react to the clicks of the user.
	 * 
	 * @param controller It tells the view what to do depending on the user's actions.
	 * @param drawnMap It is associate to a drawnMap.
	 * 
	 */
	public RoadMap(Controller controller, DrawnMap drawnMap) {
		this.setSize(600, 700);
		this.controller = controller;
		this.drawnMap = drawnMap;
		this.controller.getMap().getTour().addObserver(this);
		this.controller.getMap().addObserver(this);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		addMouseListener(this);
		Dimension d = new Dimension(450, 600);
		jTextPane.setPreferredSize(d);
		jTextPane.setEditable(false);
		scrollPane.setPreferredSize(d);
		scrollPane.setViewportView(panel);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Requests's List"));
		scrollPane.setVisible(true);
		this.add(scrollPane);

	}
	
	
	public Controller getController() {
		return controller;
	}

	public Map<JPanel, Request> getRequestsPanel() {
		return requestsPanel;
	}

	public void setRequestsPanel(Map<JPanel, Request> requestsPanel) {
		this.requestsPanel = requestsPanel;
	}

	public Map<JPanel, Stop> getStopPanel() {
		return stopPanel;
	}

	public void setStopPanel(Map<JPanel, Stop> stopPanel) {
		this.stopPanel = stopPanel;
	}
	
	/**
	 * Loads the requests in the panel.
	 * It displays a list of requests with a different color for each request.
	 * A request has a pickup and a delivery stops with their coordinates.
	 * 
	 * When the tour is computed, it displays the list of stops in the visiting order.
	 * The stops which are associate to the same request has the same color.
	 * It displays the closest road and the arrival and departure time of each stops.
	 * It also shows the tour departure with the departure and arrival times of the tour.
	 * 
	 */

	@SuppressWarnings("deprecation")
	public void LoadRequestsInPanel() {
		panel.removeAll();

		int iterator = 0;
		if (!controller.getMap().getTour().getOrderedTravel().isEmpty()) {
			panel.setPreferredSize(new Dimension(400, 90 * controller.getMap().getTour().getOrderedTravel().size()));
			drawnMap.tourCalculated = true;

			array = "<html><body>Tour Departure: <br>";
			array += "Near to: "
					+ controller.getMap().getTour().getTourDeparture().getOriginatedSegments().get(0).getName();
			array += "<br>Departure Time: " + controller.getMap().getTour().getDepartureTime().getHours() + ":"
					+ controller.getMap().getTour().getDepartureTime().getMinutes() + ":"
					+ controller.getMap().getTour().getDepartureTime().getSeconds();

			JPanel dep = new JPanel();
			JLabel labelDep = new JLabel(array);
			dep.addMouseListener(this);
			dep.add(labelDep);
			panel.add(dep);
			for (int i = 1; i < controller.getMap().getTour().getOrderedTravel().size() - 1; i++) {
				Stop stop = controller.getMap().getTour().getOrderedTravel().get(i);
				array = "<html><body>Stop °" + (iterator + 1) + ": <br>";
				array += "Near to: " + stop.getOriginatedSegments().get(0).getName();
				if (stop.getOriginatedSegments().size() > 1)
					array += "<br> And to: " + stop.getOriginatedSegments().get(1).getName();

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(stop.getArrivalTime());
				calendar.add(Calendar.SECOND, (int) stop.getStopDuration());
				Date arrivalTime = calendar.getTime();

				array += "<br>Arrival Time: " + stop.getArrivalTime().getHours() + ":"
						+ stop.getArrivalTime().getMinutes() + ":" + stop.getArrivalTime().getSeconds()
						+ ", Departure Time: " + arrivalTime.getHours() + ":" + arrivalTime.getMinutes() + ":"
						+ stop.getArrivalTime().getSeconds() + "<br>";

				Color c = panel.getBackground();
				int index = 0;
				for (Request r : controller.getMap().getTour().getTourRequests()) {
					if (stop.equals(r.getPickupStop()) || stop.equals(r.getDeliveryStop())) {
						c = drawnMap.getColors().get(index);
					}
					index++;

				}

				JPanel pane = new JPanel();
				pane.setBackground(c);
				JLabel label = new JLabel(array);
				label.setForeground(Color.white);
				pane.addMouseListener(this);
				pane.add(label);
				stopPanel.put(pane, stop);
				panel.add(pane);
				iterator++;

			}
			array = "<html><body>Tour Arrival: <br>";
			array += "Near to: "
					+ controller.getMap().getTour().getTourDeparture().getOriginatedSegments().get(0).getName();
			array += "<br>Arrival Time: " + controller.getMap().getTour().getArrivalTime().getHours() + ":"
					+ controller.getMap().getTour().getArrivalTime().getMinutes() + ":"
					+ controller.getMap().getTour().getArrivalTime().getSeconds();

			JPanel arrival = new JPanel();
			JLabel labelArrival = new JLabel(array);
			arrival.addMouseListener(this);
			arrival.add(labelArrival);
			panel.add(arrival);
		} else {
			if (array != "") {
				array = "";
			}
			ArrayList<Request> ListRequests = this.controller.getMap().getTour().getTourRequests();
			panel.setPreferredSize(new Dimension(400, 90 * ListRequests.size()));
			for (Request request : ListRequests) {

				Color c = drawnMap.getColors().get(iterator);
				array = "<html><body>Request ° " + (iterator + 1) + ": <br><br>";
				array += "Pickup Point : Longitude :" + request.getPickupStop().getLongitude() + ", Latitude :"
						+ request.getPickupStop().getLatitude() + "<br>";
				array += "Delivery Point : Longitude :" + request.getDeliveryStop().getLongitude() + ", Latitude :"
						+ request.getDeliveryStop().getLatitude() + "<br></body></html>";

				JPanel pane = new JPanel();
				pane.setBackground(c);
				JLabel label = new JLabel(array);
				label.setForeground(Color.white);
				pane.addMouseListener(this);
				pane.add(label);

				requestsPanel.put(pane, request);
				panel.add(pane);
				iterator++;

			}
		}
		drawnMap.repaint = true;

	}
	
	/**
	 * Handles the click on a request on the drawnMap.
	 * It will set the background of the request selected in red on the roadMap.
	 * 
	 * @param request selected in the drawnMap.
	 * 
	 */

	public void clickedOnMap(Request request) {
		for (Component tmp : panel.getComponents()) {
			if (!controller.getMap().getTour().getOrderedTravel().isEmpty()) {
				int index = 0;
				Color c = panel.getBackground();
				if (tmp.getBackground() == Color.red) {
					for (Request r : controller.getMap().getTour().getTourRequests()) {
						if (stopPanel.get(tmp).equals(r.getPickupStop())
								|| stopPanel.get(tmp).equals(r.getDeliveryStop())) {
							c = drawnMap.getColors().get(index);
							tmp.setBackground(c);
						}
						index++;
					}
					
				}

			} else {
				int index = 0;
				Color c = panel.getBackground();
				if (tmp.getBackground() == Color.red) {
					for (Request r : controller.getMap().getTour().getTourRequests()) {
						if (requestsPanel.get(tmp).equals(r)) {
							c = drawnMap.getColors().get(index);
							tmp.setBackground(c);
						}
						index++;
					}
				}

			}

		}

		for (JPanel tmp : requestsPanel.keySet()) {
			Request requests = requestsPanel.get(tmp);
			if (requests == drawnMap.requestClicked) {
				tmp.setBackground(Color.red);
			}
		}

		for (JPanel tmp : stopPanel.keySet()) {
			Stop stop = stopPanel.get(tmp);
			if (stop == drawnMap.intersectionClicked) {
				tmp.setBackground(Color.red);
			}
		}

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
		panel.removeAll();
		LoadRequestsInPanel();
		revalidate();
	}
	
	/**
	 * Handles the click on the roadMap.
	 * It will set the background of the request selected in red.
	 * At the same time, it associates the pointClicked to the request selected in the drawnMap.
	 * In that way, the request will be highlighted on the drawnMap as well.
	 * 
	 * @param arg0 is a mouseEvent.
	 * 
	 */

	@Override
	public void mouseClicked(MouseEvent arg0) {
		for (Component tmp : panel.getComponents()) {
			if (!controller.getMap().getTour().getOrderedTravel().isEmpty()) {
				int index = 0;
				Color c = panel.getBackground();
				if (tmp.getBackground() == Color.red) {
					for (Request r : controller.getMap().getTour().getTourRequests()) {
						if (stopPanel.get(tmp).equals(r.getPickupStop())
								|| stopPanel.get(tmp).equals(r.getDeliveryStop())) {
							c = drawnMap.getColors().get(index);
							tmp.setBackground(c);
						}
						index++;
					}
					
				}

			} else {
				int index = 0;
				Color c = panel.getBackground();
				if (tmp.getBackground() == Color.red) {
					for (Request r : controller.getMap().getTour().getTourRequests()) {
						if (requestsPanel.get(tmp).equals(r)) {
							c = drawnMap.getColors().get(index);
							tmp.setBackground(c);
						}
						index++;
					}
				}

			}
		}
		drawnMap.clicked = true;
		drawnMap.pointSelected = true;
		if (!controller.getMap().getTour().getOrderedTravel().isEmpty()) {
			drawnMap.tourCalculated = true;
			JPanel tmp = (JPanel) arg0.getComponent();
			drawnMap.pointClicked = drawnMap.getPositionOnScreen(stopPanel.get(tmp).getLongitude(),
					stopPanel.get(tmp).getLatitude());
			drawnMap.repaint();
			LoadRequestsInPanel();
			revalidate();
			arg0.getComponent().setBackground(Color.red);
		} else {
			JPanel tmp = (JPanel) arg0.getComponent();
			drawnMap.pointClicked = drawnMap.getPositionOnScreen(requestsPanel.get(tmp).getPickupStop().getLongitude(),
					requestsPanel.get(tmp).getPickupStop().getLatitude());
			drawnMap.repaint();
			LoadRequestsInPanel();
			revalidate();
			arg0.getComponent().setBackground(Color.red);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
