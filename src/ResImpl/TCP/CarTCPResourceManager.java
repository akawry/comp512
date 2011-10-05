package ResImpl.TCP;

import java.rmi.RemoteException;

import ResImpl.Car;
import ResImpl.CarResourceManager;
import ResInterface.ICarResourceManager;

public class CarTCPResourceManager extends AbstractTCPResourceManager {

	private CarResourceManager rm;

	public CarTCPResourceManager(CarResourceManager rm){
		this.rm = rm;
	}
	
	@Override
	public String processInput(String line) {
		
		String[] toks = line.split(",");
		String type = toks[0];
		String res = "false";
		if (type.startsWith("new")){
			res = "" + rm.addCars(Integer.parseInt(toks[1]), toks[2], Integer.parseInt(toks[3]), Integer.parseInt(toks[4]));
		} else if (type.startsWith("delete")){
			res = "" + rm.deleteCars(Integer.parseInt(toks[1]), toks[2]);
		} else if (type.startsWith("querycarprice")){
			res = "" + rm.queryCarsPrice(Integer.parseInt(toks[1]), toks[2]);
		} else if (type.startsWith("querycar")){
			res = "" + rm.queryCars(Integer.parseInt(toks[1]), toks[2]);
		} else if (type.startsWith("getcar")){
			Car car = rm.getCar(Integer.parseInt(toks[1]), toks[3]);
			res = car.getLocation() + "," + car.getCount() + "," + car.getPrice();
		}
		
		return res;
	}
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		(new CarTCPResourceManager(new CarResourceManager())).listen(port);
	}

}
