package Transactions;

import java.rmi.RemoteException;

import FaultTolerance.CrashException;

public interface ITransactionManager {

	public int start() throws RemoteException, InvalidTransactionException, CrashException;
	
	public boolean commit(int transactionId) throws RemoteException, TransactionAbortedException, InvalidTransactionException, CrashException;

	public void abort(int transactionId) throws RemoteException, InvalidTransactionException, CrashException;
	
	public boolean shutdown() throws RemoteException, CrashException;
	
	public boolean enlist(int transactionId) throws RemoteException, InvalidTransactionException, CrashException;
}
