package ResInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import LockManager.DeadlockException;

public interface CustomerFrontend extends Remote {

    /* new customer just returns a unique customer identifier */
    public int newCustomer(int id) throws RemoteException, DeadlockException; 
    
    /* new customer with providing id */
    public boolean newCustomer(int id, int cid) throws RemoteException, DeadlockException;

    /* deleteCustomer removes the customer and associated reservations */
    public boolean deleteCustomer(int id,int customer) throws RemoteException, DeadlockException; 

    /* return a bill */
    public String queryCustomerInfo(int id,int customer) throws RemoteException, DeadlockException; 
	
}