package rme5_bk39.chatroom.msg;


import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.RoomDataPacket;
import common.dataPacket.data.room.ITextData;
import provided.datapacket.IDataPacketID;
import rme5_bk39.chatroom.miniModel.IMiniModel2ViewAdpt;

/**
 * @author Renzo Espinoza
 * The text data cmd
 */
public class TextDataCmd extends ARoomDataPacketAlgoCmd<ITextData>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6119131021049139943L;
	/**
	 * Adapter.
	 */
	private IMiniModel2ViewAdpt adptr;

	/**
	 * constructor
	 * @param adptr the adapter
	 */
	public TextDataCmd(IMiniModel2ViewAdpt adptr) {
		this.adptr = adptr;
	}

	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<ITextData> host, Void... params) {
		Thread thread = new Thread(() -> {
			this.adptr.displayMsg(host.getSender().getName() + ": " + host.getData().getText() + "\n");
		});
		thread.start();
		System.out.println("SENDING TEXT MESSAGE IN TEXTCMD");
		return null;
	}


}
