package ResImpl.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

import ResImpl.AbstractResourceManager;
import ResImpl.Car;
import ResImpl.Customer;
import ResImpl.CustomerResourceManager;
import ResImpl.Flight;
import ResImpl.Hotel;
import ResImpl.RMHashtable;
import ResImpl.ReservableItem;
import ResImpl.ReservedItem;
import ResImpl.Trace;
import ResInterface.ICarResourceManager;
import ResInterface.ICustomerResourceManager;
import ResInterface.IFlightResourceManager;
import ResInterface.IResourceManager;
import ResInterface.IRoomResourceManager;

public class RMIMiddleWare extends AbstractRMIResourceManager implements Remote, IResourceManager {

	private ICarResourceManager carRM;
	private IFlightResourceManager flightRM;
	private IRoomResourceManager roomRM;
	private CustomerResourceManager customerRM;
	
	// By default, if there is no args for car/room/flight, we try localhost:1099	
	// Explicit is better than implicit
	private String carserver = new String("localhost") ;
	private String flightserver = new String("localhost") ;
	private String roomserver = new String("localhost") ;
	private int carport = 1099 ;
	private int flightport = 1099 ;
	private int roomport = 1099 ;

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
	public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
		return flightRM.queryFlightPrice(id, flightNumber);
	}

	@Override
	public boolean reserveFlight(int id, int customer, int flightNumber) throws RemoteException {
		return customerRM.reserveFlight(id, customer, flightNumber);
	}

	@Override
	public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
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
	public boolean reserveRoom(int id, int customer, String location) throws RemoteException {
		return customerRM.reserveRoom(id, customer, location);
	}

	@Override
	public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {
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
	public boolean reserveCar(int id, int customer, String location) throws RemoteException {
		return customerRM.reserveCar(id, customer, location);
	}

	@Override
	public int newCustomer(int id) throws RemoteException {
		return customerRM.newCustomer(id);
	}

	@Override
	public boolean newCustomer(int id, int customerID ) throws RemoteException {
		return customerRM.newCustomer(id, customerID);
	}

	@Override
	public boolean deleteCustomer(int id, int customerID) {
		return customerRM.deleteCustomer(id, customerID);
	}

	@Override
	public String queryCustomerInfo(int id, int customerID) {
		return customerRM.queryCustomerInfo(id, customerID);
	}

	@Override
	public boolean itinerary(int id, int customer, Vector<String> flightNumbers,
			String location, boolean Car, boolean Room) throws RemoteException {
		return customerRM.itinerary(id, customer, flightNumbers, location, Car, Room);
	}

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
		
		RMIMiddleWare mw = new RMIMiddleWare();
		mw.parseArgs(args) ;
		mw.launch() ;

	}

	@Override
	protected void parseArgs(String[] args) {

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
						this.port = Integer.parseInt(argval);
					} else if (s.equals("car") ) {
						carserver = argval.split(":")[0] ;
						carport = Integer.parseInt(argval.split(":")[1]) ;
					} else if (s.equals("room") ) {
						roomserver = argval.split(":")[0] ;
						roomport = Integer.parseInt(argval.split(":")[1]) ;
					} else if (s.equals("flight") ) {
						flightserver = argval.split(":")[0] ;
						flightport = Integer.parseInt(argval.split(":")[1]) ;
					}
				}
			}
		}
	}

	@Override
	protected void launch() {

		try {
			carRM = (ICarResourceManager)LocateRegistry.getRegistry(carserver,carport).lookup("akawry_MyCarResourceManager");
			roomRM = (IRoomResourceManager)LocateRegistry.getRegistry(roomserver,roomport).lookup("akawry_MyRoomResourceManager");
			flightRM = (IFlightResourceManager)LocateRegistry.getRegistry(flightserver,flightport).lookup("akawry_MyFlightResourceManager");
			customerRM = new CustomerResourceManager(carRM, flightRM, roomRM);
		} catch (Exception e) {
		    System.out.println("[ERROR] Middleware cannot get rmi object") ;
		    e.printStackTrace() ;
		    System.exit(1) ;
		}

		// Check if we have everything we need
		if (carRM == null){
			System.out.println("Middleware was unable to establish a connection with the CarResourceManager");
		} else if (flightRM == null){
			System.out.println("Middleware was unable to establish a connection with the FlightResourceManager");
		} else if (roomRM == null){
			System.out.println("Middleware was unable to establish a connection with the RoomResourceManager");
		}

		//start his own rmi
		super.launch();			
	}

	@Override 
	protected void register() throws Exception {
		registry.bind("RMIMiddleware", UnicastRemoteObject.exportObject(this,0));
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
