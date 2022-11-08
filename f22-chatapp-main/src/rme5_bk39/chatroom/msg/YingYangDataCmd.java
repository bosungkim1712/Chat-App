package rme5_bk39.chatroom.msg;

import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.ICmd2ModelAdapter;
import common.dataPacket.RoomDataPacket;
import common.dataPacket.data.room.ICmdData;
import provided.datapacket.IDataPacketID;

/**
 * @author Renzo Espinoza Bo Sung Kim
 * Ying yang data type cmd
 */
public class YingYangDataCmd extends ARoomDataPacketAlgoCmd<ICmdData>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7338688054377069364L;
	private ICmd2ModelAdapter cmd2Model;

	
	/**
	 * @param cmdAdpt The adapter for the command to model
	 */
	public YingYangDataCmd(ICmd2ModelAdapter cmdAdpt) {
		this.cmd2Model = cmdAdpt;
	}
	
	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<ICmdData> host, Void... params) {
		String message =  "─────────▀▀▀▀▀▀──────────▀▀▀▀▀▀▀\r\n"
				+ "──────▀▀▀▀▀▀▀▀▀▀▀▀▀───▀▀▀▀▀▀▀▀▀▀▀▀▀\r\n"
				+ "────▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀──────────▀▀▀\r\n"
				+ "───▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀──────────────▀▀\r\n"
				+ "──▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀──────────────▀▀\r\n"
				+ "─▀▀▀▀▀▀▀▀▀▀▀▀───▀▀▀▀▀▀▀───────────────▀▀\r\n"
				+ "─▀▀▀▀▀▀▀▀▀▀▀─────▀▀▀▀▀▀▀──────────────▀▀\r\n"
				+ "─▀▀▀▀▀▀▀▀▀▀▀▀───▀▀▀▀▀▀▀▀──────────────▀▀\r\n"
				+ "─▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀───────────────▀▀\r\n"
				+ "─▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀───────────────▀▀\r\n"
				+ "─▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀───────────────▀▀\r\n"
				+ "──▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀───────────────▀▀\r\n"
				+ "───▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀───────────────▀▀▀\r\n"
				+ "─────▀▀▀▀▀▀▀▀▀▀▀▀▀───────────────▀▀▀\r\n"
				+ "──────▀▀▀▀▀▀▀▀▀▀▀───▀▀▀────────▀▀▀\r\n"
				+ "────────▀▀▀▀▀▀▀▀▀──▀▀▀▀▀────▀▀▀▀\r\n"
				+ "───────────▀▀▀▀▀▀───▀▀▀───▀▀▀▀\r\n"
				+ "─────────────▀▀▀▀▀─────▀▀▀▀\r\n"
				+ "────────────────▀▀▀──▀▀▀▀\r\n"
				+ "──────────────────▀▀▀▀\r\n"
				+ "───────────────────▀▀" ;
		
		
		cmd2Model.displayText(host.getSender().getName() + ": " + message + "\n");
		return null;
	}

	

}