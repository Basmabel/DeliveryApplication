package Controller;

import Model.Map;
import Model.Request;
import Model.Stop;

/**
 * RemoveRequestCommand implements the interface Command in order to apply the
 * Command design pattern. It is the class representing the command to remove a
 * request.<br/>
 * 
 * A RemoveRequestCommand is characterized by:
 * <ul>
 * <li>A map which is the map on which will be removed the request.</li>
 * <li>A request which will be removed.</li>
 * <li>A pickup predecessor which is the predecessor of the pickup stop of the
 * request we want to remove.</li>
 * <li>A delivery predecessor which is the predecessor of the delivery stop of
 * the request we want to remove.</li>
 * </ul>
 * 
 * @see Command
 * 
 * @author H4122
 */
public class RemoveRequestCommand implements Command {

	private Map map;
	private Request request;
	private Stop pickupPredecessor;
	private Stop deliveryPredecessor;

	/**
	 * Constructor of RemoveRequestCommand. When the RemoveRequestCommand is
	 * created, map, request, pickupPredecessor and deliveryPredecessor are
	 * initialized with the corresponding parameters.
	 * 
	 * @param map                 The map on which a request will be removed.
	 * @param request             The request that will be removed.
	 * @param pickupPredecessor   The predecessor of the pickup stop of the request
	 *                            we want to remove.
	 * @param deliveryPredecessor The predecessor of the delivery stop of the
	 *                            request we want to remove.
	 * 
	 * @see RemoveRequestCommand#map
	 * @see RemoveRequestCommand#request
	 * @see RemoveRequestCommand#pickupPredecessor
	 * @see RemoveRequestCommand#deliveryPredecessor
	 */
	public RemoveRequestCommand(Map map, Request request, Stop pickupPredecessor, Stop deliveryPredecessor) {
		super();
		this.map = map;
		this.request = request;
		this.pickupPredecessor = pickupPredecessor;
		this.deliveryPredecessor = deliveryPredecessor;
	}

	/**
	 * Overrides doCommand() of Command. It calls the method deleteRequest() in map
	 * to delete request
	 * 
	 * @see Command#doCommand()
	 * @see Model.Map#deleteRequest(Request)
	 */
	@Override
	public void doCommand() {
		// TODO Auto-generated method stub
		try {
			this.map.deleteRequest(request);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Overrides undoCommand() of Command. It undos the remove request command and thus calls the method addRequest() in map
	 * to add the previously deleted request.
	 * @see Command#undoCommand()
	 * @see Model.Map#addRequest()
	 */
	@Override
	public void undoCommand() {
		// TODO Auto-generated method stub
		try {
			this.map.getTour().setPickupStop(request.getPickupStop());
			this.map.getTour().setDeliveryStop(request.getDeliveryStop());
			this.map.getTour().setPickupStopPredecessor(pickupPredecessor);
			this.map.getTour().setDeliveryStopPredecessor(deliveryPredecessor);
			this.map.addRequest();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
