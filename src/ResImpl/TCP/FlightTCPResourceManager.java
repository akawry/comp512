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
		
		int id = Integer.parseInt(toks[1]);
		int flightNum = Integer.parseInt(toks[2]);
		
		if (type.startsWith("new")){
			res = "" + rm.addFlight(id, flightNum, Integer.parseInt(toks[3]), Integer.parseInt(toks[4]));
		} else if (type.startsWith("delete")){
			res = "" + rm.deleteFlight(id, flightNum);
		} else if (type.startsWith("queryflightprice")){
			res = "" + rm.queryFlightPrice(id, flightNum);
		} else if (type.startsWith("queryflight")){
			res = "" + rm.queryFlight(id, flightNum);
		} else if (type.startsWith("getflight")){
			Flight flight = rm.getFlight(id, flightNum);
			res = toks[2] + "," + flight.getCount() + "," + flight.getPrice();
		} else if (type.startsWith("updateflight")){
			Flight flight = rm.getFlight(id, flightNum);  
			flight.setCount(Integer.parseInt(toks[3]));
			flight.setReserved(Integer.parseInt(toks[4]));
			flight.setPrice(Integer.parseInt(toks[5]));
			try {
				rm.updateFlight(id, flightNum, flight);	
				res = "true";
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		(new FlightTCPResourceManager(new FlightResourceManager())).listen(port);
	}

}
