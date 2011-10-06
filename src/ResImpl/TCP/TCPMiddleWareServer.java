package ResImpl.TCP;

import java.rmi.RemoteException;
import java.util.Vector;

import ResImpl.Car;
import ResImpl.CarResourceManager;
import ResImpl.CustomerResourceManager;
import ResImpl.Flight;
import ResImpl.FlightResourceManager;
import ResImpl.Hotel;
import ResImpl.RoomResourceManager;
import ResImpl.RMI.RMIMiddleWare;
import ResInterface.IResourceManager;

public class TCPMiddleWareServer extends AbstractTCPResourceManager {

	private String carRMHost = "localhost";
	private String flightRMHost = "localhost";
	private String roomRMHost = "localhost";
	private int carRMPort;
	private int flightRMPort;
	private int roomRMPort;
	private int port;
	private CustomerResourceManager rm;
	
	public TCPMiddleWareServer(){
		this.rm = new CustomerResourceManager(); //Weird customer manager
	}
	
	private Car getCar(int id, String location){
		String[] info = send(concat("getcar", id, location), carRMHost, carRMPort).split(",");
		return new Car(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]));
	}
	
	private Hotel getRoom(int id, String location){
		String[] info = send(concat("getroom", id, location), roomRMHost, roomRMPort).split(",");
		return new Hotel(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]));
	}
	
	private Flight getFlight(int id, int flightNum){
		String[] info = send(concat("getflight", id, flightNum), flightRMHost, flightRMPort).split(",");
		return new Flight(Integer.parseInt(info[0]), Integer.parseInt(info[1]), Integer.parseInt(info[2]));
	}
	
	@Override
	public String processInput(String line) {
		String res = null;
		String[] toks = line.split(",");
		String type = toks[0];
		
		// handle locally 
		if (type.contains("reserve")){
			
			try {
				int id = Integer.parseInt(toks[1]),
					cid = Integer.parseInt(toks[2]);
	
				String[] info;
				if (type.contains("car")){
					//Car car = getCar(id, toks[3]);
					res = "" + rm.reserveCar(id, cid, toks[3]);
				} else if (type.contains("flight")){
					Flight flight = getFlight(id, Integer.parseInt(toks[3]));
					res = "" + rm.reserveFlight(id, cid, flight, Integer.parseInt(toks[3]));
				} else if (type.contains("room")){
					Hotel room = getRoom(id, toks[3]);
					res = "" + rm.reserveRoom(id, cid, room, toks[3]);
				}
			
			} catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		// forward request to car rm
		else if (type.contains("car")){
			res = send(line, carRMHost, carRMPort);
	
		// forward request to flight rm 	
		} else if (type.contains("flight")){
			res = send(line, flightRMHost, flightRMPort);
			
		// forward request to room rm
		} else if (type.contains("room")){
			res = send(line, roomRMHost, roomRMPort);
		
		// handle locally 
		} else if (type.contains("customer")){
			
			int id = Integer.parseInt(toks[1]),
				cid = toks.length == 3 ? Integer.parseInt(toks[2]) : 1;
			
			if (type.startsWith("new")){
				res = "" + (toks.length == 2 ? rm.newCustomer(id) : rm.newCustomer(id, cid));
			} else if (type.startsWith("delete")){
				res = "" + rm.deleteCustomer(id, cid);
			} else if (type.startsWith("query")){
				res = "" + rm.queryCustomerInfo(id, cid);
			}
			
		// handle locally
		} else if (type.contains("itinerary")){
			
			int id = Integer.parseInt(toks[1]);
			String location = toks[toks.length - 3];
			
			Vector<Integer> flightNums = new Vector<Integer>();
			for (int i = 3; i < toks.length - 3; i++){
				flightNums.add(Integer.parseInt(toks[i]));
			}
			
			Vector<Flight> flights = new Vector<Flight>();
			for (int i : flightNums){
				Flight flight = this.getFlight(id, i);
				flights.add(flight);
			}
			
			Car car = toks[toks.length - 2].equals("true") ? this.getCar(id, location) : null;
			Hotel room = toks[toks.length - 1].equals("true") ? this.getRoom(id, location) : null;
			res = "" + rm.itinerary(Integer.parseInt(toks[1]), Integer.parseInt(toks[2]), 
					flights, flightNums, location, car, room);
			
		}
		
		return res; 
	}
	
	private String usage(){
		return "";
	}
	
	public void parseArgs(String[] args){
		if (args.length != 4 && args.length != 3){
			System.err.println(usage());
			System.exit(1);
		}

		String[] valid = {"car", "flight", "room", "port"};
		String flag;


		for (String arg : args){

			for (String s : valid){
				flag = "-" + s + "=";
				if (flag.equals(arg.substring(0, flag.length())) && arg.length() > flag.length()){
					String argval = arg.split("=")[1];
					// if : setting the port where this middleware runs  
					// else : we need to parse the server name and server port	
					if (s.equals("port")){
						port = Integer.parseInt(argval);
					} else if (s.equals("car") ) {
						carRMHost = argval.split(":")[0] ;
						carRMPort = Integer.parseInt(argval.split(":")[1]) ;
						
					} else if (s.equals("room") ) {
						roomRMHost = argval.split(":")[0] ;
						roomRMPort = Integer.parseInt(argval.split(":")[1]) ;
					} else if (s.equals("flight") ) {
						flightRMHost = argval.split(":")[0] ;
						flightRMPort = Integer.parseInt(argval.split(":")[1]) ;
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		TCPMiddleWareServer mw = new TCPMiddleWareServer();
		mw.parseArgs(args);
		mw.listen(mw.port);
	}

}
