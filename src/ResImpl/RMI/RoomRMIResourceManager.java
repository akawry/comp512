package ResImpl.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import FaultTolerance.ICrashable;
import FaultTolerance.IPingable;
import LockManager.DeadlockException;
import ResImpl.Hotel;
import ResImpl.RoomResourceManager;
import ResInterface.IRoomResourceManager;
import ResInterface.RoomBackend;
import ResInterface.RoomFrontend;
import Transactions.ITransactionManager;
import Transactions.InvalidTransactionException;
import Transactions.TransactionAbortedException;

public class RoomRMIResourceManager extends AbstractRMIResourceManager implements IRoomResourceManager {

	private RoomResourceManager rm;
	
	public RoomRMIResourceManager(RoomResourceManager rm) { 
		this.rm = rm;
	}
	
	@Override
	public String usage() {
		return "Usage: java ResImpl.RoomResourceManager [port]";
	}
	
	@Override
	public void register() throws Exception {
	    registry.bind("RMIRoom", UnicastRemoteObject.exportObject(this, 0));
	}

	@Override
	public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException, DeadlockException, InvalidTransactionException {
		return rm.addRooms(id, location, numRooms, price);
	}

	@Override
	public boolean deleteRooms(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException {
		return rm.deleteRooms(id, location);
	}

	@Override
	public int queryRooms(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException {
		return rm.queryRooms(id, location);
	}

	@Override
	public int queryRoomsPrice(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException {
		return rm.queryRoomsPrice(id, location);
	}

	@Override
	public Hotel getRoom(int id, String location) throws RemoteException, DeadlockException, InvalidTransactionException {
		return rm.getRoom(id, location);
	}

	public static void main(String[] args) {
		RoomRMIResourceManager rm = new RoomRMIResourceManager(new RoomResourceManager());
		rm.parseArgs(args) ;
		rm.launch();
	}

	@Override
	public boolean updateRoom(int id, String location, Hotel room)
			throws RemoteException, DeadlockException, InvalidTransactionException {
		return rm.updateRoom(id, location, room);
	}

	@Override
	public int start() throws RemoteException, InvalidTransactionException {
		return rm.start();
	}

	@Override
	public boolean commit(int transactionId) throws RemoteException, TransactionAbortedException, InvalidTransactionException {
		return rm.commit(transactionId);
	}

	@Override
	public void abort(int transactionId) throws RemoteException, InvalidTransactionException {
		rm.abort(transactionId);
	}

	@Override
	public boolean shutdown() throws RemoteException {
		return rm.shutdown();
	}

	@Override
	public boolean enlist(int transactionId) throws RemoteException, InvalidTransactionException {
		return rm.enlist(transactionId);
	}

	@Override
	public void undoLast(int id) throws RemoteException,
			InvalidTransactionException {
		rm.undoLast(id);
	}

	@Override
	protected void unregister() throws Exception {
		UnicastRemoteObject.unexportObject(this, true);
		registry.unbind("RMIRoom");
	}

}
