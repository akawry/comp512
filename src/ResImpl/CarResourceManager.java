package ResImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import ResInterface.ICarResourceManager;
import ResInterface.IResourceManager;

public class CarResourceManager extends AbstractResourceManager implements ICarResourceManager {

	@Override
	// Create a new car location or add cars to an existing location
	//  NOTE: if price <= 0 and the location already exists, it maintains its current price
	public boolean addCars(int id, String location, int count, int price) throws RemoteException {
		Trace.info("RM::addCars(" + id + ", " + location + ", " + count + ", $" + price + ") called" );
		Car curObj = (Car) readData( id, Car.getKey(location) );
		if( curObj == null ) {
			// car location doesn't exist...add it
			Car newObj = new Car( location, count, price );
			writeData( id, newObj.getKey(), newObj ); Trace.info("RM::addCars(" + id + ") created new location " + location + ", count=" + count + ", price=$" + price );
		} else {
			// add count to existing car location and update price...
			curObj.setCount( curObj.getCount() + count );
			if( price > 0 ) {
				curObj.setPrice( price );
			} // if
			writeData( id, curObj.getKey(), curObj );
			Trace.info("RM::addCars(" + id + ") modified existing location " + location + ", count=" + curObj.getCount() + ", price=$" + price );
		} // else
		return(true);
	}

	@Override
	public boolean deleteCars(int id, String location) throws RemoteException {
		return deleteItem(id, Car.getKey(location));
	}

	@Override
	public int queryCars(int id, String location) throws RemoteException {
		return queryNum(id, Car.getKey(location));
	}

	@Override
	public int queryCarsPrice(int id, String location) throws RemoteException {
		return queryPrice(id, Car.getKey(location));
	}

	/*@Override
	public boolean reserveCar(int id, int customerID, String location) throws RemoteException {
		Customer cust = customerRM.getCustomer(customerID);;
		System.err.println("Got customer: "+cust);
		return reserveItem(id, cust, Car.getKey(location), location);
	}*/

	@Override
	public String usage() {
		return "Usage: ResImpl.CarResourceManager [port]";
	}

	@Override
	public void register() throws Exception {
	    //ICarResourceManager rm = (ICarResourceManager) UnicastRemoteObject.exportObject((ICarResourceManager) this, 0);
	    registry.bind("akawry_MyCarResourceManager", UnicastRemoteObject.exportObject(this, 0));
	}
	
	public static void main(String[] args) {
		CarResourceManager rm = new CarResourceManager();
		rm.launch(args);
	}

	@Override
	public Car getCar(int id, String location) throws RemoteException {
		return (Car) readData(id, Car.getKey(location));
	}

}
