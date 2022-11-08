package rme5_bk39.chatroom.msg;

import common.dataPacket.data.IRoomConnectionData;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * @author rezog
 * Ying Yang data type
 */
public class YingYangData implements IRoomConnectionData{


	/**
	 * serial id
	 */
	private static final long serialVersionUID = 3080012731813727122L;
	
	/**
	 * @return the datapacketid
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(YingYangData.class);
	}

	@Override
	public IDataPacketID getID() {
		// TODO Auto-generated method stub
		return YingYangData.GetID();
	}


}