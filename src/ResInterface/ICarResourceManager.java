package ResInterface;

import java.rmi.Remote;

import Transactions.ITransactionManager;

public interface ICarResourceManager extends Remote, CarFrontend, CarBackend, ITransactionManager {

}
