package rme5_bk39.chatroom.msg;

import java.util.ArrayList;
import java.util.HashMap;

import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.ICmd2ModelAdapter;
import common.dataPacket.RoomDataPacket;
import common.dataPacket.RoomDataPacketAlgo;
import common.dataPacket.data.IRoomConnectionData;
import common.dataPacket.data.room.ICmdData;
import provided.datapacket.IDataPacketID;

/**
 * @author rezog
 *
 */
public class CmdDataCmd extends ARoomDataPacketAlgoCmd<ICmdData>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2158156262137631350L;

	/**
	 * a receiver 
	 */
	private RoomDataPacketAlgo receiverVisitor;

	/**
	 * A cache that maps each data packet id to a list of receiver data packets 
	 */
	private HashMap<IDataPacketID, ArrayList<RoomDataPacket<IRoomConnectionData>>> unexecutedMsgs;

	/**
	 * The command 2 model adapter
	 */
	private ICmd2ModelAdapter cmd2ModelAdapter;

	/**
	 * Constructor 
	 * @param receiverVisitor the visitor
	 * @param unexecutedMsgs the messages
	 */
	public CmdDataCmd(RoomDataPacketAlgo receiverVisitor, HashMap<IDataPacketID, ArrayList<RoomDataPacket<IRoomConnectionData>>> unexecutedMsgs) {
		this.receiverVisitor = receiverVisitor;
		this.unexecutedMsgs = unexecutedMsgs;
	}
	
	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<ICmdData> host, Void... params) {
		ARoomDataPacketAlgoCmd<?> receivedCmd = ((ICmdData) host.getData()).getAlgoCmd();
		IDataPacketID id = host.getData().getUnknownMsgID();
		receivedCmd.setCmd2ModelAdpt(cmd2ModelAdapter);
		receiverVisitor.setCmd(id, receivedCmd);
		

		ArrayList<RoomDataPacket<IRoomConnectionData>> toExecute = unexecutedMsgs.get(id);
		unexecutedMsgs.remove(toExecute);
				
		for (RoomDataPacket<IRoomConnectionData> message : unexecutedMsgs.get(id)) {
			message.execute(receiverVisitor);
		}
		unexecutedMsgs.put(id, new ArrayList<RoomDataPacket<IRoomConnectionData>>());
		return null;
	}

	
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
	
		this.cmd2ModelAdapter = cmd2ModelAdpt;
	}

	/**
	 * @return stuff
	 */
	public HashMap<IDataPacketID, ArrayList<RoomDataPacket<IRoomConnectionData>>> getUnexecutedMsgs() {
		return this.unexecutedMsgs;
	}

}
