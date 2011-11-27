package ResInterface;

import java.rmi.RemoteException;

import LockManager.DeadlockException;
import ResImpl.Hotel;
import Transactions.InvalidTransactionException;

public interface RoomBackend extends Backend {
	
    public Hotel getRoom(int id, String location) throws RemoteException, DeadlockException, InvalidTransactionException;
    
    public boolean updateRoom(int id, String location, Hotel room) throws RemoteException, DeadlockException, InvalidTransactionException;
    
    public void undoLast(int id) throws RemoteException, InvalidTransactionException;
}
