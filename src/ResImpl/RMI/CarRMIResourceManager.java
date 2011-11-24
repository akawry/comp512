package ResImpl.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import FaultTolerance.ICrashable;
import FaultTolerance.IPingable;
import LockManager.DeadlockException;
import ResImpl.Car;
import ResImpl.CarResourceManager;
import ResInterface.CarBackend;
import ResInterface.CarFrontend;
import ResInterface.ICarResourceManager;
import Transactions.ITransactionManager;
import Transactions.InvalidTransactionException;
import Transactions.TransactionAbortedException;

public class CarRMIResourceManager extends AbstractRMIResourceManager implements ITransactionManager, ICarResourceManager, ICrashable, IPingable {

	private CarResourceManager rm;
	
	public CarRMIResourceManager(CarResourceManager rm){
		this.rm = rm;
	}

	@Override
	public boolean addCars(int id, String location, int numCars, int price) throws RemoteException, InvalidTransactionException, DeadlockException {
		return rm.addCars(id, location, numCars, price);
	}

	@Override
	public boolean deleteCars(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException {
		return rm.deleteCars(id, location);
	}

	@Override
	public int queryCars(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException {
		return rm.queryCars(id, location);
	}

	@Override
	public int queryCarsPrice(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException {
		return rm.queryCarsPrice(id, location);
	}

	@Override
	public Car getCar(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException {
		return rm.getCar(id, location);
	}

	@Override
	public String usage() {
		return "Usage: ResImpl.CarResourceManager [port]";
	}

	@Override
	public void register() throws Exception {
	    registry.bind("RMICar", UnicastRemoteObject.exportObject(this, 0));
	}
	
	public static void main(String[] args) {
		CarRMIResourceManager rm = new CarRMIResourceManager(new CarResourceManager());
		rm.parseArgs(args) ;
		rm.launch();
	}

	@Override
	public void updateCar(int id, String location, Car car)
			throws RemoteException, InvalidTransactionException, DeadlockException {
		rm.updateCar(id, location, car);
		
	}

	@Override
	public int start() throws RemoteException {
		return rm.start();
	}

	@Override
	public boolean commit(int transactionId) throws RemoteException, TransactionAbortedException, InvalidTransactionException {
		return rm.commit(transactionId);
	}

	@Override
	public void abort(int transactionId) throws RemoteException, InvalidTransactionException {
		rm.abort(transactionId);
		
	}

	@Override
	public boolean shutdown() throws RemoteException {
		return rm.shutdown();
	}

	@Override
	public boolean enlist(int transactionId) throws RemoteException,
			InvalidTransactionException {
		return rm.enlist(transactionId);
	}

	
}
