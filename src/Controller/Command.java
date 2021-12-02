package Controller;

/**
 * Interface Command used for the Command design pattern. The following commands
 * implement Command :
 * <ul>
 * <li>AddRequestCommand : command to add a request.</li>
 * <li>RemoveRequestCommand : command to remove a request.</li>
 * </ul>
 * 
 * @see AddRequestCommand
 * @see RemoveRequestCommand
 * 
 * @author H4122
 */
public interface Command {
	/**
	 * The method that executes the command.
	 * 
	 * @see AddRequestCommand#doCommand()
	 * @see RemoveRequestCommand#doCommand()
	 */
	void doCommand();
	/**
	 * The method that undos the command.
	 * 
	 * @see AddRequestCommand#undoCommand()
	 * @see RemoveRequestCommand#undoCommand()
	 */
	void undoCommand();
}
