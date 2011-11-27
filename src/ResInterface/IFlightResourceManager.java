package ResInterface;

import java.rmi.Remote;

import FaultTolerance.ICrashable;
import Transactions.ITransactionManager;

public interface IFlightResourceManager extends Remote, FlightFrontend, FlightBackend, ITransactionManager, ICrashable {

}
