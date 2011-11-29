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

	public String getHost() throws RemoteException;
	
	public int getPort() throws RemoteException;
	
	public void keepAlive(int id) throws RemoteException;

	int nextTransactionId() throws RemoteException;
	
	void removeTransaction(int id) throws RemoteException;
	
	void clearAllTransactions() throws RemoteException;
    
}
