package ResImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import ResInterface.IRoomResourceManager;

public class RoomResourceManager extends AbstractResourceManager implements IRoomResourceManager {

	@Override
	public boolean addRooms(int id, String location, int count, int price) throws RemoteException {
		Trace.info("RM::addRooms(" + id + ", " + location + ", " + count + ", $" + price + ") called" );
		Hotel curObj = (Hotel) readData( id, Hotel.getKey(location) );
		if( curObj == null ) {
			// doesn't exist...add it
			Hotel newObj = new Hotel( location, count, price );
			writeData( id, newObj.getKey(), newObj );
			Trace.info("RM::addRooms(" + id + ") created new room location " + location + ", count=" + count + ", price=$" + price );
		} else {
			// add count to existing object and update price...
			curObj.setCount( curObj.getCount() + count );
			if( price > 0 ) {
				curObj.setPrice( price );
			} // if
			writeData( id, curObj.getKey(), curObj );
			Trace.info("RM::addRooms(" + id + ") modified existing location " + location + ", count=" + curObj.getCount() + ", price=$" + price );
		} // else
		return(true);
	}

	@Override
	public boolean deleteRooms(int id, String location) throws RemoteException {
		return deleteItem(id, Hotel.getKey(location));	
	}

	@Override
	public int queryRooms(int id, String location) throws RemoteException {
		return queryNum(id, Hotel.getKey(location));
	}

	@Override
	public int queryRoomsPrice(int id, String location) throws RemoteException {
		return queryPrice(id, Hotel.getKey(location));
	}

	@Override
	public boolean reserveRoom(int id, int customerID, String location) throws RemoteException {
		return reserveItem(id, customerID, Hotel.getKey(location), location);
	}
	
	@Override
	public String usage() {
		return "Usage: java ResImpl.RoomResourceManager [port]";
	}
	
	@Override
	public void register() throws Exception {
		Remote rm = (Remote) UnicastRemoteObject.exportObject(this, 0);
		Registry registry = LocateRegistry.getRegistry();
		registry.rebind("MyRoomResourceManager", rm);
	}
	
	public static void main(String[] args) {
		RoomResourceManager rm = new RoomResourceManager();
		rm.launch(args);
	}

}
