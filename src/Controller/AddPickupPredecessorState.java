package Controller;

import javax.swing.JOptionPane;

import Model.Stop;
import View.DrawnMap;
import View.LandingPage;
import View.LoadRequests;
import View.RoadMap;

/**
 * Implements State interface.
 * State to choose the predecessor stop of the new stop to add.
 * In the state, it is possible to load a new map, load a new list of requests,
 * to select the stop of the request to be deleted and to cancel the selection of the stop.
 * 
 * @author H4122
 * 
 * @see Controller.Controller
 */
public class AddPickupPredecessorState implements State {

	protected Controller controller;

	/**
	 * Class constructor.
	 * 
	 * @param newController The controller.
	 */
	public AddPickupPredecessorState(Controller newController) {
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
	 * Displays a pop-up saying the action is not possible in this state.
	 */
	@Override
	public void loadMap(String xmlFile) {
		JOptionPane.showMessageDialog(null, 
				"A map is already loaded, you can't load a map from this state.");
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
		JOptionPane.showMessageDialog(null, 
				"The tour is already computed.");
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
	 * Saves the selected stop and changes state to AddDeliveryStopState.
	 * 
	 * @param stop The selected pickup predecessor stop for the new request 
	 * that will be added to the tour.
	 * 
	 * @see AddDeliveryStopState
	 */
	@Override
	public void clicStop(Stop stop) {
		try {
			controller.getMap().getTour().setPickupStopPredecessor(stop);
			controller.setCurrentState(controller.getAddDeliveryStopState());
			controller.getMenu().instructionsSetText("Select the delivery intersection.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	/**
	 * Cancels the selection of the predecessor stop of the pickup stop of the request to add.
	 * It is possible to cancel if the stop has not been selected yet.<br/>
	 * 
	 * The next state is ComputedState.
	 * 
	 * @see Controller.ComputedTourState
	 */
	public void cancel() {
		controller.setCurrentState(controller.getComputedTourState());
		controller.getMenu().getDrawnMap().add = false;
		controller.getMenu().instructionsSetText("You can add and delete requests.");
		controller.getMenu().removeCancel();
	}

}