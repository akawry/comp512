package ResInterface;
import java.rmi.Remote;	
import java.rmi.RemoteException;

import FaultTolerance.ICrashable;
import ResImpl.CustomerResourceManager;
import Transactions.ITransactionManager;

public interface ResourceFrontend extends Remote, CarFrontend, RoomFrontend, FlightFrontend, CustomerFrontend, ReservationFrontend, ITransactionManager, ICrashable {
	
	public void crashHost(String host, int num) throws RemoteException;

	public void crashType(String type, int num) throws RemoteException;
	
	public String getHost() throws RemoteException;
	
	public int getPort() throws RemoteException;
	
	public void keepAlive(int id) throws RemoteException;

	int nextTransactionId() throws RemoteException;
	
	void removeTransaction(int id) throws RemoteException;
	
	void clearAllTransactions() throws RemoteException;
	

	/*
	 * TODO: Make this shit cleaner !!!!!
	 */
	public int startCustomerRM() throws RemoteException;
	
	public boolean enlistCustomerRM(int id) throws RemoteException;

	boolean commitCustomerRM(int transactionId) throws RemoteException;

	public void abortCustomerRM(int transactionId) throws RemoteException;
	
	public boolean shutdownCustomerRM() throws RemoteException;
}
