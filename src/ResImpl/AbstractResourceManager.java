package ResImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Stack;

import ResImpl.Customer;
import LockManager.DeadlockException;
import LockManager.LockManager;
import LockManager.TrxnObj;
import Transactions.ITransactionManager;
import Transactions.InvalidTransactionException;
import Transactions.Operation;
import Transactions.TransactionAbortedException;


public abstract class AbstractResourceManager {

	protected RMHashtable m_itemHT = new RMHashtable();
	protected int port;
	protected Registry registry;
	protected LockManager lockManager = new LockManager();
	protected Hashtable<Integer, Stack<Operation>> activeTransactions = new Hashtable<Integer, Stack<Operation>>();

	// Reads a data item
	protected RMItem readData(int id, String key) {
		synchronized (m_itemHT) {
			return (RMItem) m_itemHT.get(key);
		}
	}

	// Writes a data item
	protected void writeData(int id, String key, RMItem value) {
		synchronized (m_itemHT) {
			m_itemHT.put(key, value);
		}
	}

	// Remove the item out of storage
	protected RMItem removeData(int id, String key) {
		synchronized (m_itemHT) {
			return (RMItem) m_itemHT.remove(key);
		}
	}

	// deletes the entire item
	protected boolean deleteItem(int id, String key) {
		Trace.info("RM::deleteItem(" + id + ", " + key + ") called");
		ReservableItem curObj = (ReservableItem) readData(id, key);
		// Check if there is such an item in the storage
		if (curObj == null) {
			Trace.warn("RM::deleteItem(" + id + ", " + key
					+ ") failed--item doesn't exist");
			return false;
		} else {
			if (curObj.getReserved() == 0) {
				removeData(id, curObj.getKey());
				Trace.info("RM::deleteItem(" + id + ", " + key
						+ ") item deleted");
				return true;
			} else {
				Trace.info("RM::deleteItem("
						+ id
						+ ", "
						+ key
						+ ") item can't be deleted because some customers reserved it");
				return false;
			}
		} // if
	}

	// query the number of available seats/rooms/cars
	protected int queryNum(int id, String key) {
		Trace.info("RM::queryNum(" + id + ", " + key + ") called");
		ReservableItem curObj = (ReservableItem) readData(id, key);
		int value = 0;
		if (curObj != null) {
			value = curObj.getCount();
		} // else
		Trace.info("RM::queryNum(" + id + ", " + key + ") returns count="
				+ value);
		return value;
	}

	// query the price of an item
	protected int queryPrice(int id, String key) {
		Trace.info("RM::queryPrice(" + id + ", " + key + ") called");
		ReservableItem curObj = (ReservableItem) readData(id, key);
		int value = 0;
		if (curObj != null) {
			value = curObj.getPrice();
		} // else
		Trace.info("RM::queryPrice(" + id + ", " + key + ") returns cost=$"
				+ value);
		return value;
	}
	
	protected void undoAll(int id){
		Stack<Operation> ops = activeTransactions.get(id);
		while (ops.size() > 0){
			Operation op = ops.pop();
			switch (op.getType()){
			case Operation.ADD:
				Trace.info(this+":: Undoing DELETE command");
				writeData(id, (String)op.getKey(), (RMItem) op.getValue());
				break;
			case Operation.WRITE:
				Trace.info(this+":: Undoing WRITE command");
				writeData(id, (String)op.getKey(), (RMItem) op.getValue());
				break;
			case Operation.DELETE:
				Trace.info(this+":: Undoing ADD command. Removing " + op.getKey());
				try {
					// deleting a resource 
					deleteItem(id, (String) op.getKey());
				} catch (ClassCastException e){
					// deleting a customer 
					removeData(id, (String) op.getKey());
				}
				break;
			case Operation.UNRESERVE:
				Trace.info(this+":: Undoing RESERVE command. Removing " + op.getValue()+" from " + op.getKey());
				Customer cust = (Customer) readData(id, op.getKey());
				cust.unreserve((String)op.getValue());
				break;
			}
		}
	}
	

	public int start() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}


	public boolean commit(int id) throws RemoteException, TransactionAbortedException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		activeTransactions.remove(id);
		return lockManager.UnlockAll(id);
	}


	public void abort(int id) throws RemoteException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		undoAll(id);
		activeTransactions.remove(id);
		lockManager.UnlockAll(id);
	}


	public boolean shutdown() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean enlist(int id) throws RemoteException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops != null){
			throw new InvalidTransactionException("Transaction with id "+id+" already exsist");
		}
		activeTransactions.put(id, new Stack<Operation>());
		return true;
	}

}
