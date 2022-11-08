package rme5_bk39.msg;

import java.util.Set;

import common.dataPacket.AAppDataPacketAlgoCmd;
import common.dataPacket.AppDataPacket;
import common.dataPacket.data.app.IQuitData;
import common.serverObj.INamedAppConnection;
import provided.datapacket.IDataPacketID;
import rme5_bk39.model.IMainModel2ViewAdpt;

/**
 * @author bosungkim
 * The quit data type cmd
 */
public class QuitDataCmd extends AAppDataPacketAlgoCmd<IQuitData>{

	/**
	 * the serial id
	 */
	private static final long serialVersionUID = 5427043945471062538L;

	/**
	 * the main model 2 main view adapter
	 */
	private IMainModel2ViewAdpt adpt;
	
	/*
	 * the named app connections
	 */
	private Set<INamedAppConnection> contacts;
	
	/**
	 * @param adpt IMainModel2ViewAdpt adapter
	 * @param contacts Set of named app connections
	 */
	public QuitDataCmd(IMainModel2ViewAdpt adpt, Set<INamedAppConnection> contacts) {
		this.adpt = adpt;
		this.contacts = contacts;
	}
	@Override
	public Void apply(IDataPacketID index, AppDataPacket<IQuitData> host, Void... params) {
		// TODO Auto-generated method stub
		contacts.remove(host.getSender());
		adpt.updateContacts(contacts);
		return null;
	}

}
