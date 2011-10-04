package ResImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

import ResInterface.ICarResourceManager;
import ResInterface.ICustomerResourceManager;
import ResInterface.IFlightResourceManager;
import ResInterface.IResourceManager;
import ResInterface.IRoomResourceManager;

public class MiddleWare extends AbstractResourceManager implements Remote, IResourceManager {

	private ICarResourceManager carRM;
	private IFlightResourceManager flightRM;
	private IRoomResourceManager roomRM;
	
	public ICarResourceManager getCarResourceManager() {
		return carRM;
	}

	public IFlightResourceManager getFlightResourceManager() {
		return flightRM;
	}

	public IRoomResourceManager getRoomResourceManager() {
		return roomRM;
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
		return reserveItem(id, customer, flightRM.getFlight(id, flightNumber), String.valueOf(flightNumber));
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
	public boolean reserveRoom(int id, int customer, String location)
			throws RemoteException {
		return reserveItem(id, customer, roomRM.getRoom(id, location), location);
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
		return reserveItem(id, customer, carRM.getCar(id, location), location);
	}

	@Override
	public int newCustomer(int id) throws RemoteException {
		Trace.info("INFO: RM::newCustomer(" + id + ") called" );
		// Generate a globally unique ID for the new customer
		int cid = Integer.parseInt( String.valueOf(id) +
								String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND)) +
								String.valueOf( Math.round( Math.random() * 100 + 1 )));
		Customer cust = new Customer( cid );
		writeData( id, cust.getKey(), cust );
		Trace.info("RM::newCustomer(" + cid + ") returns ID=" + cid );
		return cid;
	}

	@Override
	public boolean newCustomer(int id, int customerID ) throws RemoteException {
		Trace.info("INFO: RM::newCustomer(" + id + ", " + customerID + ") called" );
		Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
		if( cust == null ) {
			cust = new Customer(customerID);
			writeData( id, cust.getKey(), cust );
			Trace.info("INFO: RM::newCustomer(" + id + ", " + customerID + ") created a new customer" );
			return true;
		} else {
			Trace.info("INFO: RM::newCustomer(" + id + ", " + customerID + ") failed--customer already exists");
			return false;
		}
	}

	@Override
	public boolean deleteCustomer(int id, int customerID) throws RemoteException {
		Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") called" );
		Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
		if( cust == null ) {
			Trace.warn("RM::deleteCustomer(" + id + ", " + customerID + ") failed--customer doesn't exist" );
			return false;
		} else {			
			// Increase the reserved numbers of all reservable items which the customer reserved. 
			RMHashtable reservationHT = cust.getReservations();
			for(Enumeration e = reservationHT.keys(); e.hasMoreElements();){		
				String reservedkey = (String) (e.nextElement());
				ReservedItem reserveditem = cust.getReservedItem(reservedkey);
				Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") has reserved " + reserveditem.getKey() + " " +  reserveditem.getCount() +  " times"  );
				ReservableItem item  = (ReservableItem) readData(id, reserveditem.getKey());
				Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") has reserved " + reserveditem.getKey() + "which is reserved" +  item.getReserved() +  " times and is still available " + item.getCount() + " times"  );
				item.setReserved(item.getReserved()-reserveditem.getCount());
				item.setCount(item.getCount()+reserveditem.getCount());
			}
			
			// remove the customer from the storage
			removeData(id, cust.getKey());
			
			Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") succeeded" );
			return true;
		}
	}

	@Override
	public String queryCustomerInfo(int id, int customerID) throws RemoteException {
		Trace.info("RM::queryCustomerInfo(" + id + ", " + customerID + ") called" );
		Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
		if( cust == null ) {
			Trace.warn("RM::queryCustomerInfo(" + id + ", " + customerID + ") failed--customer doesn't exist" );
			return "";   // NOTE: don't change this--WC counts on this value indicating a customer does not exist...
		} else {
				String s = cust.printBill();
				Trace.info("RM::queryCustomerInfo(" + id + ", " + customerID + "), bill follows..." );
				System.out.println( s );
				return s;
		} 
	}

	@Override
	public boolean itinerary(int id, int customer, Vector<String> flightNumbers,
			String location, boolean Car, boolean Room) throws RemoteException {
		
		boolean success = true;
		
		// reserve flights 
		for (String s : flightNumbers){
			success &= this.reserveFlight(id, customer, Integer.parseInt(s));
		}
		
		// reserve car
		if (Car)
			success &= this.reserveCar(id, customer, location);
		
		// reserve room
		if (Room)
			success &= this.reserveRoom(id, customer, location);
		
		return success;
	}
	
	@Override
	public String usage(){
		return "Usage: java MiddleWare [opts]\n"+
				"where opts must include all of:\n\n"+
				"\t-car=[host]\n"+
				"\t-flight=[host]\n"+
				"\t-room=[host]\n"+
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
		
		MiddleWare mw = new MiddleWare();
		
		if (args.length != 4 && args.length != 3){
			System.err.println("Wrong usage.");
			System.err.println(mw.usage());
			System.exit(1);
		} else {
			
			String[] valid = {"car", "flight", "room", "port"};
			String flag, host;
			Registry registry;
			int port = 0;
			
			for (String arg : args){
				
				for (String s : valid){
					flag = "-" + s + "=";
					if (flag.equals(arg.substring(0, flag.length())) && arg.length() > flag.length()){
						host = arg.split("=")[1];
						
						// setting the port where this middleware runs  
						if (s.equals("port")){
							mw.port = Integer.parseInt(host);

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
			
			// all resource managers successfully loaded 
			} else {
				
				try {
					mw.register();
					System.out.println("Middleware server running on port "+mw.port);
				} catch (Exception e){
					System.err.println("Middleware exception: " + e.toString());
					e.printStackTrace();
				}
			}
			
		}
	}

	@Override
	protected void register() throws Exception {
		IResourceManager rm = (IResourceManager) UnicastRemoteObject.exportObject((IResourceManager) this, port);
		Registry registry = LocateRegistry.getRegistry();
		registry.rebind("akawry_MyGroupResourceManager", rm);
	}

	@Override
	public Flight getFlight(int id, int flightNumber) throws RemoteException {
		return flightRM.getFlight(id, flightNumber);
	}

	@Override
	public Hotel getRoom(int id, String location) throws RemoteException {
		return roomRM.getRoom(id, location);
	}

	@Override
	public Car getCar(int id, String location) throws RemoteException {
		return carRM.getCar(id, location);
	}
}
