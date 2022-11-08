package rme5_bk39;

import java.awt.Component;
import java.util.HashSet;
import java.util.UUID;

import common.serverObj.INamedRoomConnection;
import provided.pubsubsync.IPubSubSyncChannelUpdate;

/**
 * @author Renzo Espinoza Bo Sung Kim
 * The adapter for the main mvc to the mini mvc
 */
public interface IMain2MiniAdpt {	
	/**
	 * @return the named receiver representing the stub of the chat room(the mini mvc)
	 */
	public INamedRoomConnection getNamedReceiver();
	
	/**
	 * @return the panel containing the view of a chat room.
	 */
	public Component getRoomPanel();
	
	/**
	 * Start the mini controller - start the chat room!
	 */
	public void start();
	
	/**
	 * Quit the chat room - when the entire chat app quit, notify each room that we quit.
	 */
	 public void quit();
	 
	 public void finalExit();


	/**
	 * @return the chatRoomID.
	 */
	public UUID getChatRoomID();
	
	
	/**
	 * @return the room name
	 */
	public String getRoomName();

	/**
	 * @return the IPubSubSyncChannelUpdate 
	 */
	IPubSubSyncChannelUpdate<HashSet<INamedRoomConnection>> getRooms();
	
}
