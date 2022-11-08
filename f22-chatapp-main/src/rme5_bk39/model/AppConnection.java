package rme5_bk39.model;

import java.rmi.RemoteException;

import common.dataPacket.AppDataPacket;
import common.dataPacket.AppDataPacketAlgo;
import common.dataPacket.data.IAppConnectionData;
import common.serverObj.IAppConnection;

/**
 * @author Renzo Espinoza Bo Sung Kim
 *
 */
public class AppConnection implements IAppConnection{
	
	/**
	 * visitor algo
	 */
	private AppDataPacketAlgo connectVisitorAlgo;
	
	/**
	 * config
	 */
	private ChatappConfig config;
	
	/**
	 * @param connectVisitorAlgo connector visitor algorithm
	 * @param config configurations
	 */
	public AppConnection(AppDataPacketAlgo connectVisitorAlgo, ChatappConfig config) {
		this.connectVisitorAlgo = connectVisitorAlgo;
		this.config = config;
		
	}

	@Override
	public void sendMessage(AppDataPacket<? extends IAppConnectionData> packet) throws RemoteException {
		// TODO Auto-generated method stub
		packet.execute(connectVisitorAlgo, null);
	}
	
	/**
	 * @return the config
	 */
	public ChatappConfig getConfig() {
		return this.config;
	}

}
