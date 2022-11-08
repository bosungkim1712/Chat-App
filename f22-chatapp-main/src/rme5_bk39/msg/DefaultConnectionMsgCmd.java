package rme5_bk39.msg;

import common.dataPacket.AAppDataPacketAlgoCmd;
import common.dataPacket.AppDataPacket;
import provided.datapacket.ADataPacket;
import provided.datapacket.IDataPacketID;
import provided.extvisitor.IExtVisitorHost;
import provided.logger.ILogger;
import provided.logger.LogLevel;

/**
 * @author rezog
 * The default connection cmd
 */
public class DefaultConnectionMsgCmd extends AAppDataPacketAlgoCmd{
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 4450983587895072402L;
	/**
	 * The system logger.
	 */
	private ILogger sysLogger;

	/**
	 * Construct a connector msg cmd.
	 * @param sysLogger the logger
	 */
	public DefaultConnectionMsgCmd(ILogger sysLogger) {
		this.sysLogger = sysLogger;

	}


	@Override
	public Void apply(IDataPacketID index, AppDataPacket host, Void... params) {
		sysLogger.log(LogLevel.DEBUG, "Unknown app connection message command received!");
		return null;
	}

}
