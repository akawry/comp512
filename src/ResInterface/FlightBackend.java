package ResInterface;

import java.rmi.RemoteException;

import ResImpl.Flight;

public interface FlightBackend {

    public Flight getFlight(int id, int flightNumber) throws RemoteException;
    
    public void updateFlight(int id, int flightNumber, Flight flight) throws RemoteException;
}
