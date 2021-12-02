package Controller;

import javax.swing.JOptionPane;

import View.DrawnMap;

/**
 * TimeoutState is the state that manages the computing of the tour if the
 * duration of computing reached <code>timeLimit</code> and if the user want to
 * keep computing.
 * 
 * @author H4122
 * @see Model.Tour
 */
public class TimeoutState implements State {

	/**
	 * controller is used to call methods from the controller.
	 * 
	 * @see Controller
	 */
	protected Controller controller;

	/**
	 * Constructor of TimeoutState
	 * 
	 * @param newController set the attribute controller.
	 */
	public TimeoutState(Controller newController) {
		controller = newController;
	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void loadNewMap() {
		JOptionPane.showMessageDialog(null, "You cannot load a new map from this state");

	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void loadMap(String xmlFile) {
		JOptionPane.showMessageDialog(null, "You cannot load a map from this state");
	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void loadRequests(String xmlFile, DrawnMap drawnMap) {
		JOptionPane.showMessageDialog(null, "You cannot load requests from this state");
	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void computeTour() {
		JOptionPane.showMessageDialog(null, "You cannot compute a tour from this state");
	}

	/**
	 * If the user chooses to, the method resumes the computing of the tour.
	 * 
	 * @see Model.Tour
	 */
	@Override
	public void continueComputing() {
		int optimalTour = controller.resumeComputing();
		if (optimalTour == 0) {
			controller.saveTSPSolution(controller.getTsp(), controller.getPaths());
			controller.setCurrentState(controller.getComputedTourState());
			controller.getMenu().instructionsSetText("You can add and delete requests.");
			controller.getMenu().setVisibleDownload();
		} else {
			int input = JOptionPane.showConfirmDialog(null, "Timeout : Do you want to continue computing the tour?");
			if (input == 0)
				continueComputing();
			else {
				controller.saveTSPSolution(controller.getTsp(), controller.getPaths());
				controller.setCurrentState(controller.getComputedTourState());
				controller.getMenu().instructionsSetText("You can add and delete requests.");
			}
		}

	}

}
