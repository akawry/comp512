package ResInterface;

import java.rmi.Remote;

import FaultTolerance.ICrashable;
import Transactions.ITransactionManager;

public interface ICarResourceManager extends Remote, CarFrontend, CarBackend, ITransactionManager, ICrashable {

}
