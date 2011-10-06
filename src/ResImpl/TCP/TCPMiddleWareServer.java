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
					res = "" + rm.reserveCar(id, cid, toks[3]);
				} else if (type.contains("flight")){
					res = "" + rm.reserveFlight(id, cid, Integer.parseInt(toks[3]));
				} else if (type.contains("room")){
					res = "" + rm.reserveRoom(id, cid, toks[3]);
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
			
			Vector<String> flightNums = new Vector<String>();
			for (int i = 3; i < toks.length - 3; i++){
				flightNums.add(toks[i]);
			}
			
			boolean car = new Boolean(toks[toks.length - 2]);
			boolean room = new Boolean(toks[toks.length - 1]);
			try {
				res = "" + rm.itinerary(Integer.parseInt(toks[1]), Integer.parseInt(toks[2]), flightNums, location, car, room);
			} catch (Exception e) {
				e.printStackTrace();
				res = "false";
			}
				
		}
		
		return res; 
	}
	
	private String usage(){
		return "";
	}
	
	private void parseArgs(String[] args){
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

	private void bindCustomerRM(){
		rm = new CustomerResourceManager(new CarTCPRMProxy(carRMHost, carRMPort),
				new FlightTCPRMProxy(flightRMHost, flightRMPort),
				new RoomTCPRMProxy(roomRMHost, roomRMPort));
	}
	
	public static void main(String[] args) {
		TCPMiddleWareServer mw = new TCPMiddleWareServer();
		mw.parseArgs(args);
		mw.bindCustomerRM();
		mw.listen(mw.port);
	}

}
