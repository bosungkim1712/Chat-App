package rme5_bk39.chatroom.msg;

import common.dataPacket.data.room.ITextData;

/**
 * @author Renzo Espinoza Bo Sung Kim
 * Text message
 */
public class TextData implements ITextData{


	/**
	 * 
	 */
	private static final long serialVersionUID = -2052796671054020387L;
	/**
	 * The actual text msg contained.
	 */
	private String text;

	/**
	 * Constructor.
	 * @param text the txt msg.
	 */
	public TextData(String text) {
		this.text = text;
	}


	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return this.text;
	}
	

}
