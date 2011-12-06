package ResImpl.RMI;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import FaultTolerance.Suspect;
import LockManager.DeadlockException;
import ResImpl.CustomerResourceManager;
import ResImpl.RMItem;
import ResImpl.Trace;
import ResInterface.IMiddleWare;
import ResInterface.ReservationFrontend;
import ResInterface.ResourceFrontend;
import Transactions.ITransactionManager;
import Transactions.InvalidTransactionException;
import Transactions.TransactionAbortedException;
import Transactions.TransactionException;

public class RMIMiddlewareGroupManager implements ResourceFrontend {

	private List<IMiddleWare> middlewares;
	private IMiddleWare middleware;
	private List<Suspect> suspectedCrashed;
	private Hashtable<IMiddleWare, String> hosts;
	private Hashtable<IMiddleWare, Integer> ports;
	
	public RMIMiddlewareGroupManager(List<IMiddleWare> rms){
		this.middlewares = rms;
		this.middleware = rms.get(0);
		this.suspectedCrashed = new ArrayList<Suspect>();
		hosts = new Hashtable<IMiddleWare, String>();
		ports = new Hashtable<IMiddleWare, Integer>();
		for (IMiddleWare mw : rms){
			try {
				hosts.put(mw, mw.getHost());
				ports.put(mw, mw.getPort());
			} catch (ConnectException e){
				handleMiddlewareCrash(mw);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void scheduleNextMiddleware(){
		if (middlewares.size() > 0){
			int idx = middlewares.indexOf(middleware);
			if (idx < middlewares.size() - 1)
				idx++;
			else
				idx = 0;
			
			middleware = middlewares.get(idx);
		}
		
	}
	
	private void handleMiddlewareCrash(ResourceFrontend middleware2){
		if (middlewares.size() > 0){
			Trace.error("[ERROR] One of the middlewares crashed ... ");
		} else {
			Trace.error("[ERROR] No more middlewares alive ... ");
		}
		middlewares.remove(middleware2);
		this.suspectedCrashed.add(new Suspect(hosts.get(middleware2), ports.get(middleware2), Suspect.MIDDLEWARE));	
	}

	@Override
	public boolean addCars(int id, String location, int numCars, int price) throws RemoteException, DeadlockException, InvalidTransactionException {
		boolean success = false;
		try {
			success = middleware.addCars(id, location, numCars, price);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return addCars(id, location, numCars, price);
		}
		scheduleNextMiddleware();
		return success;
	}

	@Override
	public boolean deleteCars(int id, String location) throws RemoteException, DeadlockException, InvalidTransactionException {
		boolean success = false;
		try {
			success = middleware.deleteCars(id, location);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return deleteCars(id, location);
		}
		scheduleNextMiddleware();
		return success;
	}

	@Override
	public int queryCars(int id, String location) throws RemoteException,
			DeadlockException, InvalidTransactionException {
		int cars = -1;
		try {
			cars = middleware.queryCars(id, location);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return queryCars(id, location);
		}
		scheduleNextMiddleware();
		return cars;
	}

	@Override
	public int queryCarsPrice(int id, String location) throws RemoteException,
			DeadlockException, InvalidTransactionException {
		int price = -1;
		try {
			price = middleware.queryCarsPrice(id, location);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return queryCarsPrice(id, location);
		}
		scheduleNextMiddleware();
		return price;
	}

	@Override
	public boolean addRooms(int id, String location, int numRooms, int price)
			throws RemoteException, DeadlockException,
			InvalidTransactionException {
		boolean success = false;
		try {
			success = middleware.addRooms(id, location, numRooms, price);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return addRooms(id, location, numRooms, price);
		}
		scheduleNextMiddleware();
		return success;
	}

	@Override
	public boolean deleteRooms(int id, String location) throws RemoteException,
			InvalidTransactionException, DeadlockException {
		boolean success = false;
		try {
			success = middleware.deleteRooms(id, location);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return deleteRooms(id, location);
		}
		scheduleNextMiddleware();
		return success;
	}

	@Override
	public int queryRooms(int id, String location) throws RemoteException,
			InvalidTransactionException, DeadlockException {
		int rooms = -1;
		try {
			rooms = middleware.queryRooms(id, location);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return queryRooms(id, location);
		}
		scheduleNextMiddleware();
		return rooms;
	}

	@Override
	public int queryRoomsPrice(int id, String location) throws RemoteException,
			InvalidTransactionException, DeadlockException {
		int price = -1;
		try {
			price = middleware.queryRoomsPrice(id, location);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return queryRoomsPrice(id, location);
		}
		scheduleNextMiddleware();
		return price;
	}

	@Override
	public boolean addFlight(int id, int flightNum, int flightSeats,
			int flightPrice) throws RemoteException, DeadlockException,
			InvalidTransactionException {
		boolean success = false;
		try {
			success = middleware.addFlight(id, flightNum, flightSeats, flightPrice);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return addFlight(id, flightNum, flightSeats, flightPrice);
		}
		scheduleNextMiddleware();
		return success;
	}

	@Override
	public boolean deleteFlight(int id, int flightNum) throws RemoteException,
			InvalidTransactionException, DeadlockException {
		boolean success = false;
		try {
			success = middleware.deleteFlight(id, flightNum);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return deleteFlight(id, flightNum);
		}
		scheduleNextMiddleware();
		return success;
	}

	@Override
	public int queryFlight(int id, int flightNumber) throws RemoteException,
			InvalidTransactionException, DeadlockException {
		int flights = -1;
		try {
			flights = middleware.queryFlight(id, flightNumber);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return queryFlight(id, flightNumber);
		}
		scheduleNextMiddleware();
		return flights;
	}

	@Override
	public int queryFlightPrice(int id, int flightNumber)
			throws RemoteException, InvalidTransactionException,
			DeadlockException {
		int price = -1;
		try {
			price = middleware.queryFlightPrice(id, flightNumber);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return queryFlightPrice(id, flightNumber);
		}
		scheduleNextMiddleware();
		return price;
	}

	@Override
	public int newCustomer(int id) throws RemoteException, DeadlockException, InvalidTransactionException {
		int cid = -1;
		boolean success = true;
		int i = 0;
		for (i = middlewares.size() - 1; i >= 0; i--){
			try {
				cid = middlewares.get(i).newCustomer(id);
				break;
			} catch (ConnectException e){
				handleMiddlewareCrash(middlewares.get(i));
			}
		}
		if (i > 0){
			for (int j = i - 1; j >= 0; j--){
				try {
					success &= middlewares.get(j).newCustomer(id, cid);
					if (!success){
						for (int k = middlewares.size() - 1; k > j; k--){
							middlewares.get(k).undoLast(id);
						}
						break;
					}
				} catch (ConnectException e){
					handleMiddlewareCrash(middlewares.get(j));
				}
			}
		}
		return cid;
	}

	@Override
	public boolean newCustomer(int id, int cid) throws RemoteException, DeadlockException, InvalidTransactionException {
		boolean success = true;
		for (int i = middlewares.size() - 1; i >= 0; i--){
			try {
				success &= middlewares.get(i).newCustomer(id, cid);
			} catch (ConnectException e){
				handleMiddlewareCrash(middlewares.get(i));
			}
			if (!success){
				for (int j = middlewares.size() - 1; j > i; j--){
					middlewares.get(i).undoLast(id);
				}
				break;
			}
		}
		return success;
	}

	@Override
	public boolean deleteCustomer(int id, int customer) throws RemoteException, DeadlockException, InvalidTransactionException {
		boolean success = true;
		for (int i = middlewares.size() - 1; i >= 0; i--){
			try {
				success &= middlewares.get(i).deleteCustomer(id, customer);
			} catch (ConnectException e){
				handleMiddlewareCrash(middlewares.get(i));
			}
			if (!success){
				for (int j = middlewares.size() - 1; j > i; j--){
					middlewares.get(i).undoLast(id);
				}
				break;
			}
		}
		return success;
	}

	@Override
	public String queryCustomerInfo(int id, int customer) throws RemoteException, DeadlockException, InvalidTransactionException {
		String cust = null;
		try {
			cust = middleware.queryCustomerInfo(id, customer);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return queryCustomerInfo(id, customer);
		}
		scheduleNextMiddleware();
		return cust;
	}

	@Override
	public boolean reserveCar(int id, int customer, String location, boolean local) throws RemoteException, InvalidTransactionException, DeadlockException {
		boolean success = true;
		boolean updateLocal = false;
		for (int i = middlewares.size() - 1; i >= 0; i--){
			try {
				middlewares.get(i).keepAlive(id);
				success &= middlewares.get(i).reserveCar(id, customer, location, updateLocal);
				if (!success){
					for (int j = middlewares.size() - 1; j > i; j--){
						middlewares.get(j).undoLast(id);
					}
					break;
				} else {
					updateLocal = true;
				}
			} catch (ConnectException e){
				handleMiddlewareCrash(middlewares.get(i));
			}
		}
		
		return success;
	}

	@Override
	public boolean reserveFlight(int id, int customer, int flightNumber, boolean local)
			throws RemoteException, DeadlockException,
			InvalidTransactionException {
		boolean success = true;
		boolean updateLocal = false;
		for (int i = middlewares.size() - 1; i >= 0; i--){
			try {
				middlewares.get(i).keepAlive(id);
				success &= middlewares.get(i).reserveFlight(id, customer, flightNumber, updateLocal);
				if (!success){
					for (int j = middlewares.size() - 1; j > i; j--){
						middlewares.get(j).undoLast(id);
					}
					break;
				} else {
					updateLocal = true;
				}
			} catch (ConnectException e){
				handleMiddlewareCrash(middlewares.get(i));
			}
		}

		return success;
	}

	@Override
	public boolean reserveRoom(int id, int customer, String location, boolean local)
			throws RemoteException, DeadlockException,
			InvalidTransactionException {
		boolean success = true;
		boolean updateLocal = false;
		for (int i = middlewares.size() - 1; i >= 0; i--){
			try {
				middlewares.get(i).keepAlive(id);
				success &= middlewares.get(i).reserveRoom(id, customer, location, updateLocal);
				if (!success){
					for (int j = middlewares.size() - 1; j > i; j--){
						middlewares.get(j).undoLast(id);
					}
					break;
				} else {
					updateLocal = true;
				}
			} catch (ConnectException e){
				handleMiddlewareCrash(middlewares.get(i));
			}
		}

		return success;
	}

	@Override
	public boolean itinerary(int id, int customer,
			Vector<String> flightNumbers, String location, boolean Car,
			boolean Room, boolean local) throws RemoteException, NumberFormatException,
			DeadlockException, InvalidTransactionException {
		boolean success = true;
		boolean updateLocal = false;
		for (int i = middlewares.size() - 1; i >= 0; i--){
			try {
				middlewares.get(i).keepAlive(id);
				success &= middlewares.get(i).itinerary(id, customer, flightNumbers, location, Car, Room, updateLocal);
				if (!success){
					for (int j = middlewares.size() - 1; j > i; j--){
						middlewares.get(j).undoLast(id);
					}
					break;
				} else {
					updateLocal = true;
				}
			} catch (ConnectException e){
				handleMiddlewareCrash(middlewares.get(i));
			}
		}

		return success;
	}

	@Override
	public int start() throws RemoteException, InvalidTransactionException {
		int id = -1;
		for (int i = middlewares.size() - 1; i >= 0; i--){
			try {
				id = middlewares.get(i).nextTransactionId();
				middlewares.get(i).startCustomerRM();
				middlewares.get(i).enlistCustomerRM(id);
			} catch (ConnectException e){
				handleMiddlewareCrash(middlewares.get(i));
			}
		}
		
		for (int i = middlewares.size() - 1; i >= 0; i--){
			try {
				middlewares.get(i).start();
				middlewares.get(i).enlist(id);
				break;
			} catch (ConnectException e){
				handleMiddlewareCrash(middlewares.get(i));
			}
		}
		
		return id;
	}

	@Override
	public boolean commit(int transactionId) throws RemoteException,
			TransactionAbortedException, InvalidTransactionException {
		
		boolean success = false;
		try {
			success = middleware.commit(transactionId);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return commit(transactionId);
		}
		
		if (success){
			for (int i = middlewares.size() - 1; i >= 0; i--){
				try {
					middlewares.get(i).commitCustomerRM(transactionId);
				} catch (ConnectException e){
					handleMiddlewareCrash(middlewares.get(i));
				}
			}
		}
		
		return success;
	}

	@Override
	public void abort(int transactionId) throws RemoteException,
			InvalidTransactionException {
		try {
			middleware.abort(transactionId);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				abort(transactionId);
			return;
		}
		
		for (int i = middlewares.size() - 1; i >= 0; i--){
			try {
				middlewares.get(i).abortCustomerRM(transactionId);
			} catch (ConnectException e){
				handleMiddlewareCrash(middlewares.get(i));
			}
		}
	}

	@Override
	public boolean shutdown() throws RemoteException {
		boolean success = true;
		try {
			success = middleware.shutdown();
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				return shutdown();
		}
		
		for (int i = middlewares.size() - 1; i >= 0; i--){
			try {
				middlewares.get(i).shutdownCustomerRM();
			} catch (ConnectException e){
				handleMiddlewareCrash(middlewares.get(i));
			}
		}
		
		return success;
	}

	@Override
	public boolean enlist(int transactionId) throws RemoteException,
			InvalidTransactionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void crash() throws RemoteException {
		// shouldn't be able to crash the group manager ... 
	}

	@Override
	public void crashHost(String host, int num) throws RemoteException {
		try {
			middleware.crashHost(host, num);
		} catch (ConnectException e){
			handleMiddlewareCrash(middleware);
			scheduleNextMiddleware();
			if (middlewares.size() > 0)
				crashHost(host, num);
			return;
		}
		scheduleNextMiddleware();
	}

	@Override
	public void crashType(String type, int num) throws RemoteException {
		if (type.equalsIgnoreCase("middleware")){
			Random rand = new Random();
			for (int i = 0; i < num; i++){
				if (middlewares.size() > 0){
					IMiddleWare mw = middlewares.remove(rand.nextInt(middlewares.size()));
					mw.crash();
				} else {
					break;
				}
			}
		} else {
			try {
				middleware.crashType(type, num);
			} catch (ConnectException e){
				handleMiddlewareCrash(middleware);
				scheduleNextMiddleware();
				if (middlewares.size() > 0)
					crashType(type, num);
				return;
			}
			scheduleNextMiddleware();
		}
	}

	@Override
	public void undoLast(int id) throws RemoteException, InvalidTransactionException {

	}
}
