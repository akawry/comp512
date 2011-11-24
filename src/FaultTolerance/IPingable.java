package FaultTolerance;

import java.rmi.RemoteException;

public interface IPingable {

	public int ping() throws RemoteException;
	
}
