package ResInterface;

import java.rmi.RemoteException;

public interface Backend {

	public String getHost() throws RemoteException;
	
	public int getPort() throws RemoteException;
	
}
