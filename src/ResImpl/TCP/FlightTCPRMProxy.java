package ResImpl.TCP;

import java.rmi.RemoteException;

import ResImpl.Flight;
import ResInterface.FlightBackend;

public class FlightTCPRMProxy extends AbstractTCPResourceManager implements FlightBackend {

	private String flightRMHost;
	private int flightRMPort;
	
	public FlightTCPRMProxy(String host, int port){
		flightRMHost = host;
		flightRMPort = port;
	}
	
	@Override
	public Flight getFlight(int id, int flightNumber) throws RemoteException {
		String[] info = send(concat("getflight", id, flightNumber), flightRMHost, flightRMPort).split(",");
		return new Flight(Integer.parseInt(info[0]), Integer.parseInt(info[1]), Integer.parseInt(info[2]));
	}

	@Override
	public void updateFlight(int id, int flightNumber, Flight flight)
			throws RemoteException {
		send(concat("updateflight", id, flightNumber, flight.getCount(), flight.getReserved(), flight.getPrice()), flightRMHost, flightRMPort);
	}

	@Override
	public String processInput(String line) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
