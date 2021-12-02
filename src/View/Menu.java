package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import Controller.Controller;
import Model.Segment;
import Model.Stop;

/**
 * Menu is the class that displays the main JFrame that contains the drawn map
 * and the road map. It contains all the buttons allowing the interactions with
 * the user such as load request, compute a tour, modifications on a tour etc.
 * 
 * A menu is composed of:
 * <ul>
 * <li>A Controller.</li>
 * <li>A DrawnMap which displays the map.</li>
 * <li>A RoadMap which displays the list of the requests or stops of the
 * tour.</li>
 * <li>LoadRequests is a frame allowing to load an xml file to load the
 * requests.</li>
 * <li>A JPanel legend that contains the instructions for the user.</li>
 * <li>A JLabel instructions are useful to understand what to do next.</li>
 * <li>A JToolBar menu that contains all the buttons.</li>
 * <li>A JButton undo to undo a modification.</li>
 * <li>A JButton redo to redo a modification.</li>
 * <li>A JButton cancel to cancel when doing a modification.</li>
 * <li>A JButton download to download the roadmap.</li>
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
 * @see View.LoadRequests
 * @see Controller.Controller
 */

public class Menu extends JFrame {

	private Controller controller;
	private DrawnMap drawnMap;
	private RoadMap roadMap;
	private LoadRequests loadRequestsFrame;
	private JPanel legend = new JPanel();
	private JLabel instructions = new JLabel();
	private JToolBar menu = new JToolBar();
	private JButton undo = new JButton("Undo");
	private JButton redo = new JButton("Redo");
	private JButton cancel = new JButton("Cancel");
	private JButton download = new JButton("DownLoad Road Map");
	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor.
	 * Creates a menu and sets its parameters (size, background ..).
	 * It also creates and sets the components such as the drawnMap, the roadMap and the legend.
	 * 
	 * @param controller It tells the view what to do depending on the user's
	 *                   actions.
	 * @param xmlFile    It contains all the data of the map and is used to draw the
	 *                   graphical map.
	 * 
	 */
	public Menu(Controller controller, String xmlFile) {
		super("Deliver'IF");
		this.setSize(1100, 750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setResizable(false);
		this.controller = controller;
		this.loadRequestsFrame = null;
		this.drawnMap = new DrawnMap(controller);
		this.roadMap = new RoadMap(controller, drawnMap);
		this.legend.setBackground(Color.white);
		this.legend.setLayout(new BoxLayout(legend, BoxLayout.LINE_AXIS));
		this.legend.setPreferredSize(new Dimension(1100, 70));
		this.instructions.setText("You can load an xml file to display the requests");
		instructions.setFont(new Font("Arial", Font.PLAIN, 20));
		String MapLegend = "<html><body><ul><li>The circle is a Pickup Point, the square is a Delivery Point.</li> <li> A circle and a square that are the same color belong to the same request. </li> <li> the pin corresponds to the Tour departure</li></ul></html></body>";
		JLabel legendMap = new JLabel(MapLegend);
		this.legend.add(legendMap);
		this.legend.add(instructions);
		this.legend.add(new JLabel("                    "));
		add(tool(), BorderLayout.NORTH);
		add(this.drawnMap, BorderLayout.CENTER);
		add(this.legend, BorderLayout.SOUTH);
		add(this.roadMap, BorderLayout.WEST);
		repaint();
		setVisible(true);
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public DrawnMap getDrawnMap() {
		return drawnMap;
	}

	public void setDrawnMap(DrawnMap drawnMap) {
		this.drawnMap = drawnMap;
	}

	public RoadMap getRoadMap() {
		return roadMap;
	}

	public void setRoadMap(RoadMap roadMap) {
		this.roadMap = roadMap;
	}

	public LoadRequests getLoadRequestsFrame() {
		return this.loadRequestsFrame;
	}

	public void setLoadRequestsFrame(LoadRequests loadRequestsFrame) {
		this.loadRequestsFrame = loadRequestsFrame;
	}

	public void instructionsSetText(String text) {
		instructions.setText(text);
	}

	/**
	 * Set the contents of the JToolBar menu with all the buttons.
	 * 
	 * @return The tool bar containing all the buttons.
	 */

	public JToolBar tool() {
		JButton loadRequests = new JButton("Load Requests");
		loadRequests.setBackground(new Color(159, 226, 191));
		loadRequests.addActionListener(new ActionListener() {
			/**
			 * Reset the last pointClicked. Reset the orderedTravel. Load new requests if
			 * the conditions are satisfied.
			 * 
			 * @param e is an ActionEvent. Here, a click on the button.
			 * 
			 * @see View.DrawnMap#poinrClicked
			 * @see View.LoadRequests
			 * @see Model.Tour#orderedTravel
			 * @see Controller.controller#orderedTravel
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				drawnMap.pointClicked = new Point();
				loadRequestsFrame = new LoadRequests(controller, drawnMap);
				controller.getMap().getTour().setOrderedTravel(new ArrayList<Stop>());
				drawnMap.tourCalculated = false;

			}
		});

		JButton computeTour = new JButton("Compute a Tour");
		computeTour.setBackground(new Color(144, 238, 144));
		computeTour.addActionListener(new ActionListener() {
			/**
			 * Reset the last pointClicked. Compute a tour if the conditions are satisfied.
			 * Repaint in order to display the tour.
			 * 
			 * @param e is an ActionEvent. Here, a click on the button.
			 * 
			 * @see View.DrawnMap#pointClicked
			 * @see Controller.controller#computeTour
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!controller.getMap().getTour().getTourRequests().isEmpty()) {
					instructionsSetText("Please wait. The tour is calculating.");
				}
				controller.computeTour();
				drawnMap.pointClicked = new Point();
				drawnMap.repaint();
			}
		});

		cancel.setBackground(new Color(60, 179, 113));
		cancel.addActionListener(new ActionListener() {

			/**
			 * Reset the last pointClicked. Cancel the action when doing a modification (add
			 * or delete)
			 * 
			 * @param e is an ActionEvent. Here, a click on the button.
			 * 
			 * @see View.DrawnMap#pointClicked
			 * @see Controller.controller#cancel
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				drawnMap.pointClicked = new Point();
				controller.cancel();
			}
		});

		JButton addRequest = new JButton("Add a request");
		addRequest.setBackground(new Color(60, 179, 113));
		addRequest.addActionListener(new ActionListener() {

			/**
			 * Reset the last pointClicked. Change the state in an add state in order to add
			 * a request.
			 * 
			 * @param e is an ActionEvent. Here, a click on the button.
			 * 
			 * @see View.DrawnMap#pointClicked
			 * @see Controller.controller#changeStateAdd
			 */

			@Override
			public void actionPerformed(ActionEvent e) {
				drawnMap.pointClicked = new Point();
				drawnMap.add = true;
				controller.changeStateAdd();
			}
		});

		JButton deleteRequest = new JButton("Delete a request");
		deleteRequest.setBackground(new Color(119, 198, 110));
		deleteRequest.addActionListener(new ActionListener() {

			/**
			 * Reset the last pointClicked. Change the state in an delete state in order to
			 * delete a request.
			 * 
			 * @param e is an ActionEvent. Here, a click on the button.
			 * 
			 * @see View.DrawnMap#pointClicked
			 * @see Controller.controller#changeStateAdd
			 */

			@Override
			public void actionPerformed(ActionEvent e) {
				drawnMap.pointClicked = new Point();
				drawnMap.delete = true;
				controller.changeStateDelete();
			}
		});

		undo.setBackground(new Color(159, 226, 191));
		undo.addActionListener(new ActionListener() {

			/**
			 * Reset the last pointClicked. Undo the last modification.
			 * 
			 * @param e is an ActionEvent. Here, a click on the button.
			 * 
			 * @see View.DrawnMap#pointClicked
			 * @see Controller.controller#undo
			 */

			@Override
			public void actionPerformed(ActionEvent e) {
				drawnMap.pointClicked = new Point();
				controller.undo();
			}
		});

		redo.setBackground(new Color(144, 238, 144));
		redo.addActionListener(new ActionListener() {

			/**
			 * Reset the last pointClicked. Redo the last modification.
			 * 
			 * @param e is an ActionEvent. Here, a click on the button.
			 * 
			 * @see View.DrawnMap#pointClicked
			 * @see Controller.controller#redo
			 */

			@Override
			public void actionPerformed(ActionEvent e) {
				drawnMap.pointClicked = new Point();
				controller.redo();
			}
		});

		download.setBackground(new Color(31, 160, 85));
		download.addActionListener(new ActionListener() {

			/**
			 * Download an html file containing the road map.
			 * 
			 * @param e is an ActionEvent. Here, a click on the button.
			 * 
			 */

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					generateHtmlFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		JButton loadNewMap = new JButton("Load New Map");
		loadNewMap.setBackground(new Color(119, 198, 110));
		loadNewMap.addActionListener(new ActionListener() {

			/**
			 * Open a new window to load a new map.
			 * 
			 * @param e is an ActionEvent. Here, a click on the button.
			 * 
			 * @see Controller.controller#loadNewMap
			 */

			@Override
			public void actionPerformed(ActionEvent e) {
				int input = JOptionPane.showConfirmDialog(null,
						"Do you want to lose all current progress and load a new map?");
				if (input == 0) {
					controller.loadNewMap();
					controller.getMap().getTour().setOrderedTravel(new ArrayList<Stop>());
					Menu.this.dispatchEvent(
							new java.awt.event.WindowEvent(Menu.this, java.awt.event.WindowEvent.WINDOW_CLOSING));
					drawnMap.tourCalculated = false;
				}

			}
		});

		menu.add(loadNewMap);
		menu.add(loadRequests);
		menu.add(computeTour);
		menu.add(addRequest);
		menu.add(deleteRequest);
		menu.add(undo);
		menu.add(redo);
		menu.add(cancel);
		menu.add(download);
		cancel.setVisible(false);
		undo.setEnabled(false);
		redo.setEnabled(false);
		download.setVisible(false);

		return menu;
	}

	/**
	 * Generate an html file containing the road map with all the stops, the paths,
	 * the departure and arrival times.
	 * 
	 */

	@SuppressWarnings("deprecation")
	public void generateHtmlFile() throws IOException {
		File f = new File("Road_Map.html");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write("<html><body><h1>Delivery Road Map :</h1>");
		bw.write("<h2> Tour Departure </h2>");
		bw.write("<p> Near road : <br> <ul>");
		for (Segment seg : roadMap.getController().getMap().getTour().getTourDeparture().getOriginatedSegments()) {
			bw.write("<li>" + seg.getName() + "</li>");
		}
		bw.write("</ul><p>");
		bw.write("<h4> Departure Time: </h4><ul><li>"+ roadMap.getController().getMap().getTour().getDepartureTime().getHours()+":"
				+ roadMap.getController().getMap().getTour().getDepartureTime().getMinutes() + ":"
				+ roadMap.getController().getMap().getTour().getDepartureTime().getSeconds() + "</ul></li>");
		int index = 1;
		for (int ind= 1 ; ind<roadMap.getController().getMap().getTour().getOrderedTravel().size()-1; ind++) {
			Stop stop= roadMap.getController().getMap().getTour().getOrderedTravel().get(ind);
			bw.write("<h3> Stop n°" + index + "</h3>");
			bw.write("<p> Road to follow : <br> <ul>");
			for (int i = 0; i < stop.getNextPath().getPathSegments().size() - 1; i++) {
				if (i >= 1 && !stop.getNextPath().getPathSegments().get(i).getName()
						.equals(stop.getNextPath().getPathSegments().get(i - 1).getName()))
					bw.write("<li> Follow " + stop.getNextPath().getPathSegments().get(i).getName() + " for "
							+ stop.getNextPath().getPathSegments().get(i).getLength() + " meters , then</li>");
			}
			bw.write("<li>Finally, follow "
					+ stop.getNextPath().getPathSegments().get(stop.getNextPath().getPathSegments().size() - 1)
							.getName()
					+ " for " + stop.getNextPath().getPathSegments()
							.get(stop.getNextPath().getPathSegments().size() - 1).getLength()
					+ " meters </li>");

			index++;
			bw.write("</ul>");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(stop.getArrivalTime());
			calendar.add(Calendar.SECOND, (int) stop.getStopDuration());
			Date arrivalTime = calendar.getTime();

			bw.write("<br><h4>Arrival Time:</h4><ul><li> " + stop.getArrivalTime().getHours() + ":" + stop.getArrivalTime().getMinutes()
					+ ":" + stop.getArrivalTime().getSeconds() + "</ul></li><h4> Departure Time: </h4><ul><li>" + arrivalTime.getHours() + ":"
					+ arrivalTime.getMinutes() + ":" + stop.getArrivalTime().getSeconds() + "</ul></li><br>");
			bw.write("Stop duration : " + stop.getStopDuration() + " seconds <br></p>");
		}

		bw.write("<h2> Tour Arrival </h2>");
		bw.write("<p> Near road : <br> <ul>");
		Stop finalStop = roadMap.getController().getMap().getTour().getOrderedTravel()
				.get(roadMap.getController().getMap().getTour().getOrderedTravel().size() - 1);
		for (int i = 0; i < finalStop.getNextPath().getPathSegments().size() - 1; i++) {
			if (i >= 1 && !finalStop.getNextPath().getPathSegments().get(i).getName()
					.equals(finalStop.getNextPath().getPathSegments().get(i - 1).getName()))
				bw.write("<li> Follow " + finalStop.getNextPath().getPathSegments().get(i).getName() + "for "
						+ finalStop.getNextPath().getPathSegments().get(i).getLength() + " meters , then</li>");
		}
		bw.write("<li>Finally, follow "
				+ finalStop.getNextPath().getPathSegments().get(finalStop.getNextPath().getPathSegments().size() - 1)
						.getName()
				+ " for " + finalStop.getNextPath().getPathSegments()
						.get(finalStop.getNextPath().getPathSegments().size() - 1).getLength()
				+ " meters </li>");
		bw.write("You have now finished your Tour Congratulation!<br><br>");

		bw.write("Arrival Time : " + roadMap.getController().getMap().getTour().getArrivalTime().getHours() + ":"
				+ roadMap.getController().getMap().getTour().getArrivalTime().getMinutes() + ":"
				+ roadMap.getController().getMap().getTour().getArrivalTime().getSeconds() + "</p>");
		bw.write("</body></html>");
		bw.close();

		Desktop.getDesktop().browse(f.toURI());
	}

	public void setVisibleCancel() {
		cancel.setVisible(true);
	}

	public void removeCancel() {
		cancel.setVisible(false);
	}

	public void setVisibleDownload() {
		download.setVisible(true);
	}

	public void removeDownload() {
		download.setVisible(false);
	}

	public void enableUndo() {
		undo.setEnabled(true);
	}

	public void disableUndo() {
		undo.setEnabled(false);
	}

	public void enableRedo() {
		redo.setEnabled(true);
	}

	public void disableRedo() {
		redo.setEnabled(false);
	}

}
