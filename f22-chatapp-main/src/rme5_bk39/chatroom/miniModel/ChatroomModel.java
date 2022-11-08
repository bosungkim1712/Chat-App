package rme5_bk39.chatroom.miniModel;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import javax.swing.JComponent;

import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.ICmd2ModelAdapter;
import common.dataPacket.RoomDataPacket;
import common.dataPacket.RoomDataPacketAlgo;
import common.dataPacket.data.IRoomConnectionData;
import common.dataPacket.data.room.ICmdData;
import common.dataPacket.data.room.ICmdRequestData;
import common.dataPacket.data.room.ITextData;
import common.dataPacket.data.status.IFailureStatusData;
import common.serverObj.INamedRoomConnection;
import common.serverObj.IRoomConnection;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;
import provided.logger.ILogEntry;
import provided.logger.ILogEntryFormatter;
import provided.logger.ILogEntryProcessor;
import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;
import rme5_bk39.chatroom.msg.CmdDataCmd;
import rme5_bk39.chatroom.msg.CmdRequestDataCmd;
import rme5_bk39.chatroom.msg.DefaultReceiverMsgCmd;
import rme5_bk39.chatroom.msg.TextData;
import rme5_bk39.chatroom.msg.TextDataCmd;
import rme5_bk39.chatroom.msg.YingYangData;
import rme5_bk39.chatroom.msg.YingYangDataCmd;
import rme5_bk39.model.ChatappConfig;

/**
 * Model for the chatroom
 * @author Renzo Espinoza
 *
 */
public class ChatroomModel {

	/**
	 * The mini to view adapter
	 */
	private IMiniModel2ViewAdpt adptr;

	/**
	 * The receiver visitor
	 */
	private RoomDataPacketAlgo receiverVisitor;

	/**
	 * The room roster
	 */
	private Set<INamedRoomConnection> roomRoster;

	/**
	 * the message cache
	 */
	private HashMap<IDataPacketID, ArrayList<RoomDataPacket<IRoomConnectionData>>> unexecutedMsgs = new HashMap<>();

	/**
	 * The receiver associated with the chatroom
	 */
	private IRoomConnection myReceiver;
	
	/**
	 * The named receiver
	 */
	private INamedRoomConnection myNamedReceiver;
	
	/**
	 * view logger
	 */
	private ILogger viewLogger;
	
	/**
	 * Sys logger
	 */
	private ILogger sysLogger;
	
	private ChatappConfig config;

	/**
	 * the command 2 model adapter
	 */
	private ICmd2ModelAdapter cmd2ModelAdapter = new ICmd2ModelAdapter() {


		/**
		 * Puts a key-value pair in the mixed dictionary
		 */
		

		@Override
		public void displayText(String text) {
			adptr.displayMsg(text);
			
		}

		@Override
		public String getUsername() {
			return adptr.getUserName();
		}

		@Override
		public String getRoomName() {
			return adptr.getRoomName();
		}

		@Override
		public void displayJComponent(String label, Supplier<JComponent> component) {
			adptr.buildComponent(component);
			component.get().setName(label);
			
		}

		@Override
		public <T extends IRoomConnectionData> void sendMessageToRoom(T data) {
			for (INamedRoomConnection person : roomRoster) {
				this.sendMessageToDyad(data, person);
			}
			//System.out.println("In send to room");
		}

		@Override
		public <T extends IRoomConnectionData> void sendMessageToDyad(T data, INamedRoomConnection dyad) {
			if (roomRoster.contains(dyad)) {
				try {
					RoomDataPacket<T> message = new RoomDataPacket<T>(data, myNamedReceiver);
					dyad.sendMessage(message);
				} catch (RemoteException e) {
					sysLogger.log(LogLevel.ERROR, "Request to send message failed");
					RoomDataPacket<T> message = new RoomDataPacket<T>(data, myNamedReceiver);
					try {
						dyad.getStub().sendMessage(new RoomDataPacket<IFailureStatusData>(IFailureStatusData.make(message, "unable to send quit network message."), myNamedReceiver));
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
					
				}
			}
			
		}

		
	};



	/**
	 * @param id the id of the cmd
	 * @param adptr the mini 2 model adapter 
	 * @param friendlyName the name of the user
	 */
	public ChatroomModel(UUID id, String friendlyName, IMiniModel2ViewAdpt adptr) {
		this.adptr = adptr;

		// Logger to log the status to the console.
		sysLogger = adptr.getSysLogger();
		viewLogger = ILoggerControl.makeLogger(new ILogEntryProcessor() {
			ILogEntryFormatter formatter = ILogEntryFormatter.MakeFormatter("[%1s] %2s");

			@Override
			public void accept(ILogEntry logEntry) {
				// TODO Auto-generated method stub
				ChatroomModel.this.adptr.displayMsg(formatter.apply(logEntry));
			}

		}, LogLevel.INFO);
		viewLogger.append(sysLogger);

		this.config = adptr.getConfig();
		// room information
		this.roomRoster = adptr.getRoomRoster();
		DefaultReceiverMsgCmd dCmd = new DefaultReceiverMsgCmd(unexecutedMsgs);
		receiverVisitor = new RoomDataPacketAlgo(dCmd);
		this.myReceiver = new RoomConnection(this.receiverVisitor);

		try {
			IRoomConnection receiverStub = (IRoomConnection) UnicastRemoteObject.exportObject(this.myReceiver, config.getPortRMI());
			//this.myNamedReceiver = new NamedRoomConnection(this.adptr.getUserName(),receiverStub);
			this.myNamedReceiver = INamedRoomConnection.make(this.adptr.getUserName(), receiverStub);
		} catch (Exception e) {
			sysLogger.log(LogLevel.ERROR, "Can't make receiver stub");
			e.printStackTrace();
		}
		dCmd.setReceiver(myNamedReceiver);
	}

	/**
	 * Initializes commands, including sample HeartMsg and TabMsg unknown commands
	 */
	
	
	private void initVisitor() {
		receiverVisitor.setCmd(DataPacketIDFactory.Singleton.makeID(ITextData.class), new TextDataCmd(adptr));
		receiverVisitor.setCmd(ICmdRequestData.GetID(), new ARoomDataPacketAlgoCmd<ICmdRequestData>() {

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<ICmdRequestData> host, Void... params) {
				IDataPacketID cmdId = host.getData().getUnknownMsgID();
				ARoomDataPacketAlgoCmd<?> cmd = (ARoomDataPacketAlgoCmd<?>) receiverVisitor.getCmd(cmdId);
				System.out.println("RECEIVED request" + cmd);
				Thread thread = new Thread(() -> {
					try {
						System.out.println("About to send message with command to: "+host.getSender().getName());
						host.getSender().getStub().sendMessage(new RoomDataPacket<IRoomConnectionData>(
								ICmdData.make(cmdId, cmd), myNamedReceiver));
					} catch (RemoteException e) {

					}
				});
				thread.start();
				return null;
			}
			
		});
		receiverVisitor.setCmd(YingYangData.GetID(), new YingYangDataCmd(cmd2ModelAdapter));
		CmdDataCmd cmd = new CmdDataCmd(receiverVisitor, unexecutedMsgs);
		cmd.setCmd2ModelAdpt(cmd2ModelAdapter);
		receiverVisitor.setCmd(ICmdData.GetID(), new ARoomDataPacketAlgoCmd<ICmdData>() {

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<ICmdData> host, Void... params) {
				ARoomDataPacketAlgoCmd<?> receivedCmd = ((ICmdData) host.getData()).getAlgoCmd();
				IDataPacketID id = host.getData().getUnknownMsgID();
				receivedCmd.setCmd2ModelAdpt(cmd2ModelAdapter);
				receiverVisitor.setCmd(id, receivedCmd);
				

				ArrayList<RoomDataPacket<IRoomConnectionData>> toExecute = unexecutedMsgs.get(id);
				unexecutedMsgs.remove(toExecute);
						
				for (RoomDataPacket<IRoomConnectionData> message : unexecutedMsgs.get(id)) {
					message.execute(receiverVisitor);
				}
				unexecutedMsgs.put(id, new ArrayList<RoomDataPacket<IRoomConnectionData>>());
				return null;
			}
			
		});
	}


	/**
	 * Gets the room roster
	 * @return the room roster
	 */
	public Set<INamedRoomConnection> getRoomRoster() {
		return this.roomRoster;
	}

	/**
	 * 
	 * @return the named receiver
	 */
	public INamedRoomConnection getMyNamedReceiver() {
		return this.myNamedReceiver;
	}

	/**
	 * @return the receiver message algo (visitor)
	 */
	public RoomDataPacketAlgo getReceiverMsgAlgo() {
		return this.receiverVisitor;
	}

	/**
	 * start the chat room - create a pubsubsync manager
	 */
	public void start() {
		this.initVisitor();
	}

	/**
	 * Sends threaded message
	 * @param receiver the named receiver
	 * @param message the data packet
	 */
	public void sendThreadedMessage(INamedRoomConnection receiver, RoomDataPacket<? extends IRoomConnectionData> message) {
		System.out.println("IN SEND THREADED MESSAGE");
		Thread thread = new Thread(() -> {
			try {
				receiver.getStub().sendMessage(message);
			} catch (Exception e) {
				// TODO: handle exception
				try {
					receiver.getStub().sendMessage(new RoomDataPacket<IFailureStatusData>(IFailureStatusData.make(message, "unable to send message."), myNamedReceiver));
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		});
		thread.start();
	}

	/**
	 * Send a string message to the chat room.
	 * @param msg the message to be sent
	 */
	public void sendStringMsg(String msg) {
		System.out.println("IN SEND STRING MSG");
		System.out.println("CURRENT ROSTER in MODEL: " + this.roomRoster);
		ITextData stringMsg = new TextData(msg);
		this.sendMsg(stringMsg);
	}

	

	/**
	 * @param msg the message to be sent; can be any IRecevierMsg.
	 */
	private void sendMsg(IRoomConnectionData msg) {
		for (INamedRoomConnection person : this.roomRoster) {
			try {
				((INamedRoomConnection)(person)).getStub().sendMessage(new RoomDataPacket<IRoomConnectionData>(msg, this.myNamedReceiver));
			} catch (RemoteException e) {
				adptr.displayMsg("Message \"" + msg.toString() + "\" failed to be sent!.");
				e.printStackTrace();
			}
		}adptr.displayStatus(msg.toString());
	}

	/**
	 * Sends a yingyang message
	 */
	public void sendYingYangMsg(){
		//System.out.println("SENDING YING YANG MESSAGE");
		YingYangData msg = new YingYangData();
		this.sendMsg(msg);
	}
	
	
}