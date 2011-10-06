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
			Car car = rm.getCar(Integer.parseInt(toks[1]), toks[2]);
			res = car.getLocation() + "," + car.getCount() + "," + car.getPrice();
		} else if (type.startsWith("updatecar")){
			Car car = rm.getCar(Integer.parseInt(toks[1]), toks[2]); // construct this car from the arguments 
			car.setCount(Integer.parseInt(toks[3]));
			car.setReserved(Integer.parseInt(toks[4]));
			try {
				rm.updateCar(Integer.parseInt(toks[1]), toks[2], car);	
				res = "true";
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		(new CarTCPResourceManager(new CarResourceManager())).listen(port);
	}

}
