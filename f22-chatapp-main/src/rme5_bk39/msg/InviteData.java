package rme5_bk39.msg;

import java.util.UUID;
import common.dataPacket.data.app.IInviteData;

/**
 * @author Renzo Espinoza
 * The invite data type
 */
public class InviteData implements IInviteData{

	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = -30495145492995682L;

	/**
	 * The room ID; 
	 */
	private UUID roomID;

	/**
	 * The room name. 
	 */
	private String roomName;

	/**
	 * @param roomID the room Id.
	 * @param roomName the room name.
	 */
	public InviteData(UUID roomID, String roomName) {
		this.roomID = roomID;
		this.roomName = roomName;
	}
	

	@Override
	public String getFriendlyName() {
		// TODO Auto-generated method stub
		return this.roomName;
	}

	@Override
	public UUID getRoomUUID() {
		// TODO Auto-generated method stub
		return this.roomID;
	}

}
