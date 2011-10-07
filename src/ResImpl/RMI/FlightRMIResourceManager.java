package ResImpl.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import ResImpl.Flight;
import ResImpl.FlightResourceManager;
import ResInterface.FlightBackend;
import ResInterface.FlightFrontend;

public class FlightRMIResourceManager extends AbstractRMIResourceManager implements Remote, FlightFrontend,FlightBackend {

	private FlightResourceManager rm;
	
	public FlightRMIResourceManager(FlightResourceManager rm){
		this.rm = rm;
	}

	@Override
	public String usage() {
		return "Usage: ResImpl.FlightResourceManager [port]";
	}

	@Override
	public void register() throws Exception {
	    registry.bind("RMiFlight", UnicastRemoteObject.exportObject(this, 0));
	}
	

	@Override
	public boolean addFlight(int id, int flightNum, int flightSeats,
			int flightPrice) throws RemoteException {
		return rm.addFlight(id, flightNum, flightSeats, flightPrice);
	}

	@Override
	public boolean deleteFlight(int id, int flightNum) throws RemoteException {
		return rm.deleteFlight(id, flightNum);
	}

	@Override
	public int queryFlight(int id, int flightNumber) throws RemoteException {
		return rm.queryFlight(id, flightNumber);
	}

	@Override
	public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
		return rm.queryFlightPrice(id, flightNumber);
	}

	@Override
	public Flight getFlight(int id, int flightNumber) throws RemoteException {
		return rm.getFlight(id, flightNumber);
	}
	

	public static void main(String[] args) {
		FlightRMIResourceManager rm = new FlightRMIResourceManager(new FlightResourceManager());
		rm.parseArgs(args) ;
		rm.launch();
	}

	@Override
	public void updateFlight(int id, int flightNumber, Flight flight)
			throws RemoteException {
		rm.updateFlight(id, flightNumber, flight);
	}

}
