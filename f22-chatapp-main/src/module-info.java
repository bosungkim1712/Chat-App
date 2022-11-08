/**
 * 
 */
/**
 * The module for this assignment.   Please change the module name below to match the project name.
 * @author swong
 *
 */
module hw08 {
	requires transitive java.desktop;
	requires transitive java.rmi;
	requires java.base;
	

	
	exports provided.logger;
	exports provided.config;
	exports provided.datapacket;
	exports provided.datapacket.test;
	exports provided.discovery;
	exports provided.discovery.impl;
	exports provided.discovery.impl.model;
	exports provided.discovery.impl.view;
	exports provided.extvisitor;
	exports provided.mixedData;
	exports provided.mixedData.test;
	exports provided.pubsubsync;
	exports provided.pubsubsync.impl;
	exports provided.rmiUtils;
	exports provided.rmiUtils.classServer;
	exports provided.utils;
	exports provided.utils.dispatcher;
	exports provided.utils.dispatcher.impl;
	exports provided.utils.displayModel;
	exports provided.utils.file;
	exports provided.utils.file.impl;
	exports provided.utils.function;
	exports provided.utils.loader;
	exports provided.utils.loader.impl;
	exports provided.utils.logic;
	exports provided.utils.logic.impl;
	exports provided.utils.struct;
	exports provided.utils.struct.impl;
	exports provided.utils.valueGenerator;
	exports provided.utils.valueGenerator.impl;	
	exports provided.utils.view;
	exports common.serverObj;
	exports common.dataPacket;
	exports common.dataPacket.data.app;
	exports common.dataPacket.data.room;
	exports common.dataPacket.data.status;
	exports common.dataPacket.data;
	
	/**
	 *  Add exports for at least the following package and necessary sub-packages: 
	 *   - common
	 *   - student-defined message types and implementations
	 *   - any serialized support packages used by message implementations
	 */

	
}