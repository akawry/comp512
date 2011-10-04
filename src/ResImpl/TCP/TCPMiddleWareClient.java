package ResImpl.TCP;

import java.rmi.RemoteException;
import java.util.Vector;

import ResImpl.Car;
import ResImpl.Flight;
import ResImpl.Hotel;
import ResInterface.IResourceManager;

public class TCPMiddleWareClient extends AbstractTCPResourceManager implements IResourceManager {

	private String middleWareServerHost = "localhost";
	private int middleWareServerPort = 2033;
	
	@Override
	public boolean addFlight(int id, int flightNum, int flightSeats,
			int flightPrice) throws RemoteException {
		
		return new Boolean(send(concat("newflight", id, flightNum, flightSeats, flightPrice), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public boolean deleteFlight(int id, int flightNum) throws RemoteException {
		return new Boolean(send(concat("deletefight", id, flightNum), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public int queryFlight(int id, int flightNumber) throws RemoteException {
		return new Integer(send(concat("queryflight", id, flightNumber), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public int queryFlightPrice(int id, int flightNumber)
			throws RemoteException {
		return new Integer(send(concat("queryflightprice", id, flightNumber), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public Flight getFlight(int id, int flightNumber) throws RemoteException {
		return null;
	}

	@Override
	public boolean addRooms(int id, String location, int numRooms, int price)
			throws RemoteException {
		return new Boolean(send(concat("newroom", id, location, numRooms, price), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public boolean deleteRooms(int id, String location) throws RemoteException {
		return new Boolean(send(concat("deleteroom", id, location), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public int queryRooms(int id, String location) throws RemoteException {
		return new Integer(send(concat("queryroom", id, location), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public int queryRoomsPrice(int id, String location) throws RemoteException {
		return new Integer(send(concat("queryroomprice", id, location), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public Hotel getRoom(int id, String location) throws RemoteException {
		return null;
	}

	@Override
	public boolean addCars(int id, String location, int numCars, int price)
			throws RemoteException {
		return new Boolean(send(concat("newcar", id, location, numCars, price), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public boolean deleteCars(int id, String location) throws RemoteException {
		return new Boolean(send(concat("deletecar", id, location), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public int queryCars(int id, String location) throws RemoteException {
		return new Integer(send(concat("querycar", id, location), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public int queryCarsPrice(int id, String location) throws RemoteException {
		return new Integer(send(concat("querycarprice", id, location), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public Car getCar(int id, String location) throws RemoteException {
		return null;
	}

	@Override
	public int newCustomer(int id) throws RemoteException {
		return new Integer(send(concat("newcustomer", id), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public boolean newCustomer(int id, int cid) throws RemoteException {
		return new Boolean(send(concat("newcustomer", id, cid), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public boolean deleteCustomer(int id, int customer) throws RemoteException {
		return new Boolean(send(concat("deletecustomer", id, customer), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public String queryCustomerInfo(int id, int customer) throws RemoteException {
		return send(concat("querycustomer", id, customer), middleWareServerHost, middleWareServerPort);

	}

	@Override
	public boolean reserveCar(int id, int customer, String location) throws RemoteException {
		return new Boolean(send(concat("reservecar", id, customer, location), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public boolean reserveFlight(int id, int customer, int flightNumber) throws RemoteException {
		return new Boolean(send(concat("reserveflight", id, customer, flightNumber), middleWareServerHost, middleWareServerPort));

	}

	@Override
	public boolean reserveRoom(int id, int customer, String location) throws RemoteException {
		return new Boolean(send(concat("reserveroom", id, customer, location), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public boolean itinerary(int id, int customer,
			Vector<String> flightNumbers, String location, boolean Car,
			boolean Room) throws RemoteException {
		String flights = "";
		for (String s : flightNumbers){
			flights += s + ",";
		}
		return new Boolean(send(concat("itinerary", id, customer, flights, location, Car, Room), middleWareServerHost, middleWareServerPort));
	}

	@Override
	public String processInput(String line) {
		return null;
	}

}
