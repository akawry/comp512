package ResImpl.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import LockManager.DeadlockException;
import ResImpl.Flight;
import ResImpl.FlightResourceManager;
import ResInterface.FlightBackend;
import ResInterface.FlightFrontend;
import ResInterface.IFlightResourceManager;
import Transactions.InvalidTransactionException;
import Transactions.TransactionAbortedException;

public class FlightRMIResourceManager extends AbstractRMIResourceManager implements IFlightResourceManager {

	private FlightResourceManager rm;
	
	public FlightRMIResourceManager(FlightResourceManager rm){
		this.rm = rm;
	}

	@Override
	public String usage() {
		return "Usage: ResImpl.FlightResourceManager [port]";
	}

	@Override
	public void register() throws Exception {
	    registry.bind("RMIFlight", UnicastRemoteObject.exportObject(this, 0));
	}
	

	@Override
	public boolean addFlight(int id, int flightNum, int flightSeats,
			int flightPrice) throws RemoteException, DeadlockException, InvalidTransactionException {
		return rm.addFlight(id, flightNum, flightSeats, flightPrice);
	}

	@Override
	public boolean deleteFlight(int id, int flightNum) throws RemoteException, InvalidTransactionException, DeadlockException {
		return rm.deleteFlight(id, flightNum);
	}

	@Override
	public int queryFlight(int id, int flightNumber) throws RemoteException, InvalidTransactionException, DeadlockException {
		return rm.queryFlight(id, flightNumber);
	}

	@Override
	public int queryFlightPrice(int id, int flightNumber) throws RemoteException, InvalidTransactionException, DeadlockException {
		return rm.queryFlightPrice(id, flightNumber);
	}

	@Override
	public Flight getFlight(int id, int flightNumber) throws RemoteException, DeadlockException, InvalidTransactionException {
		return rm.getFlight(id, flightNumber);
	}
	

	public static void main(String[] args) {
		FlightRMIResourceManager rm = new FlightRMIResourceManager(new FlightResourceManager());
		rm.parseArgs(args) ;
		rm.launch();
	}

	@Override
	public void updateFlight(int id, int flightNumber, Flight flight)
			throws RemoteException, DeadlockException, InvalidTransactionException {
		rm.updateFlight(id, flightNumber, flight);
	}

	@Override
	public int start() throws RemoteException, InvalidTransactionException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean commit(int transactionId) throws RemoteException,
			TransactionAbortedException, InvalidTransactionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void abort(int transactionId) throws RemoteException,
			InvalidTransactionException {
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
		return rm.enlist(transactionId);
	}

}
