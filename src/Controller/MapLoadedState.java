package Controller;

import javax.swing.JOptionPane;

import View.DrawnMap;
import View.LandingPage;
import View.LoadRequests;
import View.RoadMap;

/**
 * MapLoadedState is state with a loaded map (after loading a map file).
 * 
 * @author H4122
 * 
 * @see InitialState
 * @see Model.Map
 * @see InitialState
 */
public class MapLoadedState implements State {

	/**
	 * controller is used to call methods from the controller.
	 * 
	 * @see Controller
	 */
	protected Controller controller;

	/**
	 * Constructor of MapLoadedState
	 * 
	 * @param newController set the attribute controller.
	 */
	public MapLoadedState(Controller newController) {
		controller = newController;
	}

	/**
	 * Changes the state to InitialState.
	 * 
	 * @see InitialState
	 */
	@Override
	public void loadNewMap() {
		controller.setLandingPage(new LandingPage(controller));
		controller.setCurrentState(controller.getInitialState());
	}

	/**
	 * Does not do anything when called from this state.
	 */
	@Override
	public void loadMap(String xmlFile) {
	}

	/**
	 * Loads requests and changes state to RequestsLoadedState.
	 * 
	 * @see Model.Request
	 * @see RequestsLoadedState
	 */
	@Override
	public void loadRequests(String xmlFile, DrawnMap drawnMap) {
		try {
			controller.parseFile(xmlFile);
			new RoadMap(controller, drawnMap);
			controller.setCurrentState(controller.getRequestsLoadedState());
			controller.getMenu().getLoadRequestsFrame().closeLoadRequests();
			controller.getMenu().instructionsSetText("<html><body>You can compute a tour and display it on the map.<br>"
					+ " It may take 20 seconds as maximum of time.</body></html>");
		} catch (Exception e) {
			controller.getMenu().getLoadRequestsFrame().closeLoadRequests();
			controller.getMenu().setLoadRequestsFrame(new LoadRequests(controller, controller.getMenu().getDrawnMap()));
			JOptionPane.showMessageDialog(null, e);
		}

	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void computeTour() {
		JOptionPane.showMessageDialog(null,
				"You can't compute a Tour from this state, you need requests.");
	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void changeStateAdd() {
		JOptionPane.showMessageDialog(null,
				"You can't Add a request from this state, you need requests.");
	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void changeStateDelete() {
		JOptionPane.showMessageDialog(null,
				"You can't delete a request from this state, you need requests.");
	}

}
