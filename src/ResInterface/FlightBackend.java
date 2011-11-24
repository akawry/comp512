package ResInterface;

import java.rmi.RemoteException;

import FaultTolerance.CrashException;
import LockManager.DeadlockException;
import ResImpl.Flight;
import Transactions.InvalidTransactionException;

public interface FlightBackend {

    public Flight getFlight(int id, int flightNumber) throws RemoteException, DeadlockException, InvalidTransactionException, CrashException;
    
    public void updateFlight(int id, int flightNumber, Flight flight) throws RemoteException, DeadlockException, InvalidTransactionException, CrashException;
}
