package ResImpl.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import ResImpl.Car;
import ResImpl.CarResourceManager;
import ResInterface.ICarResourceManager;

public class CarRMIResourceManager extends AbstractRMIResourceManager implements Remote, ICarResourceManager {

	private CarResourceManager rm;
	
	public CarRMIResourceManager(CarResourceManager rm){
		this.rm = rm;
	}

	@Override
	public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {
		return rm.addCars(id, location, numCars, price);
	}

	@Override
	public boolean deleteCars(int id, String location) throws RemoteException {
		return rm.deleteCars(id, location);
	}

	@Override
	public int queryCars(int id, String location) throws RemoteException {
		return rm.queryCars(id, location);
	}

	@Override
	public int queryCarsPrice(int id, String location) throws RemoteException {
		return rm.queryCars(id, location);
	}

	@Override
	public Car getCar(int id, String location) throws RemoteException {
		return rm.getCar(id, location);
	}

	@Override
	public String usage() {
		return "Usage: ResImpl.CarResourceManager [port]";
	}

	@Override
	public void register() throws Exception {
	    registry.bind("akawry_MyCarResourceManager", UnicastRemoteObject.exportObject(this, 0));
	}
	
	public static void main(String[] args) {
		CarRMIResourceManager rm = new CarRMIResourceManager(new CarResourceManager());
		rm.parseArgs(args) ;
		rm.launch();
	}
	
}