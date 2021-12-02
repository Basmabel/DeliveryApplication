package Controller;



import javax.swing.JOptionPane;

import View.DrawnMap;
import View.LandingPage;
import View.Menu;

/**
 * InitialState implements a state in order to apply the State Design
 * pattern. State at the opening of the application:
 * Neither a map nor requests are loaded.

 * 
 * @see MapLoadedState
 * 
 * @author H4122
 */

public class InitialState implements State {
	
	/**
	 * controller is used to call methods from the controller.
	 * 
	 * @see Controller
	 */
	
	protected Controller controller;
	
	/**
	 * Constructor of InitialState
	 * 
	 * @param newController set the attribute controller.
	 */
	public InitialState(Controller newController) {
		controller = newController;
	}
	
	/**
	 * Does not do anything when called from this state.
	 * 
	 */
	
	@Override
	public void loadNewMap() {
		JOptionPane.showMessageDialog(null, "You are already set to load a Map ( you're in the initial State)");
	}

	/**
	 * Changes the state to MapLoadedState.
	 * 
	 * @see MapLoadedState
	 */
	
	@Override
	public void loadMap(String xmlFile) {
		
		try {
			controller.parseFile(xmlFile);
			controller.getLandingPage().closeLandingPage();
			controller.setMenu(new Menu(controller, xmlFile));
			controller.setCurrentState(controller.getMapLoadedState());
		} catch (Exception e) {
			System.out.println(e);
			controller.getLandingPage().closeLandingPage();
			controller.setLandingPage(new LandingPage(controller));
			JOptionPane.showMessageDialog(null, e);	
		}
	}
	
	/**
	 * Does not do anything when called from this state.
	 * 
	 */

	@Override
	public void loadRequests(String xmlFile, DrawnMap drawnMap) {
		JOptionPane.showMessageDialog(null, "You can't load requests from this state ( you're in the initial State)");
	}
	
	/**
	 * Does not do anything when called from this state.
	 * 
	 */

	@Override
	public void computeTour() {
		JOptionPane.showMessageDialog(null, "You can't compute a tour from this state, you need a map and requests ( you're in the initial State)");
	}
	
	/**
	 * Does not do anything when called from this state.
	 * 
	 */

	@Override
	public void changeStateAdd() {
		JOptionPane.showMessageDialog(null, "You can't Add a request from this state, you need a map,requests, and a tour");		
	}
	
	/**
	 * Does not do anything when called from this state.
	 * 
	 */
	@Override
	public void changeStateDelete() {
		JOptionPane.showMessageDialog(null, "You can't delete a request from this state, you need a map,requests, and a tour");
		
	}

}
