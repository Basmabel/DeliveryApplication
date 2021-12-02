package Controller;

import java.awt.Point;

import javax.swing.JOptionPane;

import Model.Stop;
import View.DrawnMap;
import View.LandingPage;
import View.LoadRequests;
import View.RoadMap;

/**
 * State to add a predecessor to the delivery stop a the request that will be
 * added to the tour.
 * 
 * @author H4122
 * @see Model.Request
 * @see Model.Tour
 * @see Model.Stop
 */
public class AddDeliveryPredecessorState implements State {

	/**
	 * controller is used to call methods from the controller.
	 * 
	 * @see Controller
	 */
	protected Controller controller;

	/**
	 * Constructor of AddDeliveryPredecessorState
	 * 
	 * @param newController set the attribute controller.
	 */
	public AddDeliveryPredecessorState(Controller newController) {
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
		JOptionPane.showMessageDialog(null,
				"Map Already loaded, cannot load a Map from this state (You're in the computedTour State)");
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
			controller.getMenu().disableUndo();
			controller.getMenu().disableRedo();
			controller.getMenu().removeCancel();
			controller.getMenu().removeDownload();
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
		JOptionPane.showMessageDialog(null, "Tour Already computed (You're in the ModifiedTourState State)");
	}

	/**
	 * Adds a new request to the tour, placing the pickup and the delivery where the user specified it.
	 * 
	 * @see Model.Tour
	 */
	@Override
	public void addRequest(Stop deliveryPredecessor, ListOfCommands listOfCommands) {
		try {
			controller.getMap().getTour().setDeliveryStopPredecessor(deliveryPredecessor);
			listOfCommands.add(new AddRequestCommand(controller.getMap(), controller.getMap().getTour().getPickupStop(),
					controller.getMap().getTour().getDeliveryStop(),
					controller.getMap().getTour().getPickupStopPredecessor(), deliveryPredecessor));
			controller.setCurrentState(controller.getComputedTourState());
			controller.getMenu().getDrawnMap().add = false;
			controller.getMenu().instructionsSetText("You can add and delete requests.");
			controller.getMenu().removeCancel();
			controller.getMenu().getDrawnMap().pointClicked = new Point();
			if (controller.getListOfCommands().getCurrentIndex() >= 0) {
				controller.getMenu().enableUndo();
			} else {
				controller.getMenu().disableUndo();
			}
			if (controller.getListOfCommands().getCurrentIndex() < controller.getListOfCommands().getList().size()
					- 1) {
				controller.getMenu().enableRedo();
			} else {
				controller.getMenu().disableRedo();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void changeStateAdd() {
		JOptionPane.showMessageDialog(null, "You are already adding a request");

	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void changeStateDelete() {
		JOptionPane.showMessageDialog(null, "You can't delete a request when you are adding one");

	}

	/**
	 * Cancels the adding of the new request in the tour.
	 * 
	 * @see Model.Request
	 * @see Model.Tour
	 */
	public void cancel() {
		controller.setCurrentState(controller.getComputedTourState());
		controller.getMenu().getDrawnMap().add = false;
		controller.getMenu().instructionsSetText("You can add and delete requests.");
		controller.getMenu().removeCancel();
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