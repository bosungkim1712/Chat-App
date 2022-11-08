package rme5_bk39.chatroom.msg;

import java.rmi.RemoteException;

import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.RoomDataPacket;
import common.dataPacket.RoomDataPacketAlgo;
import common.dataPacket.data.IRoomConnectionData;
import common.dataPacket.data.room.ICmdData;
import common.dataPacket.data.room.ICmdRequestData;

import common.serverObj.INamedRoomConnection;
import provided.datapacket.IDataPacketID;

/**
 * @author rezog
 *
 */
public class CmdRequestDataCmd extends ARoomDataPacketAlgoCmd<ICmdRequestData>{

	private RoomDataPacketAlgo receiverVisitor;
	private INamedRoomConnection self;

	/**
	 * @param self my named room connection
	 * @param receiverVisitor receiver visitor
	 */
	public CmdRequestDataCmd(INamedRoomConnection self, RoomDataPacketAlgo receiverVisitor) {
		this.receiverVisitor = receiverVisitor;
		this.self = self;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -452405725602635887L;




	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<ICmdRequestData> host, Void... params) {

		// TODO Auto-generated method stub
		IDataPacketID cmdId = host.getData().getUnknownMsgID();
		ARoomDataPacketAlgoCmd<?> cmd = (ARoomDataPacketAlgoCmd<?>) receiverVisitor.getCmd(cmdId);
		System.out.println("RECEIVED request" + cmd);
		Thread thread = new Thread(() -> {
			try {
				host.getSender().getStub().sendMessage(new RoomDataPacket<IRoomConnectionData>(
						ICmdData.make(cmdId, cmd), self));
			} catch (RemoteException e) {

			}
		});
		thread.start();
		return null;
	}


	

}
