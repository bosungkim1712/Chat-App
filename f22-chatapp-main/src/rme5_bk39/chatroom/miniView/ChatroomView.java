package rme5_bk39.chatroom.miniView;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * Chatroom panel mvc
 * @author bk39 rme5
 */
public class ChatroomView extends JPanel {
    

	/**
	 * The adapter for the model.
	 */
	private IMiniView2ModelAdpt adptr;

	/**
	 * Serialized version UID.
	 */
	private static final long serialVersionUID = 1707042914688785298L;
	/**
	 * The message panel.
	 */
	private final JPanel Msg = new JPanel();
	/**
	 * The control panel.
	 */
	private final JSplitPane control = new JSplitPane();
	/**
	 * Panel that split users from msg and status.
	 */
	private final JSplitPane splitPane = new JSplitPane();
	/**
	 * Text field to put message to be sent.
	 */
	private final JTextField textField = new JTextField();
	
	/**
	 * Send message button.
	 */
	private final JButton sendMsg = new JButton("Send Msg");
	/**
	 * Send BallWolrd button.
	 */
	private final JButton sendYingYang = new JButton("Send YingYang");
	/**
	 * List of users panel.
	 */
	private final JPanel users = new JPanel();
	/**
	 * Button to exit the room.
	 */
	private final JButton exit = new JButton("Exit Room");
	/**
	 * List of users
	 */
	private final JScrollPane usersPanel = new JScrollPane();
	/**
	 * A tab for each task
	 */
	private final JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
	/**
	 * textArea for msg.
	 */
	private final JTextArea msgArea = new JTextArea();

	/**
	 * The room roster
	 */
	private Set<String> roomRoster;
	
	/**
	 * The member list
	 */
	private final JTextArea memberList = new JTextArea();
	
	/**
	 * The status panel
	 */
	private final JPanel statusPanel = new JPanel();
	
	/**
	 * The status area
	 */
	private final JTextArea statusArea = new JTextArea();

	
	/**
	 * Scroll pane for message area
	 */
	private final JScrollPane scrollPane = new JScrollPane();

	/**
	 * Constructor for the view
	 * @param adptr the adapter.
	 */
	public ChatroomView(IMiniView2ModelAdpt adptr) {
		this.adptr = adptr;
		textField.setToolTipText("The string message to be sent");
		textField.setColumns(15);
		roomRoster = adptr.getRoomRoster();
		this.adptr = adptr;
		initGUI();
	}

	/**
	 * Initialize the gui. 
	 */
	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		Msg.setToolTipText("The Panel for sending all messages");

		add(Msg, BorderLayout.SOUTH);
		Msg.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		Msg.add(textField);
		textField.setEditable(true);

		sendMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//textField.setEditable(true);
				String msg = textField.getText();
				adptr.sendMessage(msg);
				textField.setText("");
				//textField.setEditable(false);
				
			}
		});
		sendMsg.setToolTipText("Button to send string message");
		Msg.add(sendMsg);
		
		
		sendYingYang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adptr.sendYingYangMsg();
			}
		});
		sendYingYang.setToolTipText("button to send ying yang message");

		
		Msg.add(sendYingYang);
		
		control.setToolTipText("Controlling the chat app");
		control.setResizeWeight(0.05);

		add(control, BorderLayout.CENTER);
		splitPane.setToolTipText("Messages and their status");
		splitPane.setResizeWeight(0.8);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

		control.setRightComponent(splitPane);
		tabs.setToolTipText("tabs for tasks");

		splitPane.setLeftComponent(tabs);
				
		tabs.addTab("New tab", null, scrollPane, null);
		msgArea.setToolTipText("Display messages");
				
		tabs.addTab("Messages", null, scrollPane, null);
		statusPanel.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		splitPane.setRightComponent(statusPanel);
		statusPanel.setLayout(new BorderLayout(0, 0));
		statusArea.setToolTipText("place to display status msg");

		statusPanel.add(statusArea, BorderLayout.CENTER);
		
		users.setToolTipText("List of users");

		control.setLeftComponent(users);
		users.setLayout(new BorderLayout(0, 0));
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adptr.leave();
			}
		});
		exit.setToolTipText("Leave this room");
		exit.setFont(new Font("SimSun", Font.PLAIN, 8));

		users.add(exit, BorderLayout.SOUTH);
		usersPanel.setToolTipText("The users");

		users.add(usersPanel, BorderLayout.CENTER);

		usersPanel.setViewportView(memberList);
		scrollPane.setViewportView(msgArea);
		msgArea.setEditable(false);
		memberList.setEditable(false);

	}

	/**
	 * Start the view.
	 */
	public void start() {
		setVisible(true);
	}

	/**
	 * Append a message (along with the user) to the message pane
	 * @param msg the msg
	 */
	public void appendMessage(String msg) {
		System.out.println("TEXTING " + msg + "\n");
		System.out.println("CURRENT ROSTER in VIEW: " + this.roomRoster);
		msgArea.append(msg);
	}

	/**
	 * Append a status message to the status pane
	 * @param status the status msg
	 */
	public void appendStatus(String status) {
		statusArea.append(status + "\n");
	}

	/**
	 * @param roomRoster the room roster
	 */
	public void updateRoomRoster(Set<String> roomRoster) {
		this.roomRoster = roomRoster;
		memberList.setEditable(true);
		this.memberList.setText("");
		for (String member : roomRoster) {
			memberList.append(member + '\n');
		}
		
		memberList.setEditable(false);
		System.out.println("ROOM ROSTER: "+roomRoster);
	}

	/**
	 * @param component the component
	 * @param name the name of the component
	 */
	public void addFixedComponent(Component component, String name) {
		//		((JComponent) component).setToolTipText(name);
		this.tabs.add(component, name);
	}

	/**
	 * @param component the component
	 * @param name of the component
	 */
	public void addScrollingComponent(Component component, String name) {
		this.tabs.add(component, name);
	}
    
}
