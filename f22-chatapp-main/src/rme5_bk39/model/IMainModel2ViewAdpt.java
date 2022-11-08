package rme5_bk39.model;

import java.util.Set;
import java.util.UUID;

import common.serverObj.INamedAppConnection;
import rme5_bk39.IMain2MiniAdpt;


/**
 * @author Renzo Espinoza Bo Sung Kim
 *	The adapter for the main model to the main view
 */
public interface IMainModel2ViewAdpt {

    
    /**
	 * Make a new chat room.
     * @param roomName the room name.
	 * @param pubSubManager the manager.
	 * @return an IMain2MiniAdptr to interact with it
	 */
	public IMain2MiniAdpt makeNewRoom(String roomName);
	
	/**
	 * Join a chat room.
	 * @param roomID the room ID.
	 * @param roomName the room name.
	 * @param pubSubManager the manager.
	 * @return the adapter so that the main can add the IRecevier to the room roster.
	 */
	public IMain2MiniAdpt join(UUID roomID, String roomName);
	

	/**
	 * Stops the discovery server
	 */
	void stopDiscModel();
	
	/**
	 * Updates the contacts
	 * @param stubs Named connection stubs
	 */
	public void updateContacts(Set<INamedAppConnection> stubs);

	/**
	 * Dispplays the status of the message
	 * @param apply A string of the status
	 */
	public void displayStatusMsg(String apply);
	
	
	/**
	 * @return the username
	 */
	public String getUserName();
}
