package Transactions;

import java.rmi.RemoteException;

public interface ITransactionManager {

	public int start() throws RemoteException;
	
	public boolean commit(int transactionId) throws RemoteException, TransactionAbortedException, InvalidTransactionException;

	public void abort(int transactionId) throws RemoteException, InvalidTransactionException;
	
	public boolean shutdown() throws RemoteException;
}
