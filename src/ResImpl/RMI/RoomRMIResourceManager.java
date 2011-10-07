package ResImpl.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import ResImpl.Hotel;
import ResImpl.RoomResourceManager;
import ResInterface.RoomBackend;
import ResInterface.RoomFrontend;

public class RoomRMIResourceManager extends AbstractRMIResourceManager implements Remote, RoomFrontend, RoomBackend {

	private RoomResourceManager rm;
	
	public RoomRMIResourceManager(RoomResourceManager rm) {
		this.rm = rm;
	}
	
	@Override
	public String usage() {
		return "Usage: java ResImpl.RoomResourceManager [port]";
	}
	
	@Override
	public void register() throws Exception {
	    registry.bind("RMIRoom", UnicastRemoteObject.exportObject(this,0));
	}

	@Override
	public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
		return rm.addRooms(id, location, numRooms, price);
	}

	@Override
	public boolean deleteRooms(int id, String location) throws RemoteException {
		return rm.deleteRooms(id, location);
	}

	@Override
	public int queryRooms(int id, String location) throws RemoteException {
		return rm.queryRooms(id, location);
	}

	@Override
	public int queryRoomsPrice(int id, String location) throws RemoteException {
		return rm.queryRoomsPrice(id, location);
	}

	@Override
	public Hotel getRoom(int id, String location) throws RemoteException {
		return rm.getRoom(id, location);
	}

	public static void main(String[] args) {
		RoomRMIResourceManager rm = new RoomRMIResourceManager(new RoomResourceManager());
		rm.parseArgs(args) ;
		rm.launch();
	}

	@Override
	public void updateRoom(int id, String location, Hotel room)
			throws RemoteException {
		rm.updateRoom(id, location, room);
	}

}
