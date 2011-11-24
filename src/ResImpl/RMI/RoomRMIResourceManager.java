package ResImpl.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import FaultTolerance.CrashException;
import FaultTolerance.CrashedRM;
import FaultTolerance.ICrashable;
import LockManager.DeadlockException;
import ResImpl.Hotel;
import ResImpl.RoomResourceManager;
import ResInterface.IRoomResourceManager;
import ResInterface.RoomBackend;
import ResInterface.RoomFrontend;
import Transactions.ITransactionManager;
import Transactions.InvalidTransactionException;
import Transactions.TransactionAbortedException;

public class RoomRMIResourceManager extends AbstractRMIResourceManager implements ITransactionManager, IRoomResourceManager, ICrashable {

	private IRoomResourceManager rm;
	
	public RoomRMIResourceManager(IRoomResourceManager rm) {
		this.rm = rm;
	}
	
	@Override
	public String usage() {
		return "Usage: java ResImpl.RoomResourceManager [port]";
	}
	
	@Override
	public void register() throws Exception {
	    registry.bind("RMIRoom", UnicastRemoteObject.exportObject(this,0));
	}

	@Override
	public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException, DeadlockException, InvalidTransactionException, CrashException {
		return rm.addRooms(id, location, numRooms, price);
	}

	@Override
	public boolean deleteRooms(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException, CrashException {
		return rm.deleteRooms(id, location);
	}

	@Override
	public int queryRooms(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException, CrashException {
		return rm.queryRooms(id, location);
	}

	@Override
	public int queryRoomsPrice(int id, String location) throws RemoteException, InvalidTransactionException, DeadlockException, CrashException {
		return rm.queryRoomsPrice(id, location);
	}

	@Override
	public Hotel getRoom(int id, String location) throws RemoteException, DeadlockException, InvalidTransactionException, CrashException {
		return rm.getRoom(id, location);
	}

	public static void main(String[] args) {
		RoomRMIResourceManager rm = new RoomRMIResourceManager(new RoomResourceManager());
		rm.parseArgs(args) ;
		rm.launch();
	}

	@Override
	public void updateRoom(int id, String location, Hotel room)
			throws RemoteException, DeadlockException, InvalidTransactionException, CrashException {
		rm.updateRoom(id, location, room);
	}

	@Override
	public int start() throws RemoteException, InvalidTransactionException, CrashException {
		return rm.start();
	}

	@Override
	public boolean commit(int transactionId) throws RemoteException, TransactionAbortedException, InvalidTransactionException, CrashException {
		return rm.commit(transactionId);
	}

	@Override
	public void abort(int transactionId) throws RemoteException, InvalidTransactionException, CrashException {
		rm.abort(transactionId);
	}

	@Override
	public boolean shutdown() throws RemoteException, CrashException {
		return rm.shutdown();
	}

	@Override
	public boolean enlist(int transactionId) throws RemoteException, InvalidTransactionException, CrashException {
		return rm.enlist(transactionId);
	}

	@Override
	public void crash() throws RemoteException {
		rm = new CrashedRM();
	}

}
