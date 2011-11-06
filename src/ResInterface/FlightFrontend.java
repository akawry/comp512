package ResInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import LockManager.DeadlockException;
import Transactions.InvalidTransactionException;

public interface FlightFrontend {
    /* Add seats to a flight.  In general this will be used to create a new
     * flight, but it should be possible to add seats to an existing flight.
     * Adding to an existing flight should overwrite the current price of the
     * available seats.
     *
     * @return success.
     */
    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws RemoteException, DeadlockException, InvalidTransactionException; 
    
    /**
     *   Delete the entire flight.
     *   deleteflight implies whole deletion of the flight.  
     *   all seats, all reservations.  If there is a reservation on the flight, 
     *   then the flight cannot be deleted
     *
     * @return success.
     * @throws DeadlockException 
     * @throws InvalidTransactionException 
     */   
    public boolean deleteFlight(int id, int flightNum) throws RemoteException, InvalidTransactionException, DeadlockException; 

    /* queryFlight returns the number of empty seats. */
    public int queryFlight(int id, int flightNumber) throws RemoteException, InvalidTransactionException, DeadlockException;
     
    /* queryFlightPrice returns the price of a seat on this flight. */
    public int queryFlightPrice(int id, int flightNumber) throws RemoteException, InvalidTransactionException, DeadlockException; 
}
