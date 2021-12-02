package Controller;

import javax.swing.JOptionPane;

import Model.Stop;
import View.DrawnMap;
import View.LandingPage;
import View.LoadRequests;
import View.RoadMap;

/**
 * Implements the Interface State used for the state design pattern.
 * AddPickupStopState is the class representing the state entered to add a pickup stop when wanting to
 * add a new request to the tour. It is characterized by :
 * <ul>
 * <li>A controller that contains some attributes and methods that
 * AddPickupStopState class needs to access.</li>
 * </ul>
 * 
 * @see Controller
 * @see State
 * 
 * @author H4122
 */
public class AddPickupStopState implements State {

	protected Controller controller;

	/**
	 * Constructor of AddPickupStopState. When the AddPickupStopState is created,its
	 * controller is initialized with a controller passed as a parameter.
	 * 
	 * @param newController The controller of AddPickupStopState that contains some
	 *                      attributes and methods that the class needs to access.
	 * 
	 * @see AddPickupStopState#controller
	 */
	public AddPickupStopState(Controller newController) {
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
		JOptionPane.showMessageDialog(null, "Map Already loaded, cannot load a Map from this state ");


	}

	/**
	 * Loads a request file and changes the state to RequestsLoadedState.
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
	 * Displays a pop-up to notify the user that they're already adding a request. 
	 */
	@Override
	public void changeStateAdd() {
		JOptionPane.showMessageDialog(null, "You are already adding a request");		
	}

	/**
	 * Displays a pop-up to notify the user that they cannot delete a request while adding one. 
	 */
	@Override
	public void changeStateDelete() {
		JOptionPane.showMessageDialog(null, "You can't delete a request when you are adding one");		
	}
	
	/**
	 * Changes state to AddPickupPredecessorState.
	 */
	@Override
	public void clicIntersection(Stop stopCreated) {
		try {
			controller.getMap().getTour().setPickupStop(stopCreated);
			controller.setCurrentState(controller.getAddPickupPredecessorState());
			controller.getMenu().instructionsSetText("Select the stop predecessor");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}	
	}

	/**
	 * Cancels the adding or deleting of a request in the tour.
	 */
	public void cancel() {
		controller.setCurrentState(controller.getComputedTourState());
		controller.getMenu().getDrawnMap().add = false;
		controller.getMenu().instructionsSetText("You can add and delete requests.");
		controller.getMenu().removeCancel();
	}


}