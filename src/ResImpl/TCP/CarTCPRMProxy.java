package ResImpl.TCP;

import java.rmi.RemoteException;

import ResImpl.Car;
import ResInterface.CarBackend;

public class CarTCPRMProxy extends AbstractTCPResourceManager implements CarBackend {

	private String carRMHost;
	private int carRMPort;
	
	public CarTCPRMProxy(String host, int port){
		carRMHost = host;
		carRMPort = port;
	}
	
	@Override
	public Car getCar(int id, String location) throws RemoteException {
		String[] info = send(concat("getcar", id, location), carRMHost, carRMPort).split(",");
		return new Car(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]));
	}

	@Override
	public void updateCar(int id, String location, Car car)
			throws RemoteException {
		send(concat("updatecar", id, location, car.getCount(), car.getReserved(), car.getPrice()), carRMHost, carRMPort);
	}

	@Override
	public String processInput(String line) {
		// TODO Auto-generated method stub
		return null;
	}


}
