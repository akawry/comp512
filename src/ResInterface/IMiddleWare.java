package ResInterface;

import java.rmi.RemoteException;
import java.util.Vector;

import LockManager.DeadlockException;
import Transactions.InvalidTransactionException;

public interface IMiddleWare extends ResourceFrontend{

	public int startCustomerRM() throws RemoteException;
	
	public boolean enlistCustomerRM(int id) throws RemoteException;

	boolean commitCustomerRM(int transactionId) throws RemoteException;

	public void abortCustomerRM(int transactionId) throws RemoteException;
	
	public boolean shutdownCustomerRM() throws RemoteException;
	
    public boolean reserveCarForCustomer(int id, int customer, String location) throws RemoteException, InvalidTransactionException, DeadlockException;
    
    public boolean reserveFlightForCustomer(int id, int customer, int flightNumber) throws RemoteException, DeadlockException, InvalidTransactionException; 
    
    public boolean reserveRoomForCustomer(int id, int customer, String location) throws RemoteException, DeadlockException, InvalidTransactionException; 

    public boolean itineraryForCustomer(int id,int customer,Vector<String> flightNumbers,String location, boolean Car, boolean Room) throws RemoteException, NumberFormatException, DeadlockException, InvalidTransactionException; 

	public String getHost() throws RemoteException;
	
	public int getPort() throws RemoteException;
	
	public void keepAlive(int id) throws RemoteException;

	int nextTransactionId() throws RemoteException;
	
	void removeTransaction(int id) throws RemoteException;
	
	void clearAllTransactions() throws RemoteException;
    
}
