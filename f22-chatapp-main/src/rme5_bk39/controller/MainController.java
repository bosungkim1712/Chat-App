package rme5_bk39.controller;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import common.dataPacket.AppDataPacket;
import common.serverObj.IInitialAppConnection;
import common.serverObj.INamedAppConnection;
import provided.discovery.IEndPointData;
import provided.discovery.impl.model.DiscoveryModel;
import provided.discovery.impl.model.IDiscoveryModelToViewAdapter;
import provided.discovery.impl.view.DiscoveryPanel;
import provided.discovery.impl.view.IDiscoveryPanelAdapter;
import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;
import provided.logger.util.LoggerPanel;
import provided.pubsubsync.IPubSubSyncManager;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.IRMI_Defs;
import rme5_bk39.IMain2MiniAdpt;

import rme5_bk39.chatroom.miniController.ChatroomController;
import rme5_bk39.chatroom.miniController.IMini2MainAdpt;
import rme5_bk39.model.ChatappConfig;
import rme5_bk39.model.IMainModel2ViewAdpt;
import rme5_bk39.model.MainModel;
import rme5_bk39.view.IMainView2ModelAdpt;
import rme5_bk39.view.MainView;
import common.dataPacket.data.app.IInviteData;
import common.dataPacket.data.status.IFailureStatusData;
import rme5_bk39.msg.InviteData;

/**
 * @author Renzo Espinoza
 * The controller of the main app
 */
public class MainController {
	
	/**
	 * The view of the chatapp
	 */
	private MainView view;
	
	/**
     * The system logger to use. Change and/or customize this logger as desired.
     */
    private ILogger chatAppLogger = ILoggerControl.getSharedLogger();

    /**
     * The Discovery server UI panel for the view
     */
    private DiscoveryPanel<IEndPointData> discPnl;
    
    /**
     * A self-contained model to handle the discovery server.   MUST be started AFTER the main model as it needs the IRMIUtils from the main model! 
     */
    private DiscoveryModel<IInitialAppConnection> discModel;
    
    /**
     * The main model
     */
    private MainModel model;
    
    /**
     * The system logger
     */
    private ILogger sysLogger = ILoggerControl.getSharedLogger();
	
    
    /**
     * Constructor for the ClientController Class.
     * @param config config file for the chat app instance
     */
    public MainController(ChatappConfig config) {
    	chatAppLogger.setLogLevel(LogLevel.DEBUG); // For debugging purposes.   Default is LogLevel.INFO

        new LoggerPanel("ChatApp Log");
        
        discPnl = new DiscoveryPanel<IEndPointData>(new IDiscoveryPanelAdapter<IEndPointData>() {
        	/**
             * watchOnly parameter is ignored because DiscoveryModelPubOnly assumes watchOnly = false.
             */
            @Override
            public void connectToDiscoveryServer(String category, boolean watchOnly,
                    Consumer<Iterable<IEndPointData>> endPtsUpdateFn) {
                // Ask the discovery model to connect to the discovery server on the given category and use the given updateFn to update the endpoints list in the discovery panel.
            
            	discModel.connectToDiscoveryServer(category, false, endPtsUpdateFn);
            }

            /**
             * This method is never called in "Server" usage mode
             */
            @Override
            public void connectToEndPoint(IEndPointData selectedValue) {
                discModel.connectToEndPoint(selectedValue);
                
            }

        }, true, true);
        
        discModel = new DiscoveryModel<IInitialAppConnection>(chatAppLogger, new IDiscoveryModelToViewAdapter<IInitialAppConnection>() {

			@Override
			public void addStub(IInitialAppConnection stub) {
				 
				sysLogger.log(LogLevel.INFO, "In discovery model adding a stub: ");
				model.connectToStub(stub);
			}

            
        });
        
        view = new MainView<INamedAppConnection>(new IMainView2ModelAdpt<INamedAppConnection>() {

			public String connectTo(String remoteIP, String boundName) {
				return model.connectTo(remoteIP, boundName);
			}
			
        	@Override
			public void quit() {
				model.quit();
				
			}

			@Override
			public void invite(INamedAppConnection app) {
				IMain2MiniAdpt currentChatRoom = model.getChatrooms().get(view.getCurrentChatRoom()-1);
				AppDataPacket<IInviteData> msg = new AppDataPacket<IInviteData>(
						new InviteData(currentChatRoom.getChatRoomID(), currentChatRoom.getRoomName()), 
						app);
				Thread t = new Thread(() -> {
					try {
						app.getStub().sendMessage(msg);
					} catch (RemoteException e) {
						try {
							app.getStub().sendMessage(new AppDataPacket<IFailureStatusData>(IFailureStatusData.make(msg, "unable to send quit network message."),model.getStub()));
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						sysLogger.log(LogLevel.ERROR, "Failed to send message "+msg.toString());
						e.printStackTrace();
					}
				});
				t.start();
				
			}

			@Override
			public void start() {
				model.start();
				System.out.println(model.getRmiUtils());
				discModel.start(model.getRmiUtils(), view.getServerName(), config.getName());
			}

			@Override
			public void makeNewRoom(String roomName) {
				model.makeRoom(roomName);
				
			}

			@Override
			public void removeRoom(int index) {
				// TODO Auto-generated method stub
				model.removeRoom(index);
				
			} 
            
        });
        
        model = new MainModel(chatAppLogger, config, (IMainModel2ViewAdpt) new IMainModel2ViewAdpt() {


            private ChatroomController makeController(String roomName){
                ChatroomController miniController = new ChatroomController(roomName, new IMini2MainAdpt(){

					@Override
					public IPubSubSyncManager getPubSubSyncManager() {
						// TODO Auto-generated method stub
						return model.getPubSubSyncManager();
					}

					@Override
					public void removeRoom() {
						// TODO Auto-generated method stub
						view.removeCurrentRoomPanel();
						
					}

					@Override
					public ILogger getLogger() {
						// TODO Auto-generated method stub
						return sysLogger;
					}

					@Override
					public INamedAppConnection getNamedConnector() {
						// TODO Auto-generated method stub
						return model.getStub();
					}

					@Override
					public String getUserName() {
					
						return view.getUserName();
					}


					@Override
					public IRMIUtils getRmiUtils() {
						// TODO Auto-generated method stub
						return model.getRmiUtils();
					}

					@Override
					public ChatappConfig getConfig() {
						return config;
					}


                });
                miniController.start();
                return miniController;
            }

			@Override
			public IMain2MiniAdpt makeNewRoom(String roomName) {
				// TODO Auto-generated method stub
                ChatroomController miniController = this.makeController(roomName);
                miniController.makeNewRoom();
                view.addNewTab(miniController.getMyRoomPanel(), roomName);
                model.getChatrooms().add(miniController.getRoomAdptr());
                return miniController.getRoomAdptr();

			}

			@Override
			public IMain2MiniAdpt join(UUID roomID, String roomName) {
				// TODO Auto-generated method stub
				ChatroomController miniController = this.makeController(roomName);
				miniController.joinRoom(roomID);
				view.addNewTab(miniController.getMyRoomPanel(),roomName);
				model.getChatrooms().add(miniController.getRoomAdptr());
				return miniController.getRoomAdptr();
			}

			@Override
			public void stopDiscModel() {
				// TODO Auto-generated method stub
				discModel.stop();
				
			}

			@Override
			public void updateContacts(Set<INamedAppConnection> stubs) {
				view.updateConnectedHosts(stubs);
			}

			@Override
			public void displayStatusMsg(String apply) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getUserName() {
				// TODO Auto-generated method stub
				return view.getUserName();
			}

        });
    }
       
        
        
    
    /**
     * Launch the application.
     * 
     * @param args
     *            Unused parameters.
     */
    public static void main(String[] args) {

    	ChatappConfig appConfig1 = new ChatappConfig("Bosung", IRMI_Defs.STUB_PORT_SERVER, IRMI_Defs.CLASS_SERVER_PORT_SERVER);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				(new MainController(appConfig1)).start();
			}
		});
    }
	/**
     * Start Java Project.
     */
    public void start() {

        //chatAppLogger.addLogProcessor(pnlLog);
        //view.addRoom("ChatApp Log", pnlLog);;

        discPnl.start(); // start the discovery panel
        System.out.print("DISCOVERY STARTED");
        
        
        view.addCtrlComponent(discPnl);
        System.out.print("DISCOVERY PANEL ADDED");
        view.start(); // start the view
        
    }

}
