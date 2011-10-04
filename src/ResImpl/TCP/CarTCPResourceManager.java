package ResImpl.TCP;

import java.rmi.RemoteException;

import ResImpl.Car;
import ResInterface.ICarResourceManager;

public class CarTCPResourceManager extends AbstractTCPResourceManager {

	private ICarResourceManager rm;

	public CarTCPResourceManager(ICarResourceManager rm){
		this.rm = rm;
	}
	
	@Override
	public String processInput(String line) {
		// TODO Auto-generated method stub
		return null;
	}

}
