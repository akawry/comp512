package ResInterface;

import java.rmi.RemoteException;
import java.util.Vector;

public interface ReservationFrontend {
	  
    /* reserve a car at this location */
    public boolean reserveCar(int id, int customer, String location) throws RemoteException;
    
    /* Reserve a seat on this flight*/
    public boolean reserveFlight(int id, int customer, int flightNumber) throws RemoteException; 
    
    /* reserve a room certain at this location */
    public boolean reserveRoom(int id, int customer, String location) throws RemoteException; 

	/* reserve an itinerary */
    public boolean itinerary(int id,int customer,Vector<String> flightNumbers,String location, boolean Car, boolean Room) throws RemoteException; 
	
}
