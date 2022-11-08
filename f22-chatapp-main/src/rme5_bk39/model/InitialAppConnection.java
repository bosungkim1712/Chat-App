package rme5_bk39.model;

import java.rmi.RemoteException;
import java.util.HashSet;

import common.dataPacket.AppDataPacket;
import common.serverObj.IInitialAppConnection;
import common.serverObj.INamedAppConnection;
import rme5_bk39.msg.ConnectionSetData;

/**
 * @author rezog
 * The initial connection of chatapp instance
 */
public class InitialAppConnection implements IInitialAppConnection{
	
	private INamedAppConnection connection;
	
	/**
	 * @param namedConnection The named connection
	 */
	public InitialAppConnection(INamedAppConnection namedConnection) {
		this.connection = namedConnection;
	}

	@Override
	public void receiveNamedConnection(INamedAppConnection namedConnection) throws RemoteException {
		// TODO Auto-generated method stub
		
		// get named connection from someone new that is trying to connect to the network, t
		// then we send them a message with the hash set message
		HashSet<INamedAppConnection> contacts = new HashSet<INamedAppConnection>();
		contacts.add(connection);
		contacts.add(namedConnection);
		namedConnection.getStub().sendMessage(new AppDataPacket(new ConnectionSetData(contacts), connection));
		
	}
	
	/**
	 * @return the named connection
	 */
	public INamedAppConnection getNamedConnection() {
		return this.connection;
	}

}
