package rme5_bk39.msg;

import common.dataPacket.AAppDataPacketAlgoCmd;
import common.dataPacket.AppDataPacket;
import common.dataPacket.data.app.IInviteData;
import provided.datapacket.IDataPacketID;
import rme5_bk39.model.IMainModel2ViewAdpt;

/**
 * @author rezog
 * The invite data cmd
 */
public class InviteDataCmd extends AAppDataPacketAlgoCmd<IInviteData>{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 6008924651855337036L;
	/**
	 * The main model 2 main view adapater
	 */
	private IMainModel2ViewAdpt adpt;
	
	/**
	 * @param adpt main model 2 main view adapter
	 */
	public InviteDataCmd(IMainModel2ViewAdpt adpt) {
		this.adpt = adpt;
	}
	@Override
	public Void apply(IDataPacketID index, AppDataPacket<IInviteData> host, Void... params) {
		// TODO Auto-generated method stub
		Thread t = new Thread(() -> {
			System.out.println("Running...");
			adpt.join(host.getData().getRoomUUID(), host.getData().getFriendlyName());
			System.out.println("Done!");
			
		});
		t.start();
		return null;
	}
	
	

}
