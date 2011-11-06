package ResImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Stack;

import LockManager.DeadlockException;
import LockManager.LockManager;
import LockManager.TrxnObj;
import ResInterface.CarBackend;
import ResInterface.CarFrontend;
import ResInterface.ICarResourceManager;
import Transactions.ITransactionManager;
import Transactions.InvalidTransactionException;
import Transactions.Operation;
import Transactions.TransactionAbortedException;

public class CarResourceManager extends AbstractResourceManager implements ICarResourceManager, ITransactionManager {

	
	@Override
	// Create a new car location or add cars to an existing location
	//  NOTE: if price <= 0 and the location already exists, it maintains its current price
	public boolean addCars(int id, String location, int count, int price) throws DeadlockException, InvalidTransactionException {
		Trace.info("RM::addCars(" + id + ", " + location + ", " + count + ", $" + price + ") called" );
		
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		
		if (lockManager.Lock(id, location, TrxnObj.WRITE)){
			
			Car curObj = (Car) readData( id, Car.getKey(location) );
			if( curObj == null ) {
				ops.push(new Operation(Operation.DELETE, location, null));
				
				// car location doesn't exist...add it
				Car newObj = new Car( location, count, price );
				writeData( id, newObj.getKey(), newObj ); Trace.info("RM::addCars(" + id + ") created new location " + location + ", count=" + count + ", price=$" + price );
			} else {
				ops.push(new Operation(Operation.WRITE, curObj.getKey(), curObj));
				
				// add count to existing car location and update price...
				curObj.setCount( curObj.getCount() + count );
				if( price > 0 ) {
					curObj.setPrice( price );
				} // if
				writeData( id, curObj.getKey(), curObj );
				Trace.info("RM::addCars(" + id + ") modified existing location " + location + ", count=" + curObj.getCount() + ", price=$" + price );
			} // else
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteCars(int id, String location) throws DeadlockException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, location, TrxnObj.WRITE);
		ops.push(new Operation(Operation.ADD, location, queryNum(id, Car.getKey(location))));
		return deleteItem(id, Car.getKey(location));
	}

	@Override
	public int queryCars(int id, String location) throws DeadlockException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, location, TrxnObj.READ);
		return queryNum(id, Car.getKey(location));
	}

	@Override
	public int queryCarsPrice(int id, String location) throws DeadlockException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, location, TrxnObj.READ);
		return queryPrice(id, Car.getKey(location));
	}

	@Override
	public Car getCar(int id, String location) throws DeadlockException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, location, TrxnObj.READ);
		return (Car) readData(id, Car.getKey(location));
	}

	@Override
	public void updateCar(int id, String location, Car car) throws RemoteException, DeadlockException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, location, TrxnObj.WRITE);
		ops.push(new Operation(Operation.WRITE, location, readData(id, Car.getKey(location))));
		writeData(id, Car.getKey(location), car);
		
	}

}
