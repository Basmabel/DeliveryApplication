package Controller;

import Model.Map;
import Model.Request;
import Model.Stop;

/**
 * AddRequestCommand implements the interface Command in order to apply the
 * Command design pattern. It is the class representing the command to add a
 * request.<br/>
 * 
 * A AddRequestCommand is characterized by:
 * <ul>
 * <li>A map which is the map on which will be add the request.</li>
 * <li>A request which will be added.</li>
 * <li>A pickup predecessor which is the predecessor of the pickup stop of the
 * request we want to add.</li>
 * <li>A delivery predecessor which is the predecessor of the delivery stop of
 * the request we want to add.</li>
 * </ul>
 * 
 * @see Command
 * 
 * @author H4122
 */
public class AddRequestCommand implements Command {
	
	private Map map;
	private Request request;
	private Stop pickupPredecessor;
	private Stop deliveryPredecessor;
	
	/**
	 * Constructor of AddRequestCommand. When the AddRequestCommand is
	 * created, map, request, pickupPredecessor and deliveryPredecessor are
	 * initialized with the corresponding parameters.
	 * 
	 * @param map                 The map on which a request will be added.
	 * @param pickupStop          The pickup stop of the request that will be added.
	 * @param deliveryStop        The delivery stop of the request that will be added.
	 * @param pickupPredecessor   The predecessor of the pickup stop of the request
	 *                            we want to add.
	 * @param deliveryPredecessor The predecessor of the delivery stop of the
	 *                            request we want to add.
	 * 
	 * @see AddRequestCommand#map
	 * @see AddRequestCommand#request
	 * @see AddRequestCommand#pickupPredecessor
	 * @see AddRequestCommand#deliveryPredecessor
	 */
	public AddRequestCommand(Map map,Stop pickupStop,Stop deliveryStop, Stop pickupPredecessor, Stop deliveryPredecessor) {
		this.map = map;
		this.request = new Request(pickupStop,deliveryStop);
		this.pickupPredecessor = pickupPredecessor;
		this.deliveryPredecessor = deliveryPredecessor;
	}

	/**
	 * Overrides doCommand() of Command. It calls the method addRequest() in map
	 * to add request
	 * 
	 * @see Command#doCommand()
	 * @see Model.Map#addRequest()
	 */
	@Override
	public void doCommand() {
		try {
			this.map.getTour().setPickupStop(request.getPickupStop());
			this.map.getTour().setDeliveryStop(request.getDeliveryStop());
			this.map.getTour().setPickupStopPredecessor(pickupPredecessor);
			this.map.getTour().setDeliveryStopPredecessor(deliveryPredecessor);
			this.map.addRequest();
		}catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Overrides undoCommand() of Command. It undoes the add request command and thus calls the method deleteRequest() in map
	 * to delete the previously added request.
	 * @see Command#undoCommand()
	 * @see Model.Map#deleteRequest(Request)
	 */
	@Override
	public void undoCommand() {
		try {
			this.map.deleteRequest(request);
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
}
