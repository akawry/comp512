package ResInterface;
import java.rmi.Remote;	
import java.rmi.RemoteException;

import Transactions.ITransactionManager;

public interface ResourceFrontend extends Remote, CarFrontend, RoomFrontend,FlightFrontend, CustomerFrontend, ReservationFrontend, ITransactionManager {
	
	public void crashHost(String host, int num) throws RemoteException;

	public void crashType(String type, int num) throws RemoteException;
	
}
