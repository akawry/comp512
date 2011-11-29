package ResInterface;

import java.rmi.RemoteException;
import java.util.Vector;

import LockManager.DeadlockException;
import Transactions.InvalidTransactionException;

public interface ReservationFrontend {

    /* reserve a car at this location */
    public boolean reserveCar(int id, int customer, String location, boolean local) throws RemoteException, InvalidTransactionException, DeadlockException;
    
    /* Reserve a seat on this flight*/
    public boolean reserveFlight(int id, int customer, int flightNumber, boolean local) throws RemoteException, DeadlockException, InvalidTransactionException; 

    /* reserve a room certain at this location */
    public boolean reserveRoom(int id, int customer, String location, boolean local) throws RemoteException, DeadlockException, InvalidTransactionException; 
    
	/* reserve an itinerary */
    public boolean itinerary(int id,int customer,Vector<String> flightNumbers,String location, boolean Car, boolean Room, boolean local) throws RemoteException, NumberFormatException, DeadlockException, InvalidTransactionException; 
	
}
