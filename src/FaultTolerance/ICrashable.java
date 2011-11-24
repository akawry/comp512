package FaultTolerance;

import java.rmi.RemoteException;

public interface ICrashable {

	public void crash() throws RemoteException;
	
}
