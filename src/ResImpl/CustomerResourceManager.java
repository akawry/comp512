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
import ResInterface.IRoomResourceManager;

public class CustomerResourceManager extends AbstractResourceManager implements ICustomerResourceManager {

	private IFlightResourceManager flightRM;
	private IRoomResourceManager roomRM;
	private ICarResourceManager carRM;
	
	public CustomerResourceManager(){
	}

	public CustomerResourceManager(ICarResourceManager carRM, IFlightResourceManager flightRM, IRoomResourceManager roomRM){
		this.carRM = carRM;
		this.roomRM = roomRM;
		this.flightRM = flightRM;
	}
	
	@Override
	public int newCustomer(int id) {
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
	public boolean newCustomer(int id, int customerID ) {
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
	public boolean deleteCustomer(int id, int customerID) {
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
				String key = reserveditem.getKey();
				try {
					if (key.startsWith("car")){
						String mkey = key.substring("car-".length()); 
						Car item = carRM.getCar(-1, mkey);
						Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") has reserved " + reserveditem.getKey() + "which is reserved" +  item.getReserved() +  " times and is still available " + item.getCount() + " times"  );
						item.setReserved(item.getReserved()-reserveditem.getCount());
						item.setCount(item.getCount()+reserveditem.getCount());
						carRM.updateCar(-1, mkey, item);
					} else if (key.startsWith("flight")){
						int mkey = Integer.parseInt(key.substring("flight-".length())); 
						Flight item = flightRM.getFlight(-1, mkey);
						Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") has reserved " + reserveditem.getKey() + "which is reserved" +  item.getReserved() +  " times and is still available " + item.getCount() + " times"  );
						item.setReserved(item.getReserved()-reserveditem.getCount());
						item.setCount(item.getCount()+reserveditem.getCount());
						flightRM.updateFlight(-1, mkey, item);
					} else if (key.startsWith("room")){
						String mkey = key.substring("room-".length()); 
						Hotel item = roomRM.getRoom(-1, mkey);
						Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") has reserved " + reserveditem.getKey() + "which is reserved" +  item.getReserved() +  " times and is still available " + item.getCount() + " times"  );
						item.setReserved(item.getReserved()-reserveditem.getCount());
						item.setCount(item.getCount()+reserveditem.getCount());
						roomRM.updateRoom(-1, mkey, item);
					}
				} catch (Exception exc){
					exc.printStackTrace();
				}
				
			}
			
			// remove the customer from the storage
			removeData(id, cust.getKey());
			
			Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") succeeded" );
			return true;
		}
	}

	@Override
	public String queryCustomerInfo(int id, int customerID) {
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
	public boolean reserveCar(int id, int customer, String location) throws RemoteException {
		Car car = carRM.getCar(id, location);
		boolean success = this.reserveItem(id, customer, car, location);
		if (success){
			// send back
			carRM.updateCar(id, location, car);
		}
		return success;
	}

	@Override
	public boolean reserveFlight(int id, int customer, int flightNumber)
			throws RemoteException {
		Flight flight = flightRM.getFlight(id, flightNumber);
		boolean success = reserveItem(id, customer, flight, String.valueOf(flightNumber));
		if (success)
			flightRM.updateFlight(id, flightNumber, flight);
		return success;
	}

	@Override
	public boolean reserveRoom(int id, int customer, String location)
			throws RemoteException {
		Hotel room = roomRM.getRoom(id, location);
		boolean success = reserveItem(id, customer, room, location);
		if (success)
			roomRM.updateRoom(id, location, room);
		return success;
	}

	@Override
	public boolean itinerary(int id, int customer, Vector<String> flightNumbers, String location, boolean Car, boolean Room) throws RemoteException {
		boolean success = true;
		
		// reserve flights 
		for (String s : flightNumbers){
			success &= reserveFlight(id, customer, Integer.parseInt(s));
		}
		
		// reserve car
		if (Car)
			success &= reserveCar(id, customer, location);
		
		// reserve room
		if (Room)
			success &= reserveRoom(id, customer, location);
		
		return success;
	}
	
	// reserve an item
	protected boolean reserveItem(int id, int customerID, String key,
			String location) {
		Trace.info("RM::reserveItem( " + id + ", customer=" + customerID + ", "
				+ key + ", " + location + " ) called");
		// Read customer object if it exists (and read lock it)
		Customer cust = (Customer) readData(id, Customer.getKey(customerID));
		if (cust == null) {
			Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", "
					+ key + ", " + location
					+ ")  failed--customer doesn't exist");
			return false;
		}

		// check if the item is available
		ReservableItem item = (ReservableItem) readData(id, key);
		if (item == null) {
			Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", "
					+ key + ", " + location + ") failed--item doesn't exist");
			return false;
		} else if (item.getCount() == 0) {
			Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", "
					+ key + ", " + location + ") failed--No more items");
			return false;
		} else {
			cust.reserve(key, location, item.getPrice());
			writeData(id, cust.getKey(), cust);

			// decrease the number of available items in the storage
			item.setCount(item.getCount() - 1);
			item.setReserved(item.getReserved() + 1);

			Trace.info("RM::reserveItem( " + id + ", " + customerID + ", "
					+ key + ", " + location + ") succeeded");
			return true;
		}
	}

	// reserve an item
	protected boolean reserveItem(int id, int customerID, ReservableItem item,
			String location) {

		// check if the item is available
		if (item == null) {
			Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", "
					+ item + ", " + location + ") failed--item doesn't exist");
			return false;
		} else {
			String key = item.getKey();
			if (item.getCount() == 0) {
				Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", "
						+ key + ", " + location + ") failed--No more items");
				return false;
			} else {

				Trace.info("RM::reserveItem( " + id + ", customer="
						+ customerID + ", " + key + ", " + location
						+ " ) called");
				// Read customer object if it exists (and read lock it)
				Customer cust = (Customer) readData(id,
						Customer.getKey(customerID));
				if (cust == null) {
					Trace.warn("RM::reserveItem( " + id + ", " + customerID
							+ ", " + key + ", " + location
							+ ")  failed--customer doesn't exist");
					return false;
				}

				cust.reserve(key, location, item.getPrice());
				writeData(id, cust.getKey(), cust);

				// decrease the number of available items in the storage
				item.setCount(item.getCount() - 1);
				item.setReserved(item.getReserved() + 1);

				Trace.info("RM::reserveItem( " + id + ", " + customerID + ", "
						+ key + ", " + location + ") succeeded");
				return true;
			}

		}

	}
}
