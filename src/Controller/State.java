package Controller;

import javax.swing.JOptionPane;

import Model.Request;
import Model.Stop;
import View.DrawnMap;

/**
 * Interface State used for the state design pattern. The following states
 * implement State :
 * <ul>
 * <li>InitialState : state at the opening of the application.</li>
 * <li>MapLoadedState : state with a loaded map (after loading a map file).</li>
 * <li>RequestsLoadedState : state when requests are loaded (after loading a
 * request file).</li>
 * <li>ComputedTourState : state entered when a tour (optimal or not) is
 * computed.</li>
 * <li>TimeoutState : state entered if <code>searchSolution</code> and
 * <code>continueTSP</code> methods for computing the tour reached
 * <code>timeLimit</code>.</li>
 * <li>DeleteRequestState : state entered to delete a request from the computed
 * tour.</li>
 * <li>AddPickupStopState : state entered to add a pickup stop when wanting to
 * add a new request to the tour.</li>
 * <li>AddPickupPredecessorState : state entered to indicate the predecessor of
 * the new pickup stop.</li>
 * <li>AddDeliveryStopState : state entered to add a delivery stop when wanting
 * to add a new request to the tour.</li>
 * <li>AddDeliveryPredecessorState : state entered to indicate the predecessor
 * of the new delivery stop.</li>
 * </ul>
 * 
 * @see Model.Map
 * @see Model.Request
 * @see Model.TemplateTSP#searchSolution(int, Model.Graph)
 * @see Model.Tour
 * @see Model.Stop
 * 
 * @author H4122
 */
public interface State {

	/**
	 * Changes the state to InitialState if the current state is amongst the
	 * following :
	 * <ul>
	 * <li>MapLoadedState</li>
	 * <li>RequestsLoadedState</li>
	 * <li>ComputedTourState</li>
	 * <li>DeleteRequestState</li>
	 * <li>AddPickupStopState</li>
	 * <li>AddPickupPredecessorState</li>
	 * <li>AddDeliveryStopState</li>
	 * <li>AddDeliveryPredecessorState</li>
	 * </ul>
	 * 
	 * @see InitialState
	 * @see MapLoadedState
	 * @see RequestsLoadedState
	 * @see ComputedTourState
	 * @see DeleteRequestState
	 * @see AddPickupStopState
	 * @see AddPickupPredecessorState
	 * @see AddDeliveryStopState
	 * @see AddDeliveryPredecessorState
	 */
	void loadNewMap();

	/**
	 * Loads a map file and changes the state to MapLoadedState only if the current
	 * state is InitialState.
	 * 
	 * @param xmlFile is the path to the xml map file.
	 * 
	 * @see InitialState
	 * @see MapLoadedState
	 * @see Model.Map
	 */
	void loadMap(String xmlFile);

	/**
	 * Loads a request file and changes the state to RequestsLoadedState if the
	 * current state is amongst the following :
	 * <ul>
	 * <li>MapLoadedState</li>
	 * <li>RequestsLoadedState</li>
	 * <li>ComputedTourState</li>
	 * <li>DeleteRequestState</li>
	 * <li>AddPickupStopState</li>
	 * <li>AddPickupPredecessorState</li>
	 * <li>AddDeliveryStopState</li>
	 * <li>AddDeliveryPredecessorState</li>
	 * </ul>
	 * 
	 * @param xmlFile  is the path to the xml request file.
	 * @param drawnMap is used to create a road map.
	 * 
	 * @see MapLoadedState
	 * @see RequestsLoadedState
	 * @see ComputedTourState
	 * @see DeleteRequestState
	 * @see AddPickupStopState
	 * @see AddPickupPredecessorState
	 * @see AddDeliveryStopState
	 * @see AddDeliveryPredecessorState
	 * @see View.RoadMap
	 * @see View.DrawnMap
	 */
	void loadRequests(String xmlFile, DrawnMap drawnMap);

	/**
	 * Computes a tour and changes to either ComputedTourState (is optimal tour is
	 * computed) or to TimeoutState (if <code>timeLimit</code> (in
	 * <code>searchSolution</code> or <code>continueTSP</code>) is reached). Changes
	 * state only if current state is RequestsLoadedState.
	 * 
	 * @see ComputedTourState
	 * @see TimeoutState
	 * @see Model.TemplateTSP#searchSolution(int, Model.Graph)
	 * @see Model.Tour
	 */
	void computeTour();

	/**
	 * Changes state to AddPickupStopState only if current state is
	 * ComputedTourState.
	 * 
	 * @see ComputedTourState
	 * @see AddPickupStopState
	 * @see Model.Stop
	 */
	public default void changeStateAdd() {
		JOptionPane.showMessageDialog(null, "You can't add a request at this state");
	};

	/**
	 * Changes state to DeleteRequestState only if current state is
	 * ComputedTourState.
	 * 
	 * @see ComputedTourState
	 * @see DeleteRequestState
	 * @see Model.Request
	 */
	public default void changeStateDelete() {
		JOptionPane.showMessageDialog(null, "You can't delete a request at this state");
	};

	/**
	 * Changes state to AddPickupPredecessorState only if current state is
	 * AddPickupStopState OR changes state to AddDeliveryPredecessorState only if
	 * current state is AddDeliveryStopState.
	 * 
	 * @param stopCreated is the new stop that will be added to the tour (pickup or
	 *             delivery).
	 * 
	 * @see AddPickupPredecessorState
	 * @see AddPickupStopState
	 * @see AddDeliveryPredecessorState
	 * @see AddDeliveryStopState
	 * @see Model.Stop
	 * @see Model.Tour
	 */
	public default void clicIntersection(Stop stopCreated) {
	};

	/**
	 * Changes state to AddDeliveryStopState only if current state is
	 * AddPickupPredecessorState.
	 * 
	 * @param stop is the pickup predecessor stop for the new request that will be
	 *             added to the tour.
	 * 
	 * @see AddPickupPredecessorState
	 * @see AddDeliveryStopState
	 * @see Model.Stop
	 * @see Model.Tour
	 */
	public default void clicStop(Stop stop) {
	};

	/**
	 * Adds the request to the tour and 
	 * changes the state from AddDeliveryPredecessorState to ComputedTourState.
	 * 
	 * @param deliveryPredecessor is the delivery predecessor stop for the new
	 *                            request that will be added to the tour.
	 * @param listOfCommands      saves the command list.
	 * 
	 * @see Model.Stop
	 * @see Model.Request
	 * @see Model.Tour
	 * @see ListOfCommands
	 */
	public default void addRequest(Stop deliveryPredecessor, ListOfCommands listOfCommands) {
	};

	/**
	 * Deletes the request from the tour and 
	 * changes the state from DeleteRequestState to ComputedTourState.
	 * 
	 * @param requestToDelete is the request that will be deleted from the tour.
	 * @param listOfCommands  saves the command list.
	 * 
	 * @see Model.Request
	 * @see Model.Tour
	 * @see ListOfCommands
	 */
	public default void deleteRequest(Request requestToDelete, ListOfCommands listOfCommands) {
	};

	/**
	 * Undoes the last command of the <code>listOfCommands</code>.
	 * 
	 * @param listOfCommands saves the command list.
	 * 
	 * @see ListOfCommands
	 */
	public default void undo(ListOfCommands listOfCommands) {
		JOptionPane.showMessageDialog(null, "You can't undo your command at this state");
	};

	/**
	 * Redoes the last undone command of the <code>listOfCommands</code>.
	 * 
	 * @param listOfCommands saves the command list.
	 * 
	 * @see ListOfCommands
	 */
	public default void redo(ListOfCommands listOfCommands) {
		JOptionPane.showMessageDialog(null, "You can't redo your command at this state");
	};

	/**
	 * Cancels the adding or deleting of a request in the tour.
	 * 
	 * @see Model.Request
	 * @see Model.Tour
	 */
	public default void cancel() {
	};

	/**
	 * When a tour is being computed, if the timeout is reached and the user chooses
	 * to keep computing, this method loops the state on TimeoutState, until another
	 * <code>timeLimit</code> is reached.
	 * 
	 * @see Model.Tour
	 * @see TimeoutState
	 */
	public default void continueComputing() {
		JOptionPane.showMessageDialog(null, "You can't continue computing at this state");
	};
}
