package ResInterface;

import java.rmi.RemoteException;

import LockManager.DeadlockException;
import ResImpl.Flight;
import Transactions.InvalidTransactionException;

public interface FlightBackend extends Backend {

    public Flight getFlight(int id, int flightNumber) throws RemoteException, DeadlockException, InvalidTransactionException;
    
    public boolean updateFlight(int id, int flightNumber, Flight flight) throws RemoteException, DeadlockException, InvalidTransactionException;
    
    public void undoLast(int id) throws RemoteException, InvalidTransactionException;
}
