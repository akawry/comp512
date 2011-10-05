package ResImpl.TCP;

import java.rmi.RemoteException;

import ResImpl.Car;
import ResImpl.CarResourceManager;
import ResImpl.Flight;
import ResImpl.Hotel;
import ResImpl.RoomResourceManager;
import ResInterface.ICarResourceManager;

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
		if (type.startsWith("new")){
			res = "" + rm.addRooms(Integer.parseInt(toks[1]), toks[2], Integer.parseInt(toks[3]), Integer.parseInt(toks[4]));
		} else if (type.startsWith("delete")){
			res = "" + rm.deleteRooms(Integer.parseInt(toks[1]), toks[2]);
		} else if (type.startsWith("queryroomprice")){
			res = "" + rm.queryRoomsPrice(Integer.parseInt(toks[1]), toks[2]);
		} else if (type.startsWith("queryroom")){
			res = "" + rm.queryRooms(Integer.parseInt(toks[1]), toks[2]);
		} else if (type.startsWith("getroom")){
			Hotel room = rm.getRoom(Integer.parseInt(toks[1]), toks[2]);
			res = room.getLocation() + "," + room.getCount() + "," + room.getPrice();
		}
		
		return res;
	}
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		(new RoomTCPResourceManager(new RoomResourceManager())).listen(port);
	}

}
