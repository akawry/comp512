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
	
	/**
	 * Returns true if the shared lock has been granted for the given resource, false otherwise  
	 * @param transactionId
	 * @param type
	 * @param itemId
	 * @return
	 */
	public boolean read(int transactionId, String type, int itemId){
		return lockManager.Lock(transactionId, type + itemId, TrxnObj.READ);
	}
	
	/**
	 * Returns true if the exclusive lock has been granted for the given resource, false otherwise 
	 * @param transactionId
	 * @param type
	 * @param itemId
	 * @return
	 */
	public boolean write(int transactionId, String type, int itemId){
		// TODO: Keep the corresponding 'undo' commands 
		return lockManager.Lock(transactionId, type + itemId, TrxnObj.WRITE);
	}


}
