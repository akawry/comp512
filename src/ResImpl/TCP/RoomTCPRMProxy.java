package ResImpl.TCP;

import java.rmi.RemoteException;

import ResImpl.Hotel;
import ResInterface.IRoomResourceManager;

public class RoomTCPRMProxy extends AbstractTCPResourceManager implements IRoomResourceManager {

	private String roomRMHost;
	private int roomRMPort;
	
	public RoomTCPRMProxy(String host, int port){
		roomRMHost = host;
		roomRMPort = port;
	}
	
	@Override
	public boolean addRooms(int id, String location, int numRooms, int price)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteRooms(int id, String location) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int queryRooms(int id, String location) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryRoomsPrice(int id, String location) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Hotel getRoom(int id, String location) throws RemoteException {
		String[] info = send(concat("getroom", id, location), roomRMHost, roomRMPort).split(",");
		return new Hotel(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]));
	}

	@Override
	public void updateRoom(int id, String location, Hotel room)
			throws RemoteException {
		send(concat("updateroom", id, location, room.getCount(), room.getReserved(), room.getPrice()), roomRMHost, roomRMPort);
	}

	@Override
	public String processInput(String line) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
