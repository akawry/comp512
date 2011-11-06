package ResImpl.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

import LockManager.DeadlockException;
import ResImpl.AbstractResourceManager;
import ResImpl.Car;
import ResImpl.CarResourceManager;
import ResImpl.Customer;
import ResImpl.CustomerResourceManager;
import ResImpl.Flight;
import ResImpl.Hotel;
import ResImpl.RMHashtable;
import ResImpl.ReservableItem;
import ResImpl.ReservedItem;
import ResImpl.Trace;
import ResInterface.CarFrontend;
import ResInterface.FlightFrontend;
import ResInterface.ICarResourceManager;
import ResInterface.IFlightResourceManager;
import ResInterface.IRoomResourceManager;
import ResInterface.ResourceFrontend;
import ResInterface.RoomFrontend;
import Transactions.ITransactionManager;
import Transactions.InvalidTransactionException;
import Transactions.TransactionAbortedException;
import Transactions.TransactionManager;

public class RMIMiddleWare extends AbstractRMIResourceManager implements Remote, ResourceFrontend {

	private ICarResourceManager carRM;
	private IFlightResourceManager flightRM;
	private IRoomResourceManager roomRM;
	private CustomerResourceManager customerRM;
	private TransactionManager transactionManager;
	
	private int txnId = 1;
	
	// By default, if there is no args for car/room/flight, we try localhost:1099	
	// Explicit is better than implicit
	private String carserver = new String("localhost") ;
	private String flightserver = new String("localhost") ;
	private String roomserver = new String("localhost") ;
	private int carport = 1099 ;
	private int flightport = 1099 ;
	private int roomport = 1099 ;

	public CarFrontend getCarResourceManager() {
		return carRM;
	}

	public FlightFrontend getFlightResourceManager() {
		return flightRM;
	}

	public RoomFrontend getRoomResourceManager() {
		return roomRM;
	}

	public void setCarResourceManager(CarRMIResourceManager carRM) {
		this.carRM = carRM;
	}

	public void setFlightResourceManager(FlightRMIResourceManager flightRM) {
		this.flightRM = flightRM;
	}

	public void setRoomResourceManager(RoomRMIResourceManager roomRM) {
		this.roomRM = roomRM;
	}
	
	@Override
	public boolean addFlight(int id, int flightNum, int flightSeats,
			int flightPrice) throws RemoteException {
		
		try {
			return flightRM.addFlight(id, flightNum, flightSeats, flightPrice);
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteFlight(int id, int flightNum) throws RemoteException {
		try {
			return flightRM.deleteFlight(id, flightNum);
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int queryFlight(int id, int flightNumber) throws RemoteException {
		try {
			return flightRM.queryFlight(id, flightNumber);
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flightNumber;
	}

	@Override
	public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
		try {
			return flightRM.queryFlightPrice(id, flightNumber);
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flightNumber;
	}

	@Override
	public boolean reserveFlight(int id, int customer, int flightNumber) throws RemoteException {
		try {
			return customerRM.reserveFlight(id, customer, flightNumber);
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
		try {
			return roomRM.addRooms(id, location, numRooms, price);
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteRooms(int id, String location) throws RemoteException {
		try {
			return roomRM.deleteRooms(id, location);
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int queryRooms(int id, String location) throws RemoteException {
		try {
			return roomRM.queryRooms(id, location);
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public int queryRoomsPrice(int id, String location) throws RemoteException {
		try {
			return roomRM.queryRoomsPrice(id, location);
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public boolean reserveRoom(int id, int customer, String location) throws RemoteException {
		try {
			return customerRM.reserveRoom(id, customer, location);
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {
		try {
			return carRM.addCars(id, location, numCars, price);
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteCars(int id, String location) throws RemoteException {
		// ask customer if this car is reserved
		
		try {
			return carRM.deleteCars(id, location);
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int queryCars(int id, String location) throws RemoteException {
		try {
			return carRM.queryCars(id, location);
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public int queryCarsPrice(int id, String location) throws RemoteException {
		try {
			return carRM.queryCarsPrice(id, location);
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public boolean reserveCar(int id, int customer, String location) throws RemoteException {
		try {
			return customerRM.reserveCar(id, customer, location);
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int newCustomer(int id) throws RemoteException {
		try {
			return customerRM.newCustomer(id);
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public boolean newCustomer(int id, int customerID ) throws RemoteException {
		try {
			return customerRM.newCustomer(id, customerID);
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteCustomer(int id, int customerID) {
		try {
			return customerRM.deleteCustomer(id, customerID);
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String queryCustomerInfo(int id, int customerID) {
		try {
			return customerRM.queryCustomerInfo(id, customerID);
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return carserver;
	}

	@Override
	public boolean itinerary(int id, int customer, Vector<String> flightNumbers,
			String location, boolean Car, boolean Room) throws RemoteException {
		try {
			return customerRM.itinerary(id, customer, flightNumbers, location, Car, Room);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Room;
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
			carRM = (ICarResourceManager) LocateRegistry.getRegistry(carserver,carport).lookup("RMICar");
			roomRM = (IRoomResourceManager) LocateRegistry.getRegistry(roomserver,roomport).lookup("RMIRoom");
			flightRM = (IFlightResourceManager) LocateRegistry.getRegistry(flightserver,flightport).lookup("RMIFlight");
			customerRM = new CustomerResourceManager(carRM,flightRM,roomRM);
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
	public int start() throws RemoteException, InvalidTransactionException {
		int id = txnId;
		txnId++;
		enlist(id);
		return id;
	}

	@Override
	public boolean commit(int transactionId) throws RemoteException, TransactionAbortedException, InvalidTransactionException {
		boolean success = true;
		success &= carRM.commit(transactionId);
		success &= flightRM.commit(transactionId);
		success &= roomRM.commit(transactionId);
		return success;
	}

	@Override
	public void abort(int transactionId) throws RemoteException, InvalidTransactionException {
		carRM.abort(transactionId);
		flightRM.abort(transactionId);
		roomRM.abort(transactionId);
	}

	@Override
	public boolean shutdown() throws RemoteException {
		boolean success = true;
		success &= carRM.shutdown();
		success &= flightRM.shutdown();
		success &= roomRM.shutdown();
		return success;
	}

	@Override
	public boolean enlist(int transactionId) throws RemoteException, InvalidTransactionException {
		boolean success = true;
		success &= carRM.enlist(transactionId);
		success &= flightRM.enlist(transactionId);
		success &= roomRM.enlist(transactionId);
		return success;
	}
}
