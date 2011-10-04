package ResInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ResImpl.Customer;

public interface ICustomerResourceManager extends Remote{

    /* new customer just returns a unique customer identifier */
    public int newCustomer(int id) 
	throws RemoteException; 
    
    /* new customer with providing id */
    public boolean newCustomer(int id, int cid)
    throws RemoteException;

    
    /* deleteCustomer removes the customer and associated reservations */
    public boolean deleteCustomer(int id,int customer) 
	throws RemoteException; 


    /* return a bill */
    public String queryCustomerInfo(int id,int customer) 
	throws RemoteException; 
    
    /* reserve a car at this location */
    public boolean reserveCar(int id, int customer, String location) 
	throws RemoteException;
    
    /* Reserve a seat on this flight*/
    public boolean reserveFlight(int id, int customer, int flightNumber) 
	throws RemoteException; 
    
    /* reserve a room certain at this location */
    public boolean reserveRoom(int id, int customer, String locationd) 
	throws RemoteException; 
	
}
