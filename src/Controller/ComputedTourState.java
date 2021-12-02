package Controller;

import javax.swing.JOptionPane;

import Model.Request;
import View.DrawnMap;
import View.LandingPage;
import View.LoadRequests;
import View.RoadMap;

/**
 * Implements the Interface State used for the state design pattern.
 * ComputedTourState is the class representing the state entered when a tour
 * (optimal or not) is computed. It is characterized by :
 * <ul>
 * <li>A controller that contains some attributes and methods that
 * ComputedTourState class needs to access.</li>
 * </ul>
 * 
 * @see Controller
 * @see State
 * 
 * @author H4122
 */
public class ComputedTourState implements State {

	protected Controller controller;

	/**
	 * Constructor of ComputedTourState. When the ComputedTourState is created,its
	 * controller is initialized with a controller passed as a parameter.
	 * 
	 * @param newController The controller of ComputedTourState that contains some
	 *                      attributes and methods that the class needs to access.
	 * 
	 * @see ComputedTourState#controller
	 */
	public ComputedTourState(Controller newController) {
		controller = newController;
	}

	/**
	 * Changes the state to InitialState.
	 */
	@Override
	public void loadNewMap() {
		controller.setLandingPage(new LandingPage(controller));
		controller.setCurrentState(controller.getInitialState());
	}

	/**
	 * Displays a pop-up to notify the user that the map cannot be loaded at this state. 
	 */
	@Override
	public void loadMap(String xmlFile) {
		JOptionPane.showMessageDialog(null,
				"Map Already loaded, cannot load a Map from this state ");

	}

	/**
	 * Loads a request file and changes the state to RequestsLoadedState
	 */
	@Override
	public void loadRequests(String xmlFile, DrawnMap drawnMap) {
		try {
			controller.parseFile(xmlFile);
			new RoadMap(controller, drawnMap);
			controller.setCurrentState(controller.getRequestsLoadedState());
			controller.getMenu().getLoadRequestsFrame().closeLoadRequests();
			controller.getMenu().disableUndo();
			controller.getMenu().disableRedo();
			controller.getMenu().removeCancel();
			controller.getMenu().removeDownload();
			controller.getMenu().instructionsSetText("<html><body>You can compute a tour and display it on the map.<br>"
					+ " It may take 20 seconds as maximum of time.</body></html>");

		} catch (Exception e) {
			System.out.println(e);
			controller.getMenu().getLoadRequestsFrame().closeLoadRequests();
			controller.getMenu().setLoadRequestsFrame(new LoadRequests(controller, controller.getMenu().getDrawnMap()));
			JOptionPane.showMessageDialog(null, e);

		}

	}

	/**
	 * Displays a pop-up to notify the user that the tour cannot be computed at this state.
	 */
	@Override
	public void computeTour() {
		JOptionPane.showMessageDialog(null, "Tour Already computed");
	}

	/**
	 * Displays a pop-up to notify the user that a request cannot be deleted at this state.
	 */
	@Override
	public void deleteRequest(Request requestToDelete, ListOfCommands listOfCommands) {
		JOptionPane.showMessageDialog(null, "Not possible to delete request at this state");
	}

	/**
	 * Changes state to AddPickupStopState
	 */
	@Override
	public void changeStateAdd() {
		try {
			controller.setCurrentState(controller.getAddPickupStopState());
			controller.getMenu().instructionsSetText("Click on a intersection to place a pick up stop");
			controller.getMenu().setVisibleCancel();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	/**
	 * Changes state to DeleteRequestState
	 */
	@Override
	public void changeStateDelete() {
		try {
			controller.setCurrentState(controller.getDeleteRequestState());
			controller.getMenu()
					.instructionsSetText("Click on a pick up stop or a delivery stop to delete the associated request");
			controller.getMenu().setVisibleCancel();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	/**
	 * Undoes the last command of the <code>listOfCommands</code>.
	 */
	public void undo(ListOfCommands listOfCommands) {
		listOfCommands.undo();
		if (controller.getListOfCommands().getCurrentIndex() >= 0) {
			controller.getMenu().enableUndo();
		} else {
			controller.getMenu().disableUndo();
		}
		if (controller.getListOfCommands().getCurrentIndex() < controller.getListOfCommands().getList().size() - 1) {
			controller.getMenu().enableRedo();
		} else {
			controller.getMenu().disableRedo();
		}
	}

	/**
	 * Redoes the last undone command of the <code>listOfCommands</code>
	 */
	public void redo(ListOfCommands listOfCommands) {
		listOfCommands.redo();
		if (controller.getListOfCommands().getCurrentIndex() >= 0) {
			controller.getMenu().enableUndo();
		} else {
			controller.getMenu().disableUndo();
		}
		if (controller.getListOfCommands().getCurrentIndex() < controller.getListOfCommands().getList().size() - 1) {
			controller.getMenu().enableRedo();
		} else {
			controller.getMenu().disableRedo();
		}
	}

}