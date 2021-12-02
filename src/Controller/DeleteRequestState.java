package Controller;

import java.awt.Point;
import javax.swing.JOptionPane;

import Model.Request;
import Model.Stop;
import View.DrawnMap;
import View.LandingPage;
import View.LoadRequests;
import View.RoadMap;

/**
 * Implements State interface.
 * State after asking to delete a request from the computed tour.
 * In the state, it is possible to load a new map, load a new list of requests,
 * to select the stop of the request to be deleted and to cancel the selection of the stop.
 * 
 * @author H4122
 * 
 * @see Controller.Controller
 */
public class DeleteRequestState implements State {

	protected Controller controller;

	/**
	 * Class constructor.
	 * @param newController The controller.
	 */
	public DeleteRequestState(Controller newController) {
		controller = newController;
	}

	/**
	 * Changes the state to InitialState.
	 * 
	 * @see Controller.InitialState
	 */
	@Override
	public void loadNewMap() {
		controller.setLandingPage(new LandingPage(controller));
		controller.setCurrentState(controller.getInitialState());
	}

	/**
	 * Loads a map file and changes the state to MapLoadedState.
	 * 
	 * @see Controller.MapLoadedState
	 */
	@Override
	public void loadMap(String xmlFile) {
		JOptionPane.showMessageDialog(null,
				"Map Already loaded, cannot load a Map from this state (You're in the computedTour State)");
	}

	/**
	 * Loads a request file and changes the state to RequestsLoadedState.
	 * 
	 * @see Controller.RequestsLoadedState
	 */
	@Override
	public void loadRequests(String xmlFile, DrawnMap drawnMap) {
		try {
			controller.parseFile(xmlFile);
			new RoadMap(controller, drawnMap);
			controller.setCurrentState(controller.getRequestsLoadedState());
			System.out.println("change from ModifiedTourState to RequestLoadState");
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
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void computeTour() {
		JOptionPane.showMessageDialog(null, "The tour is already computed.");
	}

	/**
	 * Deletes the request given in parameter and add this action to the list of commands if possible.
	 * The clickable buttons are updated (enabled or disabled).<br/>
	 * 
	 * The next state is ComputedState.
	 * 
	 * @param requestToDelete The request to delete.
	 * @param listOfCommands The list of commands that save done actions to make undo/redo possible.
	 * 
	 * @see Controller.ComputedTourState
	 * @see Controller.Controller
	 * @see Controller.ListOfCommands
	 * @see Model.Request
	 * @see Model.Tour#getOrderedTravel()
	 */
	@Override
	public void deleteRequest(Request requestToDelete, ListOfCommands listOfCommands) {
		try {
			// find the pickupPredecessorStop and the deliveryPredecessorStop in the orderedTravel
			int indexPickup = controller.getMap().getTour().getOrderedTravel()
					.indexOf(requestToDelete.getPickupStop());
			int indexDelivery = controller.getMap().getTour().getOrderedTravel()
					.indexOf(requestToDelete.getDeliveryStop());
			Stop pickupPredecessorStop = controller.getMap().getTour().getOrderedTravel().get(indexPickup - 1);
			Stop deliveryPredecessorStop = controller.getMap().getTour().getOrderedTravel().get(indexDelivery - 1);
			if (controller.getMap().getTour().getOrderedTravel().get(indexPickup + 1)
					.equals(requestToDelete.getDeliveryStop())) {
				deliveryPredecessorStop = controller.getMap().getTour().getOrderedTravel().get(indexDelivery - 2);
			}
			
			// delete the request
			listOfCommands.add(new RemoveRequestCommand(controller.getMap(), requestToDelete, pickupPredecessorStop,
					deliveryPredecessorStop));
			controller.setCurrentState(controller.getComputedTourState());
			
			// update buttons
			controller.getMenu().getDrawnMap().delete = false;
			controller.getMenu().instructionsSetText("You can add and delete requests.");
			controller.getMenu().removeCancel();
			controller.getMenu().getDrawnMap().pointClicked=new Point();
			if (controller.getListOfCommands().getCurrentIndex() >= 0) {
				controller.getMenu().enableUndo();
			} else {
				controller.getMenu().disableUndo();
			}
			if (controller.getListOfCommands().getCurrentIndex() < controller.getListOfCommands().getList().size()-1) {
				controller.getMenu().enableRedo();
			} else {
				controller.getMenu().disableRedo();
			}
		} catch (Exception e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(null, e);
		}
	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void changeStateAdd() {
		JOptionPane.showMessageDialog(null, "You can't add a request when you are deleting one");

	}

	/**
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void changeStateDelete() {
		JOptionPane.showMessageDialog(null, "You are already deleting a request.");
	}
	
	/**
	 * Cancels the selection of the stop of the request to delete.
	 * It is possible to cancel if the stop has not been selected yet.<br/>
	 * 
	 * The next state is ComputedState.
	 * 
	 * @see Controller.ComputedTourState
	 */
	public void cancel() {
		controller.setCurrentState(controller.getComputedTourState());
		controller.getMenu().getDrawnMap().delete = false;
		controller.getMenu().instructionsSetText("You can add and delete requests.");
		controller.getMenu().removeCancel();
		if (controller.getListOfCommands().getCurrentIndex() >= 0) {
			controller.getMenu().enableUndo();
		} else {
			controller.getMenu().disableUndo();
		}
		if (controller.getListOfCommands().getCurrentIndex() < controller.getListOfCommands().getList().size()-1) {
			controller.getMenu().enableRedo();
		} else {
			controller.getMenu().disableRedo();
		}
	}


}