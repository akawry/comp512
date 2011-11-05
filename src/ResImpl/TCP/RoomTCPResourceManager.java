package ResImpl.TCP;

import java.rmi.RemoteException;

import ResImpl.Car;
import ResImpl.CarResourceManager;
import ResImpl.Flight;
import ResImpl.Hotel;
import ResImpl.RoomResourceManager;

public class RoomTCPResourceManager extends AbstractTCPResourceManager {

	private RoomResourceManager rm;

	public RoomTCPResourceManager(RoomResourceManager rm){
		this.rm = rm;
	}
	
	@Override
	public String processInput(String line) {
		
		String[] toks = line.split(",");
		String type = toks[0];
		String res = "false";
		
		int id = Integer.parseInt(toks[1]);
		String location = toks[2];
		
		if (type.startsWith("new")){
			res = "" + rm.addRooms(id, location, Integer.parseInt(toks[3]), Integer.parseInt(toks[4]));
		} else if (type.startsWith("delete")){
			res = "" + rm.deleteRooms(id, location);
		} else if (type.startsWith("queryroomprice")){
			res = "" + rm.queryRoomsPrice(id, location);
		} else if (type.startsWith("queryroom")){
			res = "" + rm.queryRooms(id, location);
		} else if (type.startsWith("getroom")){
			Hotel room = rm.getRoom(id, location);
			res = room.getLocation() + "," + room.getCount() + "," + room.getPrice();
		} else if (type.startsWith("updateroom")){
			Hotel room = rm.getRoom(id, location);  
			room.setCount(Integer.parseInt(toks[3]));
			room.setReserved(Integer.parseInt(toks[4]));
			room.setPrice(Integer.parseInt(toks[5]));
			try {
				rm.updateRoom(id, location, room);	
				res = "true";
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		(new RoomTCPResourceManager(new RoomResourceManager())).listen(port);
	}

}
