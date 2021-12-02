package Controller;

import javax.swing.JOptionPane;

import View.DrawnMap;
import View.LandingPage;
import View.LoadRequests;
import View.RoadMap;

/**
 * State when requests are loaded (after loading a request file).
 * 
 * @author H4122
 * @see Model.Request
 */
public class RequestsLoadedState implements State {

	/**
	 * controller is used to call methods from the controller.
	 * 
	 * @see Controller
	 */
	protected Controller controller;

	/**
	 * Constructor of RequestsLoadedState
	 * 
	 * @param newController set the attribute controller.
	 */
	public RequestsLoadedState(Controller newController) {
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
	 * Does nothing when called from this state.
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
		} catch (Exception e) {
			controller.getMenu().getLoadRequestsFrame().closeLoadRequests();
			controller.getMenu().setLoadRequestsFrame(new LoadRequests(controller, controller.getMenu().getDrawnMap()));
			JOptionPane.showMessageDialog(null, e);

		}

	}

	/**
	 * Computes a tour. If an optimal tour is computed before
	 * <code>timeLimit</code>, changes to ComputedTourState. Otherwise, changes to
	 * TimeoutState.
	 * 
	 * @see Model.Tour
	 * @see ComputedTourState
	 * @see TimeoutState
	 */
	@Override
	public void computeTour() {
		int finishCompute = controller.computeOptimisedTour(controller.getMap());
		if (finishCompute == 0) {
			controller.saveTSPSolution(controller.getTsp(), controller.getPaths());
			controller.setCurrentState(controller.getComputedTourState());
			controller.getMenu().instructionsSetText("You can add and delete requests.");
			controller.getMenu().setVisibleDownload();
		} else {
			controller.setCurrentState(controller.getTimeoutState());
			int input = JOptionPane.showConfirmDialog(null, "Timeout : Do you want to continue computing the tour?");
			if (input == 0)
				controller.getCurrentState().continueComputing();
			else {
				controller.saveTSPSolution(controller.getTsp(), controller.getPaths());
				controller.setCurrentState(controller.getComputedTourState());
				controller.getMenu().instructionsSetText("You can add and delete requests.");
			}
		}

	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void changeStateAdd() {
		JOptionPane.showMessageDialog(null, "You can't add a request from this state");
	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void changeStateDelete() {
		JOptionPane.showMessageDialog(null, "You can't delete a request from this state");
	}

}
