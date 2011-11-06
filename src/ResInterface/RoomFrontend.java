package ResInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import LockManager.DeadlockException;
import Transactions.InvalidTransactionException;

public interface RoomFrontend {
    /* Add rooms to a location.  
     * This should look a lot like addFlight, only keyed on a string location
     * instead of a flight number.
     */
    public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException, DeadlockException, InvalidTransactionException; 

    /* Delete all Rooms from a location.
     * It may not succeed if there are reservations for this location.
     *
     * @return success
     */
    public boolean deleteRooms(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException; 
    
    /* return the number of rooms available at a location */
    public int queryRooms(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException;  

    /* return the price of a room at a location */
    public int queryRoomsPrice(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException;
}
