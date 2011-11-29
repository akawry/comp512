package ResImpl;

import java.io.Serializable;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import FaultTolerance.CrashException;
import LockManager.DeadlockException;
import LockManager.TrxnObj;
import ResImpl.RMI.RMIMiddleWare;
import ResInterface.CarBackend;
import ResInterface.CustomerFrontend;
import ResInterface.FlightBackend;
import ResInterface.ICarResourceManager;
import ResInterface.IFlightResourceManager;
import ResInterface.IRoomResourceManager;
import ResInterface.ReservationFrontend;
import ResInterface.ResourceFrontend;
import ResInterface.RoomBackend;
import Transactions.ITransactionManager;
import Transactions.InvalidTransactionException;
import Transactions.Operation;
import Transactions.TransactionException;

public class CustomerResourceManager extends AbstractResourceManager implements ITransactionManager, CustomerFrontend, ReservationFrontend {
	private List<IFlightResourceManager> flightRMs;
	private List<IRoomResourceManager> roomRMs;
	private List<ICarResourceManager> carRMs;
	private FlightBackend flightRM;
	private RoomBackend roomRM;
	private CarBackend carRM;

	public CustomerResourceManager(){
	}

	public CustomerResourceManager(CarBackend carRM, FlightBackend flightRM, RoomBackend roomRM){
		this.carRM = carRM;
		this.roomRM = roomRM;
		this.flightRM = flightRM;
	}
	
	public CustomerResourceManager(List<ICarResourceManager> carRMs, List<IFlightResourceManager> flightRMs, List<IRoomResourceManager> roomRMs){
		this.carRMs = carRMs;
		this.roomRMs = roomRMs;
		this.flightRMs = flightRMs;
	}
	
	@Override
	public int newCustomer(int id) throws DeadlockException, InvalidTransactionException {
		Trace.info("INFO: RM::newCustomer(" + id + ") called" );
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		
		// Generate a globally unique ID for the new customer
		int cid = Integer.parseInt( String.valueOf(id) +
								String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND)) +
								String.valueOf( Math.round( Math.random() * 100 + 1 )));
		Customer cust = new Customer( cid );
		lockManager.Lock(id, cust.getKey(), TrxnObj.WRITE);
		ops.push(new Operation(Operation.DELETE, cust.getKey(), null));
		writeData( id, cust.getKey(), cust );
		Trace.info(this+":: newCustomer(" + cid + ") returns ID=" + cid );
		return cid;
	}

	@Override
	public boolean newCustomer(int id, int customerID ) throws DeadlockException, InvalidTransactionException {
		Trace.info("INFO: RM::newCustomer(" + id + ", " + customerID + ") called" );
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		
		Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
		if( cust == null ) {
			if (lockManager.Lock(id, Customer.getKey(customerID), TrxnObj.WRITE)){
				cust = new Customer(customerID);
				ops.push(new Operation(Operation.DELETE, cust.getKey(), null));
				writeData( id, cust.getKey(), cust );
				Trace.info("INFO: RM::newCustomer(" + id + ", " + customerID + ") created a new customer" );
				return true;
			} else {
				return false;
			}
		} else {
			Trace.info("INFO: RM::newCustomer(" + id + ", " + customerID + ") failed--customer already exists");
			return false;
		}
	}

	@Override
	public boolean deleteCustomer(int id, int customerID) throws DeadlockException, InvalidTransactionException {
		Trace.info(this+":: deleteCustomer(" + id + ", " + customerID + ") called" );
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		
		Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
		if( cust == null ) {
			Trace.warn(this+":: deleteCustomer(" + id + ", " + customerID + ") failed--customer doesn't exist" );
			return false;
		} else {			
			// Increase the reserved numbers of all reservable items which the customer reserved. 
			RMHashtable reservationHT = cust.getReservations();
			for(Enumeration e = reservationHT.keys(); e.hasMoreElements();){		
				String reservedkey = (String) (e.nextElement());
				ReservedItem reserveditem = cust.getReservedItem(reservedkey);
				Trace.info(this+":: deleteCustomer(" + id + ", " + customerID + ") has reserved " + reserveditem.getKey() + " " +  reserveditem.getCount() +  " times"  );
				String key = reserveditem.getKey();
				try {
					if (key.startsWith("car")){
						String mkey = key.substring("car-".length()); 
						Car item = carRM.getCar(-1, mkey);
						Trace.info(this+":: deleteCustomer(" + id + ", " + customerID + ") has reserved " + reserveditem.getKey() + "which is reserved" +  item.getReserved() +  " times and is still available " + item.getCount() + " times"  );
						item.setReserved(item.getReserved()-reserveditem.getCount());
						item.setCount(item.getCount()+reserveditem.getCount());
						carRM.updateCar(-1, mkey, item);
					} else if (key.startsWith("flight")){
						int mkey = Integer.parseInt(key.substring("flight-".length())); 
						Flight item = flightRM.getFlight(-1, mkey);
						Trace.info(this+":: deleteCustomer(" + id + ", " + customerID + ") has reserved " + reserveditem.getKey() + "which is reserved" +  item.getReserved() +  " times and is still available " + item.getCount() + " times"  );
						item.setReserved(item.getReserved()-reserveditem.getCount());
						item.setCount(item.getCount()+reserveditem.getCount());
						flightRM.updateFlight(-1, mkey, item);
					} else if (key.startsWith("room")){
						String mkey = key.substring("room-".length()); 
						Hotel item = roomRM.getRoom(-1, mkey);
						Trace.info(this+":: deleteCustomer(" + id + ", " + customerID + ") has reserved " + reserveditem.getKey() + "which is reserved" +  item.getReserved() +  " times and is still available " + item.getCount() + " times"  );
						item.setReserved(item.getReserved()-reserveditem.getCount());
						item.setCount(item.getCount()+reserveditem.getCount());
						roomRM.updateRoom(-1, mkey, item);
					}
				} catch (Exception exc){
					exc.printStackTrace();
				}
				
			}
			
			if (lockManager.Lock(id, cust.getKey(), TrxnObj.WRITE)){
				ops.push(new Operation(Operation.ADD, cust.getKey(), cust));
				// remove the customer from the storage
				removeData(id, cust.getKey());
				
				Trace.info(this+":: deleteCustomer(" + id + ", " + customerID + ") succeeded" );
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public String queryCustomerInfo(int id, int customerID) throws DeadlockException, InvalidTransactionException {
		Trace.info(this+":: queryCustomerInfo(" + id + ", " + customerID + ") called" );
		
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		
		Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
		if( cust == null ) {
			Trace.warn(this+":: queryCustomerInfo(" + id + ", " + customerID + ") failed--customer doesn't exist" );
			return "";   // NOTE: don't change this--WC counts on this value indicating a customer does not exist...
		} else {
			if (lockManager.Lock(id, cust.getKey(), TrxnObj.READ)){
				String s = cust.printBill();
				Trace.info(this+":: queryCustomerInfo(" + id + ", " + customerID + "), bill follows..." );
				System.out.println( s );
				return s;
			} else {
				return "";
			}
		} 
	}

	@Override
	public boolean reserveCar(int id, int customer, String location, boolean local) throws RemoteException, InvalidTransactionException, DeadlockException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		
		Car car = null;
		try {
			car = carRM.getCar(id, location);
		} catch (ConnectException e){
			throw new CrashException(e.getMessage(), carRM, true);
		}
		
		boolean success = false;
		if (lockManager.Lock(id, Customer.getKey(customer), TrxnObj.WRITE)){
			ops.push(new Operation(Operation.UNRESERVE, Customer.getKey(customer), Car.getKey(location)));
			success = this.reserveItem(id, customer, car, location);
		} 
		
		if (!local && success){
			for (int i = carRMs.size() - 1; i >= 0; i--){
				try {
					success &= carRMs.get(i).updateCar(id, location, car);
					if (!success){
						for (int j = carRMs.size() - 1; j > i; j--){
							carRMs.get(j).undoLast(id);
						}
						break;
					}
				} catch (ConnectException e){
					throw new CrashException(e.getMessage(), carRMs.get(i));
				} catch (TransactionException e){
					return false;
				} 
			}
		}
		
		return success;
	}
	
	@Override
	public boolean reserveFlight(int id, int customer, int flightNumber, boolean local)
			throws RemoteException, DeadlockException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		
		Flight flight = null;
		try {
			flight = flightRM.getFlight(id, flightNumber);
		} catch (ConnectException e){
			throw new CrashException(e.getMessage(), flightRM, true);
		}
		
		boolean success = false;
		if (lockManager.Lock(id, Customer.getKey(customer), TrxnObj.WRITE)){
			ops.push(new Operation(Operation.UNRESERVE, Customer.getKey(customer), Flight.getKey(flightNumber)));
			success = reserveItem(id, customer, flight, String.valueOf(flightNumber));
		}
		
		if (!local && success){
			for (int i = flightRMs.size() - 1; i >= 0; i--){
				try {
					success &= flightRMs.get(i).updateFlight(id, flightNumber, flight);
					if (!success){
						for (int j = flightRMs.size() - 1; j > i; j--){
							flightRMs.get(j).undoLast(id);
						}
						break;
					}
				} catch (ConnectException e){
					throw new CrashException(e.getMessage(), flightRMs.get(i));
				} catch (TransactionException e){
					return false;
				}
			}
		}
		
		return success;
	}

	@Override
	public boolean reserveRoom(int id, int customer, String location, boolean local) throws RemoteException, DeadlockException, InvalidTransactionException {
		Stack<Operation> ops = activeTransactions.get(id);
		if (ops == null){
			throw new InvalidTransactionException("No transaction with id "+id);
		}
		
		Hotel room = null;
		try {
			room = roomRM.getRoom(id, location);
		} catch (ConnectException e){
			throw new CrashException(e.getMessage(), roomRM, true);
		}
		
		boolean success = false;
		if (lockManager.Lock(id, Customer.getKey(customer), TrxnObj.WRITE)){
			ops.push(new Operation(Operation.UNRESERVE, Customer.getKey(customer), Hotel.getKey(location)));
			success = this.reserveItem(id, customer, room, location);
		}
		
		if (!local && success){
			for (int i = roomRMs.size() - 1; i >= 0; i--){
				try {
					success &= roomRMs.get(i).updateRoom(id, location, room);
					if (!success){
						for (int j = roomRMs.size() - 1; j > i; j--){
							roomRMs.get(j).undoLast(id);
						}
						break;
					}
				} catch (ConnectException e){
					throw new CrashException(e.getMessage(), roomRMs.get(i));
				} catch (TransactionException e){
					return false;
				}
			}
		}
		
		return success;
	}

	@Override
	public boolean itinerary(int id, int customer, Vector<String> flightNumbers, String location, boolean Car, boolean Room, boolean local) throws RemoteException, NumberFormatException, DeadlockException, InvalidTransactionException {
		boolean success = true;
		
		// reserve flights 
		for (String s : flightNumbers){
			success &= reserveFlight(id, customer, Integer.parseInt(s), local);
		}
		
		// reserve car
		if (Car)
			success &= reserveCar(id, customer, location, local);
		
		// reserve room
		if (Room)
			success &= reserveRoom(id, customer, location, local);
		
		return success;
	}
	
	// reserve an item
	protected boolean reserveItem(int id, int customerID, String key,
			String location) throws DeadlockException {
		Trace.info(this+":: reserveItem( " + id + ", customer=" + customerID + ", "
				+ key + ", " + location + " ) called");
		// Read customer object if it exists (and read lock it)
		Customer cust = (Customer) readData(id, Customer.getKey(customerID));
		if (cust == null) {
			Trace.warn(this+":: reserveItem( " + id + ", " + customerID + ", "
					+ key + ", " + location
					+ ")  failed--customer doesn't exist");
			return false;
		}

		// check if the item is available
		ReservableItem item = (ReservableItem) readData(id, key);
		if (item == null) {
			Trace.warn(this+":: reserveItem( " + id + ", " + customerID + ", "
					+ key + ", " + location + ") failed--item doesn't exist");
			return false;
		} else if (item.getCount() == 0) {
			Trace.warn(this+":: reserveItem( " + id + ", " + customerID + ", "
					+ key + ", " + location + ") failed--No more items");
			return false;
		} else {
			cust.reserve(key, location, item.getPrice());
			writeData(id, cust.getKey(), cust);

			// decrease the number of available items in the storage
			item.setCount(item.getCount() - 1);
			item.setReserved(item.getReserved() + 1);

			Trace.info(this+":: reserveItem( " + id + ", " + customerID + ", "
					+ key + ", " + location + ") succeeded");
			return true;
		}
	}

	// reserve an item
	protected boolean reserveItem(int id, int customerID, ReservableItem item,
			String location) throws DeadlockException {

		// check if the item is available
		if (item == null) {
			Trace.warn(this+":: reserveItem( " + id + ", " + customerID + ", "
					+ item + ", " + location + ") failed--item doesn't exist");
			return false;
		} else {
			String key = item.getKey();
			if (item.getCount() == 0) {
				Trace.warn(this+":: reserveItem( " + id + ", " + customerID + ", "
						+ key + ", " + location + ") failed--No more items");
				return false;
			} else {

				Trace.info(this+":: reserveItem( " + id + ", customer="
						+ customerID + ", " + key + ", " + location
						+ " ) called");
				// Read customer object if it exists (and read lock it)
				Customer cust = (Customer) readData(id,
						Customer.getKey(customerID));
				if (cust == null) {
					Trace.warn(this+":: reserveItem( " + id + ", " + customerID
							+ ", " + key + ", " + location
							+ ")  failed--customer doesn't exist");
					return false;
				}

				cust.reserve(key, location, item.getPrice());
				writeData(id, cust.getKey(), cust);

				// decrease the number of available items in the storage
				item.setCount(item.getCount() - 1);
				item.setReserved(item.getReserved() + 1);

				Trace.info(this+":: reserveItem( " + id + ", " + customerID + ", "
						+ key + ", " + location + ") succeeded");
				return true;
			}

		}

	}
	
	public void setCarRM(CarBackend carRM){
		this.carRM = carRM;
	}
	
	public void setFlightRM(FlightBackend flightRM){
		this.flightRM = flightRM;
	}
	
	public void setRoomRM(RoomBackend roomRM){
		this.roomRM = roomRM;
	}
}
