package rme5_bk39.model;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import common.dataPacket.AAppDataPacketAlgoCmd;
import common.dataPacket.AppDataPacket;
import common.dataPacket.AppDataPacketAlgo;
import common.dataPacket.data.IAppConnectionData;
import common.dataPacket.data.app.IConnectionSetData;
import common.dataPacket.data.app.IInviteData;
import common.dataPacket.data.app.IQuitData;
import common.dataPacket.data.status.IErrorStatusData;
import common.dataPacket.data.status.IFailureStatusData;
import common.dataPacket.data.status.IRejectStatusData;
import common.serverObj.IAppConnection;
import common.serverObj.IInitialAppConnection;
import common.serverObj.INamedAppConnection;
import provided.datapacket.IDataPacketID;
import provided.logger.ILogEntry;
import provided.logger.ILogEntryFormatter;
import provided.logger.ILogEntryProcessor;
import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;
import provided.pubsubsync.IPubSubSyncConnection;
import provided.pubsubsync.IPubSubSyncManager;
import provided.pubsubsync.IPubSubSyncUpdater;
import provided.rmiUtils.RMIUtils;
import rme5_bk39.IMain2MiniAdpt;
import rme5_bk39.msg.ConnectionSetData;
import rme5_bk39.msg.DefaultConnectionMsgCmd;
import rme5_bk39.msg.InviteDataCmd;
import rme5_bk39.msg.QuitData;
import rme5_bk39.msg.QuitDataCmd;


/**
 * @author Renzo Espinoza Bo Sung Kim
 * Responsible for the setup of a chat app instance.
 */
public class MainModel {
	
	/**
	 * rrmi utils
	 */
	private RMIUtils rmi;
	
	/**
	 * the system logger
	 */
	private ILogger sysLog = ILoggerControl.makeLogger(LogLevel.DEBUG);

	/**
	 * the view logger
	 */
    private ILogger viewLog;
	
    /**
     * the registry
     */
	private Registry registry;
	
	/**
	 * the pubsubmanager
	 */
    private IPubSubSyncManager pubSubManager;

    /**
     * the connected managers
     */
	private HashSet<INamedAppConnection> connectedManagers = new HashSet<>();
	
	/**
	 * the view adapters
	 */
	private IMainModel2ViewAdpt viewAdpt;
	
	/**
	 * all  the chatrooms
	 */
	private List<IMain2MiniAdpt> allChatrooms;
    
	/**
	 * The connector representing the current chat app instance.
	 */
	private IAppConnection myConnector;
	
	/**
	 * The stub of the current chat app instance.
	 */
	private IAppConnection myConnectorStub;
	
	/**
	 * the configuration
	 */
	private ChatappConfig config;
    
	/**
	 * my named connection isntance 
	 */
    private INamedAppConnection myNamedConnection;
    
    /**
     * the registry stub
     */
    private IInitialAppConnection registryStub;
    /**
     * the initial server
     */
    private IInitialAppConnection initialServer;
    
    /**
    * Visitor to process all data packets circulating in the chatroom
    * associated with the data processMessager.
    */
    private AppDataPacketAlgo connectAlgo;

    /**
     * Sets up visitor for the model
     */
    private void setUpVisitor() {
		
    	
		this.connectAlgo.setCmd(IInviteData.GetID(), new InviteDataCmd(viewAdpt));
		this.connectAlgo.setCmd(IQuitData.GetID(), new QuitDataCmd(this.viewAdpt, this.connectedManagers));

		this.connectAlgo.setCmd(IFailureStatusData.GetID(), new AAppDataPacketAlgoCmd<IFailureStatusData>(){

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IFailureStatusData> host, Void... params) {
				// TODO Auto-generated method stub
				viewAdpt.displayStatusMsg("Failure Status: " + host.getData().getStatusMsg());
				return null;
			}
			
		});

		this.connectAlgo.setCmd(IErrorStatusData.GetID(), new AAppDataPacketAlgoCmd<IErrorStatusData>(){

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IErrorStatusData> host, Void... params) {
				// TODO Auto-generated method stub
				viewAdpt.displayStatusMsg("Error Status: " + host.getData().getStatusMsg());
				return null;
			}
			
		});

		this.connectAlgo.setCmd(IRejectStatusData.GetID(), new AAppDataPacketAlgoCmd<IRejectStatusData>(){

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IRejectStatusData> host, Void... params) {
				// TODO Auto-generated method stub
				viewAdpt.displayStatusMsg("Reject Status: " + host.getData().getStatusMsg());
				return null;
			}});
    	
		this.connectAlgo.setCmd(IConnectionSetData.GetID(), new AAppDataPacketAlgoCmd<IAppConnectionData>() {

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IAppConnectionData> host, Void... params) {
				if (!connectedManagers.contains(host.getSender()) && !host.getData().equals(myNamedConnection)) {
					addContact(host.getSender());
					//connectedManagers.add((INamedAppConnection)host.getSender());
					//adptr.addContact
				
				
					new Thread() {
						public void run() {
							try {
								((INamedAppConnection) host.getSender()).getStub().sendMessage(new AppDataPacket<IAppConnectionData>(IConnectionSetData.make(connectedManagers), myNamedConnection));
							} catch(RemoteException e) {
								e.printStackTrace();
								sysLog.log(LogLevel.ERROR, "Could not connect back to" + ((IConnectionSetData) host.getData()).getConnectionSet());
							}
						}
					}.start();
					
					HashSet<INamedAppConnection> namedConnections = ((IConnectionSetData)host.getData()).getConnectionSet(); 
					
					
					for (INamedAppConnection newPeer : namedConnections) {
						if (!connectedManagers.contains(newPeer) && !connectedManagers.equals(myNamedConnection)) {
							connectedManagers.add(newPeer);
							//adptr.addContact(newPeer);
							new Thread() {
								public void run() {
									try {
										newPeer.getStub().sendMessage(new AppDataPacket<IAppConnectionData>(IConnectionSetData.make(connectedManagers), myNamedConnection));
									}catch(RemoteException e) {
										e.printStackTrace();
										sysLog.log(LogLevel.ERROR, "Could not connect back to " + myNamedConnection.getName());
									}
								}
							}.start();
						}
					}
				}else {
					new Thread() {
						public void run() {
							try {
								((INamedAppConnection) host.getSender()).getStub().sendMessage(new AppDataPacket<IAppConnectionData>(
										IRejectStatusData.make(host, "Already connected to" + myNamedConnection.getName()), myNamedConnection));
							} catch(RemoteException e) {
								e.printStackTrace();
								sysLog.log(null);
							}
						}
					}.start();
				}
				sysLog.log(LogLevel.INFO, "Successfully connected to " + ((INamedAppConnection) host.getSender()).getName());
				return null;
			}
			
		});


    }
    
	
	/**
	 * @param logger Logger 
	 * @param chatAppConfig config
	 * @param model2ViewAdpt adapter
	 */
	public MainModel(ILogger logger, ChatappConfig chatAppConfig, IMainModel2ViewAdpt model2ViewAdpt) {
		this.sysLog = logger;
		this.viewAdpt = model2ViewAdpt;
		this.config = chatAppConfig;
		this.allChatrooms = new ArrayList<>();
		
		rmi = new RMIUtils(logger);
		
		// The view logger will display itself through the main view.
		viewLog = ILoggerControl.makeLogger(new ILogEntryProcessor() {
			ILogEntryFormatter formatter = ILogEntryFormatter.MakeFormatter("[%1s] %2s");

			@Override
			public void accept(ILogEntry logEntry) {
				MainModel.this.viewAdpt.displayStatusMsg(formatter.apply(logEntry));
			}

		}, LogLevel.INFO);
		
		// Chain the system logger to the end of the view logger so that 
		// anything logged to the view also goes to the system log 
		// (default = to console).
		viewLog.append(sysLog);
		AAppDataPacketAlgoCmd defaultCmd = new DefaultConnectionMsgCmd(this.sysLog);
		this.connectAlgo = new AppDataPacketAlgo(defaultCmd);
		
		this.initialServer = new IInitialAppConnection() {
			@Override
			public void receiveNamedConnection(INamedAppConnection namedConnection) throws RemoteException {
				// TODO Auto-generated method stub
				//connectedManagers.add(namedConnection);
				//addContact(namedConnection);
				namedConnection.getStub().sendMessage(new AppDataPacket(new ConnectionSetData(connectedManagers), myNamedConnection));
			}
		};
		
		new IAppConnection() {

			@Override
			public void sendMessage(AppDataPacket<? extends IAppConnectionData> data) throws RemoteException {
				// TODO Auto-generated method stub
				data.execute(connectAlgo, null);
			}
			
		};

		
	}
	
	/**
	 * Starts our chatapp instance
	 */
	public void start() {
		
		// must start the rmi-utils first
		System.out.print("START model");
		this.rmi.startRMI(this.config.getPortClassServer());
		// must be here rather than the constructor to ensure the RMI has already started
		try {
			pubSubManager = IPubSubSyncConnection.getPubSubSyncManager(this.sysLog, this.rmi, this.config.getPortRMI());
		} catch (RemoteException e) {
			this.sysLog.log(LogLevel.ERROR, "Failed to create a pubsubSync Manager.");
			e.printStackTrace();
			System.exit(1);
		}
		// make the Connector and the namedConnector
		this.myConnector = new AppConnection(connectAlgo, config);
		try {
			this.myConnectorStub = (IAppConnection) UnicastRemoteObject.exportObject(this.myConnector,
					config.getPortRMI());
			
		} catch (RemoteException e1) {
			sysLog.log(LogLevel.ERROR, "Unable to make connector stub!");
			e1.printStackTrace();
		}
		
		this.myNamedConnection = INamedAppConnection.make(config.getName(), myConnectorStub);
		this.addContact(myNamedConnection);
		try {
			this.registryStub = (IInitialAppConnection) UnicastRemoteObject.exportObject(this.initialServer,
					config.getPortRMI());
			
		} catch (RemoteException e1) {
			sysLog.log(LogLevel.ERROR, "Unable to make connector stub!");
			e1.printStackTrace();
		}
		// Bind the connector to the discovery server
		try {
			registry = rmi.getLocalRegistry();
			viewLog.log(LogLevel.INFO, "Local Registry = " + registry);
		} catch (Exception e) {
			viewLog.log(LogLevel.ERROR, "Exception while intializing RMI: " + e);
			e.printStackTrace();
			quit(); // exit the program.
		}

		try {
			registry.rebind(this.config.getName(), this.registryStub);
			viewLog.log(LogLevel.INFO, this.config.getName()+" sucessfully binded to the discovery server!");

		} catch (Exception e) {
			viewLog.log(LogLevel.ERROR, "Failed to bind to the discovery server");
			e.printStackTrace();
			quit(); // exit the program.
		}
			
		//this.viewAdpt.updateContacts(connectedManagers);
			
		this.setUpVisitor();
		System.out.print("DONE STARTING main model");
  
	}
	
	/**
	 * Quits the chatapp
	 *
	 */
	public void quit() {
		List<IMain2MiniAdpt> copy = new ArrayList();
		for (int i = 0; i<this.allChatrooms.size(); i++) {
			copy.add(allChatrooms.get(i));
		}
		for (int i = 0; i < copy.size(); i++) {
			//this.pubSubManager.closeChannel(allChatrooms.get(i).getChatRoomID());
			//this.allChatrooms.get(i).getRooms().update(IPubSubSyncUpdater.makeSetRemoveFn(this.allChatrooms.get(i).getNamedReceiver()));
			//this.allChatrooms.get(i).getRooms().unsubscribe();
			copy.get(i).finalExit();
			//this.removeRoom(i);
		}
		System.out.println(copy.size());
		Set<INamedAppConnection> copyContacts = new HashSet<INamedAppConnection>(this.connectedManagers);
		QuitData quitMsg2 = new QuitData();
		AppDataPacket<IQuitData> quitM = new AppDataPacket<IQuitData>(quitMsg2, this.myNamedConnection); 
    	copyContacts.forEach((entry)->{
    		try {
				entry.getStub().sendMessage(quitM);
				System.out.println("Getting stubs and sending messages");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block				entry.getStub().sendMessage(new AppDataPacket<IFailureStatusData>(quitMsg2, this.myNamedConnection), myNamedConnection);

				//entry.getStub().sendMessage(new FailureStatusData(quitMsg2, "d").getCausalDataPacket());
				try {
					entry.getStub().sendMessage(new AppDataPacket<IFailureStatusData>(IFailureStatusData.make(quitM, "unable to send quit network message."), myNamedConnection));
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				e.printStackTrace();
			}
        });
    	
		//System.out.println("We reached right before the rmi");
    	
		rmi.stopRMI();
        viewAdpt.stopDiscModel(); //UNSURE
        System.exit(404);
	}
	
	/**
	 * Connect to another Chat app instance.
	 * @param remoteRegistryIPAddr the ip address to connect to.
	 * @param boundName the bound name of the chat app instance.
	 * @return a status message of the connection
	 */
	public String connectTo(String remoteRegistryIPAddr, String boundName) {
		try {
			//System.out.println("IN CONNECTTO()");
			sysLog.log(LogLevel.INFO, "Locating registry at " + remoteRegistryIPAddr + "...");
			Registry registry = rmi.getRemoteRegistry(remoteRegistryIPAddr);
			sysLog.log(LogLevel.INFO, "Found registry: " + registry);
			
			// Wrap the stub inside a namedConnector and add it to our contact.
			//System.out.println("IN MIDDLE OF CONNECTTO()");
			sysLog.log(LogLevel.INFO, "Looking for stub in registry: " + registry);
			IInitialAppConnection remoteStub = (IInitialAppConnection) registry.lookup(boundName);
			//sysLog.log(LogLevel.INFO, "Found some stub: " + registry);
			this.connectToStub(remoteStub);
			sysLog.log(LogLevel.INFO, "Found remote stub: ");
			
		} catch (Exception e) {
			sysLog.log(LogLevel.ERROR, "Exception connecting to " + remoteRegistryIPAddr + ": " + e);
			e.printStackTrace();
			
			return "No connection established!";
		}
		return "Connection to " + remoteRegistryIPAddr + " established!";
    }


	/**
	 * @param connectedStub the stub we got from the registry
	 */
	public void connectToStub(IInitialAppConnection connectedStub) {
		Thread t = new Thread(()->{
			try {
				connectedStub.receiveNamedConnection(myNamedConnection);
				//connectedStub.getStub().sendMessage(new AppDataPacket<IConnectionSetData>(new ConnectionSetData(this.connectedManagers), this.myNamedConnection));
			} catch (RemoteException e) {
				sysLog.log(LogLevel.DEBUG, "Failed to send add peers message to the remote stub.");
				e.printStackTrace();
			}
		});
		t.start();
	}
	
    /**
	 * @param roomName the room name.
	 */
	public void makeRoom(String roomName) {
		this.viewAdpt.makeNewRoom(roomName);
	}
	
	/**
	 * @return Gets the chatrooms
	 */
	public List<IMain2MiniAdpt> getChatrooms(){
		return this.allChatrooms;
	}
	
	/**
	 * @return Gets the rmi utils
	 */
	public RMIUtils getRmiUtils() {
		return this.rmi;
	}
	
	/**
	 * @return Returns the pubsubmanager
	 */
	public IPubSubSyncManager getPubSubSyncManager() {
		return pubSubManager;
	}
	
	/**
	 * @return Returns the conneections
	 */
	public HashSet<INamedAppConnection> getContacts(){
		return this.connectedManagers;
	}



	/**
	 * @param index The indx of the room to remove
	 */
	public void removeRoom(int index) {
		// TODO Auto-generated method stub
		this.allChatrooms.remove(index);
	
	}
	
	
	
	/**
	 * Add the connected stub to the list of contacts
	 * @param contact the stub of a chat app instance.
	 */
	public void addContact(INamedAppConnection contact) {
		this.connectedManagers.add(contact);
		this.viewAdpt.updateContacts(this.connectedManagers);
	}
	
	
	/**
	 * @return The named app connection
	 */
	public INamedAppConnection getStub() {
		return myNamedConnection;
	}
}
