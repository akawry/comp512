package FaultTolerance;

import java.rmi.RemoteException;

import LockManager.DeadlockException;
import ResImpl.Car;
import ResImpl.Flight;
import ResImpl.Hotel;
import ResInterface.ICarResourceManager;
import ResInterface.IFlightResourceManager;
import ResInterface.IRoomResourceManager;
import Transactions.InvalidTransactionException;
import Transactions.TransactionAbortedException;

public class CrashedRM implements ICarResourceManager, IRoomResourceManager, IFlightResourceManager {
	
	@Override
	public boolean addCars(int id, String location, int numCars, int price)
			throws RemoteException, DeadlockException,
			InvalidTransactionException, CrashException {
		throw new CrashException();
	}

	@Override
	public boolean deleteCars(int id, String location) throws RemoteException,
			DeadlockException, InvalidTransactionException, CrashException {
		throw new CrashException();
	}

	@Override
	public int queryCars(int id, String location) throws RemoteException,
			DeadlockException, InvalidTransactionException, CrashException {
		throw new CrashException();
	}

	@Override
	public int queryCarsPrice(int id, String location) throws RemoteException,
			DeadlockException, InvalidTransactionException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public Car getCar(int id, String location) throws RemoteException,
			DeadlockException, InvalidTransactionException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public void updateCar(int id, String location, Car car)
			throws RemoteException, DeadlockException,
			InvalidTransactionException, CrashException {
		throw new CrashException();	
	}

	@Override
	public int start() throws RemoteException, InvalidTransactionException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public boolean commit(int transactionId) throws RemoteException,
			TransactionAbortedException, InvalidTransactionException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public void abort(int transactionId) throws RemoteException,
			InvalidTransactionException, CrashException {
		throw new CrashException();	
	}

	@Override
	public boolean shutdown() throws RemoteException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public boolean enlist(int transactionId) throws RemoteException,
			InvalidTransactionException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public boolean addRooms(int id, String location, int numRooms, int price)
			throws RemoteException, DeadlockException,
			InvalidTransactionException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public boolean deleteRooms(int id, String location) throws RemoteException,
			InvalidTransactionException, DeadlockException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public int queryRooms(int id, String location) throws RemoteException,
			InvalidTransactionException, DeadlockException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public int queryRoomsPrice(int id, String location) throws RemoteException,
			InvalidTransactionException, DeadlockException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public Hotel getRoom(int id, String location) throws RemoteException,
			DeadlockException, InvalidTransactionException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public void updateRoom(int id, String location, Hotel room)
			throws RemoteException, DeadlockException,
			InvalidTransactionException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public boolean addFlight(int id, int flightNum, int flightSeats,
			int flightPrice) throws RemoteException, DeadlockException,
			InvalidTransactionException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public boolean deleteFlight(int id, int flightNum) throws RemoteException,
			InvalidTransactionException, DeadlockException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public int queryFlight(int id, int flightNumber) throws RemoteException,
			InvalidTransactionException, DeadlockException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public int queryFlightPrice(int id, int flightNumber)
			throws RemoteException, InvalidTransactionException,
			DeadlockException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public Flight getFlight(int id, int flightNumber) throws RemoteException,
			DeadlockException, InvalidTransactionException, CrashException {
		throw new CrashException();
		
	}

	@Override
	public void updateFlight(int id, int flightNumber, Flight flight)
			throws RemoteException, DeadlockException,
			InvalidTransactionException, CrashException {
		throw new CrashException();	
	}

}
