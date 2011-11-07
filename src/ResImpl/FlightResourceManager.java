package ResImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Stack;

import LockManager.DeadlockException;
import LockManager.TrxnObj;
import ResImpl.RMI.FlightRMIResourceManager;
import ResInterface.FlightBackend;
import ResInterface.FlightFrontend;
import ResInterface.IFlightResourceManager;
import Transactions.ITransactionManager;
import Transactions.InvalidTransactionException;
import Transactions.Operation;
import Transactions.TransactionAbortedException;

public class FlightResourceManager extends AbstractResourceManager implements IFlightResourceManager, ITransactionManager {

	@Override
	public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws DeadlockException, InvalidTransactionException {
		Trace.info("RM::addFlight(" + id + ", " + flightNum + ", $" + flightPrice + ", " + flightSeats + ") called" );
		
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		
		if (lockManager.Lock(id, ""+flightNum, TrxnObj.WRITE)){
			Flight curObj = (Flight) readData( id, Flight.getKey(flightNum) );
			if( curObj == null ) {
				ops.push(new Operation(Operation.DELETE, Flight.getKey(flightNum), null));
				
				// doesn't exist...add it
				Flight newObj = new Flight( flightNum, flightSeats, flightPrice );
				writeData( id, newObj.getKey(), newObj );
				Trace.info("RM::addFlight(" + id + ") created new flight " + flightNum + ", seats=" +
						flightSeats + ", price=$" + flightPrice );
			} else {
				ops.push(new Operation(Operation.WRITE, curObj.getKey(), new Flight(flightNum, curObj.getCount(), curObj.getPrice())));
				
				// add seats to existing flight and update the price...
				curObj.setCount( curObj.getCount() + flightSeats );
				if( flightPrice > 0 ) {
					curObj.setPrice( flightPrice );
				} // if
				writeData( id, curObj.getKey(), curObj );
				Trace.info("RM::addFlight(" + id + ") modified existing flight " + flightNum + ", seats=" + curObj.getCount() + ", price=$" + flightPrice );
			} // else
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteFlight(int id, int flightNum) throws InvalidTransactionException, DeadlockException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, ""+flightNum, TrxnObj.WRITE);
		ops.push(new Operation(Operation.ADD, Flight.getKey(flightNum), readData(id, Flight.getKey(flightNum))));
		return deleteItem(id, Flight.getKey(flightNum));
	}

	@Override
	public int queryFlight(int id, int flightNum) throws InvalidTransactionException, DeadlockException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, ""+flightNum, TrxnObj.READ);
		return queryNum(id, Flight.getKey(flightNum));
	}

	@Override
	public int queryFlightPrice(int id, int flightNum ) throws InvalidTransactionException, DeadlockException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, ""+flightNum, TrxnObj.READ);
		return queryPrice(id, Flight.getKey(flightNum));
	}

	@Override
	public Flight getFlight(int id, int flightNumber) throws DeadlockException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, ""+flightNumber, TrxnObj.READ);
		return (Flight) readData(id, Flight.getKey(flightNumber));
	}

	@Override
	public void updateFlight(int id, int flightNumber, Flight flight) throws RemoteException, DeadlockException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		lockManager.Lock(id, ""+flightNumber, TrxnObj.WRITE);
		ops.push(new Operation(Operation.WRITE, Flight.getKey(flightNumber), readData(id, Flight.getKey(flightNumber))));
		writeData(id, Flight.getKey(flightNumber), flight);
	}

}
