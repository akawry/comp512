package ResInterface;
import java.rmi.Remote;	

import Transactions.ITransactionManager;

public interface ResourceFrontend extends Remote, CarFrontend, RoomFrontend,FlightFrontend, CustomerFrontend, ReservationFrontend, ITransactionManager {

}
