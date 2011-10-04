package ResInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import ResImpl.Car;

public interface ICarResourceManager extends Remote{

    
    /* Add cars to a location.  
     * This should look a lot like addFlight, only keyed on a string location
     * instead of a flight number.
     */
    public boolean addCars(int id, String location, int numCars, int price) 
	throws RemoteException; 
    
    
    /* Delete all Cars from a location.
     * It may not succeed if there are reservations for this location
     *
     * @return success
     */		    
    public boolean deleteCars(int id, String location) 
	throws RemoteException; 
    

    /* return the number of cars available at a location */
    public int queryCars(int id, String location) 
	throws RemoteException; 
    

    /* return the price of a car at a location */
    public int queryCarsPrice(int id, String location) 
	throws RemoteException;
    
    public Car getCar(int id, String location) throws RemoteException;
	
}
