package ResInterface;
import java.rmi.Remote;	
import java.rmi.RemoteException;

import FaultTolerance.ICrashable;
import Transactions.ITransactionManager;

public interface ResourceFrontend extends Remote, CarFrontend, RoomFrontend, FlightFrontend, CustomerFrontend, ReservationFrontend, ITransactionManager, ICrashable {
	
	public void crashHost(String host, int num) throws RemoteException;

	public void crashType(String type, int num) throws RemoteException;
	
	public String getHost() throws RemoteException;
	
	public int getPort() throws RemoteException;
}
