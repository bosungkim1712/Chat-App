package rme5_bk39.chatroom.miniModel;


import common.serverObj.INamedRoomConnection;
import common.serverObj.IRoomConnection;

/**
 * @author rezog
 *
 */
public class NamedRoomConnection implements INamedRoomConnection {

	/**
	 * Serialization purpose.
	 */
	private static final long serialVersionUID = -6162987341184298439L;

	/**
	 * The receiver
	 */
	private IRoomConnection receiver;

	//	private transient IMini2ViewAdptr adptr;

	/**
	 * Username
	 */
	private String userName;

	/**
	 * The named connector of the app
	 */
	private IRoomConnection app;

	/**
	 * The Named Receiver constructor
	 * @param receiver the receiver
	 * @param userName the username
	 * @param app the app connector
	 */
	public NamedRoomConnection(String userName, IRoomConnection app) {
		
		this.userName = userName;
		this.app = app;
	}

	@Override
	public String getName() {
		return this.userName;
	}

	
	@Override
	public IRoomConnection getStub() {
		return this.receiver;
	}
	@Override
	public String toString() {
		return this.getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof INamedRoomConnection o)) {
			return false;
		}
		return this.getStub().equals(o.getStub());
	}
	
	@Override
	public int hashCode() {
		return this.getStub().hashCode();
	}

	

}
