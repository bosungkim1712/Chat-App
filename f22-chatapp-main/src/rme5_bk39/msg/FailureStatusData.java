package rme5_bk39.msg;

import common.dataPacket.AppDataPacket;
import common.dataPacket.data.IAppConnectionData;
import common.dataPacket.data.status.IFailureStatusData;
import provided.datapacket.DataPacket;

/**
 * @author rezog
 * A failure status data type
 */
public class FailureStatusData implements IFailureStatusData<IAppConnectionData>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 4757344015924458586L;

	/**
	 * A app connection data packet
	 */
	private AppDataPacket<IAppConnectionData> packet;
	
	/**
	 * A failure msg
	 */
	private String failureMsg;
	
	/**
	 * @param packet the app data packet for an app message
	 * @param failureMsg a failure data type
	 */
	public FailureStatusData(AppDataPacket<IAppConnectionData> packet, String failureMsg) {
		this.packet = packet;
		this.failureMsg = failureMsg;
	}

	

	@Override
	public String getStatusMsg() {
		return this.failureMsg;
	}



	@Override
	public DataPacket<IAppConnectionData, ?> getCausalDataPacket() {
		return this.packet;
	}




	

}