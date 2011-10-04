package ResImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import ResInterface.ICarResourceManager;
import ResInterface.ICustomerResourceManager;
import ResInterface.IFlightResourceManager;
import ResInterface.IResourceManager;
import ResInterface.IRoomResourceManager;

public class MiddleWare implements Remote, IResourceManager {

	private ICarResourceManager carRM;
	private IFlightResourceManager flightRM;
	private IRoomResourceManager roomRM;
	private ICustomerResourceManager customerRM;
	
	public ICarResourceManager getCarResourceManager() {
		return carRM;
	}

	public IFlightResourceManager getFlightResourceManager() {
		return flightRM;
	}

	public IRoomResourceManager getRoomResourceManager() {
		return roomRM;
	}

	public ICustomerResourceManager getCustomerResourceManager() {
		return customerRM;
	}

	public void setCarResourceManager(ICarResourceManager carRM) {
		this.carRM = carRM;
	}

	public void setFlightResourceManager(IFlightResourceManager flightRM) {
		this.flightRM = flightRM;
	}

	public void setRoomResourceManager(IRoomResourceManager roomRM) {
		this.roomRM = roomRM;
	}

	public void setCustomerResourceManager(ICustomerResourceManager customerRM) {
		this.customerRM = customerRM;
	}
	
	@Override
	public boolean addFlight(int id, int flightNum, int flightSeats,
			int flightPrice) throws RemoteException {
		return flightRM.addFlight(id, flightNum, flightSeats, flightPrice);
	}

	@Override
	public boolean deleteFlight(int id, int flightNum) throws RemoteException {
		return flightRM.deleteFlight(id, flightNum);
	}

	@Override
	public int queryFlight(int id, int flightNumber) throws RemoteException {
		return flightRM.queryFlight(id, flightNumber);
	}

	@Override
	public int queryFlightPrice(int id, int flightNumber)
			throws RemoteException {
		return flightRM.queryFlightPrice(id, flightNumber);
	}

	@Override
	public boolean reserveFlight(int id, int customer, int flightNumber)
			throws RemoteException {
		return flightRM.reserveFlight(id, customer, flightNumber);
	}

	@Override
	public boolean addRooms(int id, String location, int numRooms, int price)
			throws RemoteException {
		return roomRM.addRooms(id, location, numRooms, price);
	}

	@Override
	public boolean deleteRooms(int id, String location) throws RemoteException {
		return roomRM.deleteRooms(id, location);
	}

	@Override
	public int queryRooms(int id, String location) throws RemoteException {
		return roomRM.queryRooms(id, location);
	}

	@Override
	public int queryRoomsPrice(int id, String location) throws RemoteException {
		return roomRM.queryRoomsPrice(id, location);
	}

	@Override
	public boolean reserveRoom(int id, int customer, String locationd)
			throws RemoteException {
		return roomRM.reserveRoom(id, customer, locationd);
	}

	@Override
	public boolean addCars(int id, String location, int numCars, int price)
			throws RemoteException {
		return carRM.addCars(id, location, numCars, price);
	}

	@Override
	public boolean deleteCars(int id, String location) throws RemoteException {
		return carRM.deleteCars(id, location);
	}

	@Override
	public int queryCars(int id, String location) throws RemoteException {
		return carRM.queryCars(id, location);
	}

	@Override
	public int queryCarsPrice(int id, String location) throws RemoteException {
		return carRM.queryCarsPrice(id, location);
	}

	@Override
	public boolean reserveCar(int id, int customer, String location)
			throws RemoteException {
		return carRM.reserveCar(id, customer, location);
	}

	@Override
	public int newCustomer(int id) throws RemoteException {
		return customerRM.newCustomer(id);
	}

	@Override
	public boolean newCustomer(int id, int cid) throws RemoteException {
		return customerRM.newCustomer(id, cid);
	}

	@Override
	public boolean deleteCustomer(int id, int customer) throws RemoteException {
		return customerRM.deleteCustomer(id, customer);
	}

	@Override
	public String queryCustomerInfo(int id, int customer)
			throws RemoteException {
		return customerRM.queryCustomerInfo(id, customer);
	}

	@Override
	public boolean itinerary(int id, int customer, Vector flightNumbers,
			String location, boolean Car, boolean Room) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	public static String usage(){
		return "Usage: java MiddleWare [opts]\n"+
				"where opts must include all of:\n\n"+
				"\t-car=[host]\n"+
				"\t-flight=[host]\n"+
				"\t-room=[host]\n"+
				"\t-customer=[host]\n"+
				"\t[-port=[host]]\n\n"+
				"and order does not matter";
	}
	
	/**
	 * Main entry point for the middle ware 
	 *
	 * @param args command line arguments
	 * 
	 */
	public static void main(String[] args) {
		
		if (args.length != 5 && args.length != 4){
			System.err.println("Wrong usage.");
			System.err.println(usage());
			System.exit(1);
		} else {
			
			MiddleWare mw = new MiddleWare();
			
			String[] valid = {"car", "flight", "room", "customer", "port"};
			String flag, host, server = "localhost";
			Registry registry;
			int port = 0;
			
			for (String arg : args){
				
				for (String s : valid){
					flag = "-" + s + "=";
					if (flag.equals(arg.substring(0, flag.length())) && arg.length() > flag.length()){
						host = arg.split("=")[1];
						
						// setting the port where this middleware runs  
						if (s.equals("port")){
							port = Integer.parseInt(host);
						} else {
							
							try  {
								registry = LocateRegistry.getRegistry(host);
								
								// setting host of car resource manager 
								if (s.equals("car")){
									ICarResourceManager crm = (ICarResourceManager) registry.lookup("akawry_MyCarResourceManager");
									if(crm != null) {
										System.out.println("Got the CarResourceManager");
									} else {
										System.out.println("Could not load the CarResourceManager");
									}
									
									mw.setCarResourceManager(crm);
									
								// setting host of flight resource manager 
								} else if (s.equals("flight")){
									
									IFlightResourceManager frm = (IFlightResourceManager) registry.lookup("akawry_MyFlightResourceManager");
									if(frm != null) {
										System.out.println("Got the FlightResourceManager");
									} else {
										System.out.println("Could not load the FlightResourceManager");
									}
									
									mw.setFlightResourceManager(frm);
									
								// setting host of room resource manager 
								} else if (s.equals("room")){
									
									IRoomResourceManager rrm = (IRoomResourceManager) registry.lookup("akawry_MyRoomResourceManager");
									if(rrm != null) {
										System.out.println("Got the RoomResourceManager");
									} else {
										System.out.println("Could not load the RoomResourceManager");
									}
									
									mw.setRoomResourceManager(rrm);
									
								// setting host of customer resource manager 
								} else if (s.equals("customer")){
									
									ICustomerResourceManager custrm = (ICustomerResourceManager) registry.lookup("akawry_MyCustomerResourceManager");
									if(custrm != null) {
										System.out.println("Got the CustomerResourceManager");
									} else {
										System.out.println("Could not load the CustomerResourceManager");
									}
									
									mw.setCustomerResourceManager(custrm);
								}
								
							} catch (Exception e)  {	
								System.err.println("Middleware exception: " + e.toString());
								e.printStackTrace();
							}
							
						}
						
					}
				}
				
			}
			
			if (mw.getCarResourceManager() == null){
				System.out.println("Middleware was unable to establish a connection with the CarResourceManager");
			} else if (mw.getFlightResourceManager() == null){
				System.out.println("Middleware was unable to establish a connection with the FlightResourceManager");
			} else if (mw.getRoomResourceManager() == null){
				System.out.println("Middleware was unable to establish a connection with the RoomResourceManager");
			} else if (mw.getCustomerResourceManager() == null){
				System.out.println("Middleware was unable to establish a connection with the CustomerResourceManager");
			
			// all resource managers successfully loaded 
			} else {
				
				try {
					IResourceManager rm = (IResourceManager) UnicastRemoteObject.exportObject(mw, port);
					registry = LocateRegistry.getRegistry(server);
					registry.rebind("akawry_MyGroupResourceManager", rm);
					
					System.out.println("Middleware server running on port "+port);
				} catch (Exception e){
					System.err.println("Middleware exception: " + e.toString());
					e.printStackTrace();
				}
			}
			
		}
	}
}
