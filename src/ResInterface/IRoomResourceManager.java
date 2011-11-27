package ResInterface;

import java.rmi.Remote;

import FaultTolerance.ICrashable;
import Transactions.ITransactionManager;

public interface IRoomResourceManager extends Remote, RoomFrontend, RoomBackend, ITransactionManager, ICrashable {

}
