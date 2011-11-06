package ResInterface;

import java.rmi.Remote;

import Transactions.ITransactionManager;

public interface IFlightResourceManager extends Remote, FlightFrontend, FlightBackend, ITransactionManager {

}
