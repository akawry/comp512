package ResImpl;

import java.rmi.RemoteException;
import java.util.Vector;

import ResInterface.ResourceManager;

public class CarResourceManagerImpl implements ResourceManager{

	@Override
	public boolean addFlight(int id, int flightNum, int flightSeats,
			int flightPrice) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addCars(int id, String location, int numCars, int price)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addRooms(int id, String location, int numRooms, int price)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int newCustomer(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean newCustomer(int id, int cid) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteFlight(int id, int flightNum) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteCars(int id, String location) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteRooms(int id, String location) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteCustomer(int id, int customer) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int queryFlight(int id, int flightNumber) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryCars(int id, String location) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryRooms(int id, String location) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String queryCustomerInfo(int id, int customer)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int queryFlightPrice(int id, int flightNumber)
			throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryCarsPrice(int id, String location) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryRoomsPrice(int id, String location) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean reserveFlight(int id, int customer, int flightNumber)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reserveCar(int id, int customer, String location)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reserveRoom(int id, int customer, String locationd)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean itinerary(int id, int customer, Vector flightNumbers,
			String location, boolean Car, boolean Room) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

}
