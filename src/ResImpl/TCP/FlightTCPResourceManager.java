package ResImpl.TCP;

import java.rmi.RemoteException;

import ResImpl.Car;
import ResImpl.CarResourceManager;
import ResImpl.Flight;
import ResImpl.FlightResourceManager;
import ResImpl.RoomResourceManager;
import ResInterface.ICarResourceManager;

public class FlightTCPResourceManager extends AbstractTCPResourceManager {

	private FlightResourceManager rm;

	public FlightTCPResourceManager(FlightResourceManager rm){
		this.rm = rm;
	}
	
	@Override
	public String processInput(String line) {
		
		String[] toks = line.split(",");
		String type = toks[0];
		String res = "false";
		if (type.startsWith("new")){
			res = "" + rm.addFlight(Integer.parseInt(toks[1]), Integer.parseInt(toks[2]), Integer.parseInt(toks[3]), Integer.parseInt(toks[4]));
		} else if (type.startsWith("delete")){
			res = "" + rm.deleteFlight(Integer.parseInt(toks[1]), Integer.parseInt(toks[2]));
		} else if (type.startsWith("queryflightprice")){
			res = "" + rm.queryFlightPrice(Integer.parseInt(toks[1]), Integer.parseInt(toks[2]));
		} else if (type.startsWith("queryflight")){
			res = "" + rm.queryFlight(Integer.parseInt(toks[1]), Integer.parseInt(toks[2]));
		} else if (type.startsWith("getflight")){
			Flight flight = rm.getFlight(Integer.parseInt(toks[1]), Integer.parseInt(toks[2]));
			res = toks[2] + "," + flight.getCount() + "," + flight.getPrice();
		}
		
		return res;
	}
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		(new FlightTCPResourceManager(new FlightResourceManager())).listen(port);
	}

}
