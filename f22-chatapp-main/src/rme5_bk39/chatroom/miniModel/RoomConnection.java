package rme5_bk39.chatroom.miniModel;

import java.rmi.RemoteException;

import common.dataPacket.RoomDataPacket;
import common.dataPacket.RoomDataPacketAlgo;
import common.dataPacket.data.IRoomConnectionData;
import common.serverObj.IRoomConnection;

/**
 * @author Renzo Espinoza Bo Sung Kim
 *	A connection for a room
 */
public class RoomConnection implements IRoomConnection {

	private RoomDataPacketAlgo algo;
	
	/**
	 * @param algo The algorithm for the room msg
	 */
	public RoomConnection(RoomDataPacketAlgo algo) {
		this.algo = algo;
	}
	
	@Override
	public void sendMessage(RoomDataPacket<? extends IRoomConnectionData> data) throws RemoteException {
		data.execute(this.algo, null);
	}
	

}
