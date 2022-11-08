package rme5_bk39.chatroom.msg;

import common.dataPacket.data.room.ICmdRequestData;
import provided.datapacket.IDataPacketID;

/**
 * @author rezog
 * The cmd request data
 */
public class CmdRequestData implements ICmdRequestData{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = -8685137732104382510L;
	private IDataPacketID id;
	
	/**
	 * @param id the datapacket id
	 */
	public CmdRequestData(IDataPacketID id) {
		this.id = id;
	}
	

	@Override
	public IDataPacketID getUnknownMsgID() {
		return this.id;
	}

}
