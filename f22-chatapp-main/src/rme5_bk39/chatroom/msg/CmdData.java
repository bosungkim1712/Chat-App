package rme5_bk39.chatroom.msg;


import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.data.IRoomConnectionData;
import common.dataPacket.data.room.ICmdData;
import provided.datapacket.IDataPacketID;

/**
 * @author Renzo Espinoza
 * The cmd data
 * @param <D> Room connection data
 */
public class CmdData<D extends IRoomConnectionData> implements ICmdData<D> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1273964658462151289L;
	/**
	 * the cmd
	 */
	private ARoomDataPacketAlgoCmd<?> cmd;
	/**
	 * the id
	 */
	private IDataPacketID cmdId;

	/**
	 * @param cmdId the algo Id.
	 * @param cmd the algo cmd
	 */
	public CmdData(ARoomDataPacketAlgoCmd<?> cmd, IDataPacketID cmdId) {
		this.cmd = cmd;
		this.cmdId = cmdId;
	}


	@Override
	public IDataPacketID getUnknownMsgID() {
		return this.cmdId;
	}

	@Override
	public ARoomDataPacketAlgoCmd getAlgoCmd() {
		return this.cmd;
	}


}