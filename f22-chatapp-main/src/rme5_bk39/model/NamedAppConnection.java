package rme5_bk39.model;

import common.serverObj.IAppConnection;
import common.serverObj.INamedAppConnection;

/**
 * @author rezog
 * The named app connection
 */
public class NamedAppConnection implements INamedAppConnection{


    /**
	 * serial id
	 */
	private static final long serialVersionUID = -8406916292154414570L;
	
	/**
	 * the bound name
	 */
	private String boundName;

	/**
	 * the stub of the named app connection
	 */
    private IAppConnection stub;

    /**
     * @param boundName The bound name
     * @param Stub The app connection stub
     */
    public NamedAppConnection(String boundName, IAppConnection Stub){
        this.boundName = boundName;
        this.stub = stub;
    }

	@Override
	public String getName() {
		return this.boundName;
	}

	@Override
	public IAppConnection getStub() {
		return this.stub;
	}
    
}
