package ResInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import LockManager.DeadlockException;
import ResImpl.RMItem;
import Transactions.InvalidTransactionException;

public interface CustomerFrontend extends Remote {

    /* new customer just returns a unique customer identifier */
    public int newCustomer(int id) throws RemoteException, DeadlockException, InvalidTransactionException; 
    
    /* new customer with providing id */
    public boolean newCustomer(int id, int cid) throws RemoteException, DeadlockException, InvalidTransactionException;

    /* deleteCustomer removes the customer and associated reservations */
    public boolean deleteCustomer(int id,int customer) throws RemoteException, DeadlockException, InvalidTransactionException; 

    /* return a bill */
    public String queryCustomerInfo(int id,int customer) throws RemoteException, DeadlockException, InvalidTransactionException;
    
    public void undoLast(int id) throws RemoteException, InvalidTransactionException;

}