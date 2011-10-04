package ResImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import ResInterface.IFlightResourceManager;

public class FlightResourceManager extends AbstractResourceManager implements Remote, IFlightResourceManager {

	@Override
	public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws RemoteException {
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
	public boolean deleteFlight(int id, int flightNum) throws RemoteException {
		return deleteItem(id, Flight.getKey(flightNum));
	}

	@Override
	public int queryFlight(int id, int flightNum) throws RemoteException {
		return queryNum(id, Flight.getKey(flightNum));
	}

	@Override
	public int queryFlightPrice(int id, int flightNum ) throws RemoteException {
		return queryPrice(id, Flight.getKey(flightNum));
	}

	@Override
	public boolean reserveFlight(int id, int customerID, int flightNum) throws RemoteException {
		return reserveItem(id, customerID, Flight.getKey(flightNum), String.valueOf(flightNum));
	}

	@Override
	public String usage() {
		return "Usage: ResImpl.FlightResourceManager [port]";
	}

	@Override
	public void register() throws Exception {
		System.out.println(" Starting registry on port " + port) ;
	    IFlightResourceManager rm = (IFlightResourceManager) UnicastRemoteObject.exportObject((FlightResourceManager) this, port);
		Registry registry = LocateRegistry.getRegistry();
		registry.rebind("akawry_MyFlightResourceManager", rm);
	}
	
	public static void main(String[] args) {
		FlightResourceManager rm = new FlightResourceManager();
		rm.launch(args);
	}
	

}
