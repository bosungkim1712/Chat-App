package rme5_bk39.model;

import provided.config.AppConfig;

/**
 * @author Renzo Espinoza Bo Sung Kim
 * The chat app configuration
 */
public class ChatappConfig extends AppConfig{
	/**
	 * port rmi
	 */
    private int portRMI;
    
    /**
     * the port class server
     */
    private int portClassServer;

    /**
     * @param boundName bound name
     * @param portRMI portRMI
     * @param portClassServer port for the class server
     */
    public ChatappConfig(String boundName, int portRMI, int portClassServer){
        super(boundName);
        this.portClassServer = portClassServer;
        this.portRMI = portRMI;        
    }

    /**
     * @return the port class server
     */
    public int getPortClassServer(){
        return this.portClassServer;
    }

    /**
     * @return the port rmi
     */
    public int getPortRMI(){
        return this.portRMI;
    }

    /**
     * @return the name
     */
    public String getName(){
        return this.name;
    }
}
