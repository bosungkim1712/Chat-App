package rme5_bk39.msg;

import java.util.HashSet;

import common.dataPacket.data.app.IConnectionSetData;
import common.serverObj.INamedAppConnection;

/**
 * @author bosungkim
 * The connection set message type
 */
public class ConnectionSetData implements IConnectionSetData{


    private static final long serialVersionUID = -7027717794599112623L;
	private HashSet<INamedAppConnection> myContacts;

    /**
     * @param contacts the set of named app connection
     */
    public ConnectionSetData(HashSet<INamedAppConnection> contacts){
        this.myContacts = contacts;
    }

	@Override
	public HashSet<INamedAppConnection> getConnectionSet() {
		return this.myContacts;
	}
    
}
