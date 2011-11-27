package ResInterface;

import java.rmi.RemoteException;

import LockManager.DeadlockException;
import ResImpl.Car;
import Transactions.InvalidTransactionException;

public interface CarBackend extends Backend {

	 public Car getCar(int id, String location) throws RemoteException, DeadlockException, InvalidTransactionException;
	    
	 public boolean updateCar(int id, String location, Car car) throws RemoteException, DeadlockException, InvalidTransactionException;
	 
	 public void undoLast(int id) throws RemoteException, InvalidTransactionException;
		
}
