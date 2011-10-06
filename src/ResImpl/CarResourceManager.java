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
	public boolean addCars(int id, String location, int count, int price) {
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
	public boolean deleteCars(int id, String location) {
		return deleteItem(id, Car.getKey(location));
	}

	@Override
	public int queryCars(int id, String location) {
		return queryNum(id, Car.getKey(location));
	}

	@Override
	public int queryCarsPrice(int id, String location) {
		return queryPrice(id, Car.getKey(location));
	}

	@Override
	public Car getCar(int id, String location) {
		return (Car) readData(id, Car.getKey(location));
	}

	@Override
	public void updateCar(int id, String location, Car car)
			throws RemoteException {
		writeData(id, Car.getKey(location), car);
		
	}

}
