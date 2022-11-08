package rme5_bk39.chatroom.miniModel;



import java.awt.Component;
import java.util.Set;
import java.util.function.Supplier;

import javax.swing.JComponent;

import common.serverObj.INamedAppConnection;
import common.serverObj.INamedRoomConnection;
import provided.logger.ILogger;
import provided.rmiUtils.IRMIUtils;
import rme5_bk39.model.ChatappConfig;

/**
 * @author Renzo Espinoza
 * ADapter for the mini mvc model to the main view
 */
public interface IMiniModel2ViewAdpt {

	/**
	 * Displays the status
	 * @param status the status of the message
	 */
	public void displayStatus(String status);
	
	/**
	 * @param msg the message to be displayed.
	 */
	public void displayMsg(String msg);

	

	/**
	 * @return all the INamedReceivers in the room.
	 */
	public Set<INamedRoomConnection> getRoomRoster();

	/**
	 * 
	 * @return the system's logger
	 */
	public ILogger getSysLogger();

	/**
	 * 
	 * @return the RMI Utils
	 */
	public IRMIUtils getRmiUtils();

	//	public void updateMemberList(Set<INamedReceiver> namedReceivers);

	/**
	 * 
	 * @return the username
	 */
	public String getUserName();

	/**
	 * Builds a component
	 * @param sup Supplier
	 */
	public void buildComponent(Supplier<JComponent> sup);

	/**
	 * Gets the named connector associated with this app.
	 * @return the named connector
	 */
	public INamedAppConnection getNamedConnector();

	
	/**
	 * gets the view panel
	 * @return the view panel
	 */
	public Component getViewPanel();
	
	/**
	 * gets the config
	 * @return the config
	 */
	public ChatappConfig getConfig();

	
	
	/**
	 * Gets the room name
	 * @return the room name
	 */
	public String getRoomName();
}