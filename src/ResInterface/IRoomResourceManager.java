package ResInterface;

import java.rmi.Remote;

import Transactions.ITransactionManager;

public interface IRoomResourceManager extends Remote, RoomFrontend, RoomBackend, ITransactionManager {

}
