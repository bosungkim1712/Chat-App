package rme5_bk39.chatroom.miniView;


import java.util.Set;

/**
 * Interface for mini-view to use mini-model methods. 
 * 
 * @author bk39 rme5
 *
 */
public interface IMiniView2ModelAdpt {
    
    /**
     * Send message
     * @param msg message being sent
     */
    void sendMessage(String msg);
    

	/**
	 * leave the room
	 */
	void leave();



	/**
	 * @return the roster in the room
	 */
	Set<String> getRoomRoster();
	
	/**
	 * sends the ying yang msg
	 */
	void sendYingYangMsg();
}
