package ResImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Stack;

import LockManager.DeadlockException;
import LockManager.TrxnObj;
import ResInterface.IRoomResourceManager;
import ResInterface.RoomBackend;
import ResInterface.RoomFrontend;
import Transactions.InvalidTransactionException;
import Transactions.Operation;

public class RoomResourceManager extends AbstractResourceManager implements Remote, IRoomResourceManager {

	@Override
	public boolean addRooms(int id, String location, int count, int price) throws DeadlockException, InvalidTransactionException {
		Trace.info("RM::addRooms(" + id + ", " + location + ", " + count + ", $" + price + ") called" );
		
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		
		if (lockManager.Lock(id, location, TrxnObj.WRITE)){
			Hotel curObj = (Hotel) readData( id, Hotel.getKey(location) );
			if( curObj == null ) {
				ops.push(new Operation(Operation.DELETE, Hotel.getKey(location), null));

				// doesn't exist...add it
				Hotel newObj = new Hotel( location, count, price );
				writeData( id, newObj.getKey(), newObj );
				Trace.info("RM::addRooms(" + id + ") created new room location " + location + ", count=" + count + ", price=$" + price );
			} else {
				ops.push(new Operation(Operation.WRITE, curObj.getKey(), new Hotel(curObj.getLocation(), curObj.getCount(), curObj.getPrice())));
				
				// add count to existing object and update price...
				curObj.setCount( curObj.getCount() + count );
				if( price > 0 ) {
					curObj.setPrice( price );
				} // if
				writeData( id, curObj.getKey(), curObj );
				Trace.info("RM::addRooms(" + id + ") modified existing location " + location + ", count=" + curObj.getCount() + ", price=$" + price );
			} // else
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteRooms(int id, String location) throws InvalidTransactionException, DeadlockException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, location, TrxnObj.WRITE);
		ops.push(new Operation(Operation.ADD, Hotel.getKey(location), readData(id, Hotel.getKey(location))));
		return deleteItem(id, Hotel.getKey(location));	
	}

	@Override
	public int queryRooms(int id, String location) throws InvalidTransactionException, DeadlockException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, location, TrxnObj.READ);
		return queryNum(id, Hotel.getKey(location));
	}

	@Override
	public int queryRoomsPrice(int id, String location) throws InvalidTransactionException, DeadlockException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, location, TrxnObj.READ);
		return queryPrice(id, Hotel.getKey(location));
	}

	@Override
	public Hotel getRoom(int id, String location) throws DeadlockException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, location, TrxnObj.READ);
		return (Hotel) readData(id, Hotel.getKey(location));
	}

	@Override
	public void updateRoom(int id, String location, Hotel room)
			throws RemoteException, DeadlockException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, location, TrxnObj.WRITE);
		ops.push(new Operation(Operation.WRITE, Hotel.getKey(location), readData(id, Hotel.getKey(location))));
		writeData(id, Hotel.getKey(location), room);
	}

}
