package ResInterface;
	import java.rmi.Remote;	

	public interface ResourceFrontend extends Remote, CarFrontend, RoomFrontend,FlightFrontend, CustomerFrontend, ReservationFrontend{
	    			
	}
