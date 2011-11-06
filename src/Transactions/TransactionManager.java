package Transactions;

import java.rmi.RemoteException;

import LockManager.LockManager;
import LockManager.TrxnObj;

public class TransactionManager implements ITransactionManager {

	public final static String CAR = "CAR";
	public final static String FLIGHT = "FLIGHT";
	public final static String ROOM = "ROOM";
	public final static String CUSTOMER = "CUSTOMER";
	
	private LockManager lockManager;
	
	public TransactionManager(LockManager lockManager){
		this.lockManager = lockManager;
	}
	
	@Override
	public int start() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean commit(int transactionId) throws RemoteException, TransactionAbortedException, InvalidTransactionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void abort(int transactionId) throws RemoteException, InvalidTransactionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shutdown() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean enlist(int transactionId) throws RemoteException,
			InvalidTransactionException {
		// TODO Auto-generated method stub
		return false;
	}


}
