package rme5_bk39.chatroom.miniController;

import common.serverObj.INamedAppConnection;
import provided.logger.ILogger;
import provided.pubsubsync.IPubSubSyncManager;
import provided.rmiUtils.IRMIUtils;
import rme5_bk39.model.ChatappConfig;

/**
 * The mini to main adapter
 * @author Renzo Espinoza Bo Sung Kim
 */
public interface IMini2MainAdpt {
	/**
	 * Get the pubsubsync manager to create data channels
	 * @return the manager.
	 */
	public IPubSubSyncManager getPubSubSyncManager();

	// public void removeRoom();

	/**
	 * Upon exit, tell the main to clean up.
	 */
	public void removeRoom();

	/**
	 * @return the logger to be used to display status.
	 */
	public ILogger getLogger();

	/**
	 * @return INamedAppConnection
	 */
	public INamedAppConnection getNamedConnector();

	/**
	 * @return username
	 */
	public String getUserName();


	/**
	 * @return the rmiutils
	 */
	public IRMIUtils getRmiUtils();
	
	/**
	 * @return the config
	 */
	public ChatappConfig getConfig();
}
