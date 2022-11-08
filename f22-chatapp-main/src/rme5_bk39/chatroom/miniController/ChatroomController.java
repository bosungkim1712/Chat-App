package rme5_bk39.chatroom.miniController;


import java.awt.Component;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import javax.swing.JComponent;
import common.dataPacket.RoomDataPacketAlgo;
import common.serverObj.INamedAppConnection;
import common.serverObj.INamedRoomConnection;
import provided.logger.ILogger;
import provided.logger.LogLevel;
import provided.pubsubsync.IPubSubSyncChannelUpdate;
import provided.pubsubsync.IPubSubSyncManager;
import provided.pubsubsync.IPubSubSyncUpdater;
import provided.rmiUtils.IRMIUtils;
import rme5_bk39.IMain2MiniAdpt;
import rme5_bk39.chatroom.miniModel.ChatroomModel;
import rme5_bk39.chatroom.miniModel.IMiniModel2ViewAdpt;
import rme5_bk39.chatroom.miniView.ChatroomView;
import rme5_bk39.chatroom.miniView.IMiniView2ModelAdpt;
import rme5_bk39.model.ChatappConfig;


/**
 * Mini-controller for chatroom. 
 * 
 * @author Renzo Espinoza
 * @author Bo Sung Kim
 *
 */

/**
 * @author Renzo Espinoza
 * Mini Controller, the controller of a chat room
 */
public class ChatroomController {
	/**
	 * The view of a chat room.
	 */
	private ChatroomView view;

	/**
	 * The model behind a chat room.
	 */
	private ChatroomModel model;

	/**
	 * Mini-controller 2 Main model adapter.
	 */
	private IMini2MainAdpt mini2MainAdptr;

	/**
	 * The manager to create data channels, i.e. chat rooms.
	 */
	private IPubSubSyncManager pubSubSyncManager;

	/**
	 * Name of the chat room.
	 */
	private String roomName;

	private ILogger sysLogger;

	/**
	 * The chat room ID.
	 */
	private UUID chatRoomID;

	private IPubSubSyncChannelUpdate<HashSet<INamedRoomConnection>> chatRoom;

	private HashSet<INamedRoomConnection> roster = new HashSet<>();

	private IMain2MiniAdpt chatRoomAdptr;

	/**
	 * Construct a miniController, representing a instance of the chat room!
	 * @param name The name of the room
	 * @param mini2MainAdptr the adapter towards the main model
	 * @param chatRoomID the chatRoomID.
	 */
	public ChatroomController(String name, IMini2MainAdpt mini2MainAdptr) {

		this.mini2MainAdptr = mini2MainAdptr;

		this.pubSubSyncManager = this.mini2MainAdptr.getPubSubSyncManager();

		this.sysLogger = this.mini2MainAdptr.getLogger();

		this.roomName = name;

		model = new ChatroomModel(chatRoomID, name, new IMiniModel2ViewAdpt() {

			@Override
			public void displayMsg(String msg) {
				view.appendMessage(msg);
				
			}
			
			
			@Override
			public void displayStatus(String status) {
				view.appendStatus(status);
			}

			@Override
			public Set<INamedRoomConnection> getRoomRoster() {
				return roster;
			}

			@Override
			public ILogger getSysLogger() {
				return sysLogger;
			}

			@Override
			public IRMIUtils getRmiUtils() {
				return mini2MainAdptr.getRmiUtils();
			}

			@Override
			public String getUserName() {
				return mini2MainAdptr.getUserName();
			}

			@Override
			public void buildComponent(Supplier<JComponent> sup) {
				view.add(sup.get());
			
			}

			@Override
			public INamedAppConnection getNamedConnector() {
				return mini2MainAdptr.getNamedConnector();
			}

			@Override
			public Component getViewPanel() {
				return view;
			}

			@Override
			public String getRoomName() {
				return name;
			}


			@Override
			public ChatappConfig getConfig() {
				return mini2MainAdptr.getConfig();
			}
			

			
		});

		view = new ChatroomView(new IMiniView2ModelAdpt() {


			@Override
			public Set<String> getRoomRoster() {
				Set<INamedRoomConnection> roster = model.getRoomRoster();
				Set<String> stringRoster = new HashSet<>();
				for (INamedRoomConnection person : roster) {
					stringRoster.add(person.toString());
				}
				return stringRoster;
			}


			@Override
			public void sendMessage(String msg) {
				// TODO Auto-generated method stub
				model.sendStringMsg(msg);
				
			}

			@Override
			public void leave() {
				stop();
			
			}


			@Override
			public void sendYingYangMsg() {
				model.sendYingYangMsg();
			}

			


		});

	}

	/**
	 * Start the chat room by giving the room a manager.
	 * @param pubSubSyncManager data channel manager.
	 */
	public void start() {
		model.start();
		view.start();

		chatRoomAdptr = new IMain2MiniAdpt() {

			@Override
			public INamedRoomConnection getNamedReceiver() {
				return model.getMyNamedReceiver();
			}

			@Override
			public Component getRoomPanel() {
				return view;
			}

			@Override
			public void start() {
				ChatroomController.this.start();

			}

			@Override
			public UUID getChatRoomID() {
				return getRoomID();
			}

			@Override
			public String getRoomName() {
				return roomName;
			}

			@Override
			public void quit() {
				stop();
			}
			
			@Override
			public IPubSubSyncChannelUpdate<HashSet<INamedRoomConnection>> getRooms() {
				return chatRoom;
			}

			@Override
			public void finalExit() {
				// TODO Auto-generated method stub
				finalExiting();
				
			}
		};
	}

	/**
	 * Makes a new room.
	 */
	public void makeNewRoom() {
		Set<String> nameRoster = new HashSet<>();
		/*
		* The (reference to the) roster will be passed to the mini controller so that it gets updated whenever 
		* the data channel changes.
		*/
		chatRoom = pubSubSyncManager.createChannel(this.roomName, new HashSet<INamedRoomConnection>(), (pubSubSyncData) -> {
			roster.clear();
			//System.out.println(roster);
			nameRoster.clear();
			roster.addAll(pubSubSyncData.getData());
			//System.out.println("CHATROOM ROSTER:");
			//System.out.println(roster);
			//System.out.println();
			for (INamedRoomConnection person : roster) {
				nameRoster.add(person.getName());
			}
			view.updateRoomRoster(nameRoster);
		}, (statusMessage) -> {
			//view.appendStatus(statusMessage);
			sysLogger.log(LogLevel.DEBUG, "room " + this.roomName + " has been left sucessfully.");
		});

		this.chatRoomID = chatRoom.getChannelID();
		chatRoom.update(IPubSubSyncUpdater.makeSetAddFn(model.getMyNamedReceiver()));
		System.out.println("MAKING NEW ROOM... YOUR RECEIVER IS... " +model.getMyNamedReceiver());
	}

	/**
	 * Joins a room
	 * @param roomID the room ID
	 */
	public void joinRoom(UUID roomID) {
		Set<String> nameRoster = new HashSet<>();
		/*
		* The (reference to the) roster will be passed to the mini controller so that it gets updated whenever 
		* the data channel changes.
		*/
		chatRoom = pubSubSyncManager.subscribeToUpdateChannel(roomID, (pubSubSyncData) -> {
			roster.clear();
			nameRoster.clear();
			roster.addAll(pubSubSyncData.getData());
			//System.out.println("CHATROOM ROSTER:");
			//System.out.println(roster);
			//System.out.println();
			for (INamedRoomConnection person : roster) {
				nameRoster.add(person.getName());
			}
			view.updateRoomRoster(nameRoster);
		}, (statusMessage) -> {
			sysLogger.log(LogLevel.DEBUG, "room " + this.roomName + " has been left sucessfully.");
		});

		this.chatRoomID = roomID;
		chatRoom.update(IPubSubSyncUpdater.makeSetAddFn(model.getMyNamedReceiver()));
	}

	/**
	 * Stop the current chat room - i.e. remove myself from the room.
	 */
	public void stop() {
		//System.out.println("IN STOP");
		//System.out.println("LEAVING ROOM... YOUR RECEIVER IS... " +model.getMyNamedReceiver());
		chatRoom.update(IPubSubSyncUpdater.makeSetRemoveFn(model.getMyNamedReceiver()));
		chatRoom.unsubscribe();
		this.mini2MainAdptr.removeRoom();
	}
	
	
	public void finalExiting() {
		//System.out.println("IN STOP");
		//System.out.println("LEAVING ROOM... YOUR RECEIVER IS... " +model.getMyNamedReceiver());
		chatRoom.update(IPubSubSyncUpdater.makeSetRemoveFn(model.getMyNamedReceiver()));
		chatRoom.unsubscribe();
	}
	
	
	

	/**
	 * @return the chat room id.
	 */
	public UUID getRoomID() {
		return chatRoomID;
	}

	/**
	 * @return the current chat room panel;
	 */
	public Component getMyRoomPanel() {
		return this.view;
	}
	
	/**
	 * 
	 * @return the receiver message algo
	 */
	public RoomDataPacketAlgo getReceiverMsgAlgo() {
		return this.model.getReceiverMsgAlgo();
	}

	/**
	 * 
	 * @return the main to mini adapter
	 */
	public IMain2MiniAdpt getRoomAdptr() {
		return chatRoomAdptr;
	}
	


}