package Transactions;

import java.rmi.RemoteException;

public interface ITransactionManager {

	public int start() throws RemoteException, InvalidTransactionException;
	
	public boolean commit(int transactionId) throws RemoteException, TransactionAbortedException, InvalidTransactionException;

	public void abort(int transactionId) throws RemoteException, InvalidTransactionException;
	
	public boolean shutdown() throws RemoteException;
	
	public boolean enlist(int transactionId) throws RemoteException, InvalidTransactionException;
}
