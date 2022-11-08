package rme5_bk39.chatroom.msg;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.RoomDataPacket;
import common.dataPacket.data.IRoomConnectionData;
import common.dataPacket.data.room.ICmdRequestData;

import common.serverObj.INamedRoomConnection;
import provided.datapacket.IDataPacketID;

/**
 * The default receiver msg cmd
 * @author Renzo Espinoza Bo Sung Kim
 */
public class DefaultReceiverMsgCmd extends ARoomDataPacketAlgoCmd<IRoomConnectionData> {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7678324618698194234L;

	/**
	 * not executed messages.
	 */
	private HashMap<IDataPacketID, ArrayList<RoomDataPacket<IRoomConnectionData>>> unexecutedMsgs;
	
	private INamedRoomConnection myReceiver;
	
	
	/**
	 * DefaultReceiverMsgCmd to process the 
	 * @param unexecutedMsgs essagmes left to be executed
	 */
	public DefaultReceiverMsgCmd(HashMap<IDataPacketID, ArrayList<RoomDataPacket<IRoomConnectionData>>> unexecutedMsgs) {
		this.unexecutedMsgs = unexecutedMsgs;

	}
	

	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<IRoomConnectionData> host, Void... params) {
		// TODO Auto-generated method stub
		System.out.println("In default receiver");
		if (!unexecutedMsgs.containsKey(index)) {
			ArrayList<RoomDataPacket<IRoomConnectionData>> list = new ArrayList<>();
			list.add(host);
			unexecutedMsgs.put(index, list);
		}
		else {
			unexecutedMsgs.get(index).add(host);
		}
		
		Thread thread = new Thread(() -> {
			try {
				host.getSender().getStub().sendMessage(new RoomDataPacket<ICmdRequestData>(
						ICmdRequestData.make(host.getData().getID()), myReceiver));
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			}
		});
		System.out.println("this are the unexecutedmsg: " + unexecutedMsgs.toString());
		thread.start();
		return null;
	}
	
	/**
	 * @param rec The named room
	 */
	public void setReceiver(INamedRoomConnection rec) {
		this.myReceiver = rec;
	}


	
}

	

