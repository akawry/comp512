package ResInterface;

import java.rmi.RemoteException;

import ResImpl.Car;

public interface CarBackend {

	 public Car getCar(int id, String location) throws RemoteException;
	    
	 public void updateCar(int id, String location, Car car) throws RemoteException;
		
}
