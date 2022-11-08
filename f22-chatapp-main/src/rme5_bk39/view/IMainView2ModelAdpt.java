package rme5_bk39.view;


/**
 * @author Renzo Espinoza
 *
 * @param <T> Chatapp instance
 */
public interface IMainView2ModelAdpt<T> {


	/**
	 * Terminate the program, clean up(send IQuitMsg).
	 */
	void quit();
	
	/**
	 * Invite the chat room app instance to the current chat room.
	 * @param app the app to be invited.
	 */
	void invite(T app);
	
	/**
	 * Start the model, used by the start button.
	 */
	void start();
	
	/**
	 * @param roomName the room name.
	 */
	void makeNewRoom(String roomName);
	
	/**
	 * Removes the indexed room
	 * @param index the idx
	 */
	void removeRoom(int index);
	
	
}
