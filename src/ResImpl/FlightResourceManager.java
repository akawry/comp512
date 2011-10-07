package ResImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import ResImpl.RMI.FlightRMIResourceManager;
import ResInterface.FlightBackend;
import ResInterface.FlightFrontend;

public class FlightResourceManager extends AbstractResourceManager implements FlightFrontend,FlightBackend {

	@Override
	public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) {
		Trace.info("RM::addFlight(" + id + ", " + flightNum + ", $" + flightPrice + ", " + flightSeats + ") called" );
		Flight curObj = (Flight) readData( id, Flight.getKey(flightNum) );
		if( curObj == null ) {
			// doesn't exist...add it
			Flight newObj = new Flight( flightNum, flightSeats, flightPrice );
			writeData( id, newObj.getKey(), newObj );
			Trace.info("RM::addFlight(" + id + ") created new flight " + flightNum + ", seats=" +
					flightSeats + ", price=$" + flightPrice );
		} else {
			// add seats to existing flight and update the price...
			curObj.setCount( curObj.getCount() + flightSeats );
			if( flightPrice > 0 ) {
				curObj.setPrice( flightPrice );
			} // if
			writeData( id, curObj.getKey(), curObj );
			Trace.info("RM::addFlight(" + id + ") modified existing flight " + flightNum + ", seats=" + curObj.getCount() + ", price=$" + flightPrice );
		} // else
		return(true);
	}

	@Override
	public boolean deleteFlight(int id, int flightNum) {
		return deleteItem(id, Flight.getKey(flightNum));
	}

	@Override
	public int queryFlight(int id, int flightNum) {
		return queryNum(id, Flight.getKey(flightNum));
	}

	@Override
	public int queryFlightPrice(int id, int flightNum ) {
		return queryPrice(id, Flight.getKey(flightNum));
	}

	@Override
	public Flight getFlight(int id, int flightNumber) {
		return (Flight) readData(id, Flight.getKey(flightNumber));
	}

	@Override
	public void updateFlight(int id, int flightNumber, Flight flight)
			throws RemoteException {
		writeData(id, Flight.getKey(flightNumber), flight);
	}
	

}
