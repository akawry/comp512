package Client ;

import java.util.*;
import java.util.concurrent.*;
import ResInterface.*;

public class LoopClient implements Callable {

	private static ResourceFrontend rm;
	private int loopnb ;
	private int trsec ;
	private int type ;

	public LoopClient(ResourceFrontend r,int l, int tr, int t) {
		rm = r ;
		loopnb = l ;
		trsec = tr ;
		type = t ;
	}

	public Integer call() {
		long wavelength = 0 ;
		if (trsec > 0) {
			// if trsec = 0 , no sleep
			wavelength = 1000000 / (long)trsec ; 
		}
		long total = 0 ;

		for (int i = 0 ; i < loopnb ; i++) {
			long res = 0 ;
			if (type == 1) {
				res = bigTransactionOneRM() ;
			} else if (type == 2) {
				res = bigTransactionMultipleRM() ;
			} else if (type == 3) {
				res = readTransaction() ;
			} else if (type == 4) {
				res = writeTransaction() ;
			} else {
				//default
				res = 0 ;
			}

			long sleeptime = wavelength - res ;
			if (sleeptime < 0 ) {
				//	System.out.println("Frequency too high by " + (-sleeptime) + " microseconds") ;
				//	return ;
			} else {
				try {
					//Put some variation in sleep +/- x
					int sleeptime_milli = (int)sleeptime ;
					sleeptime_milli = sleeptime_milli / 1000 ;
					Random  generator = new Random() ;
					int x = generator.nextInt(2 * sleeptime_milli) ;
					x = x - sleeptime_milli ;
					int variation = sleeptime_milli + x ;
					//System.out.println("Sleeping for " + variation + " microseconds") ;
					Thread.sleep(variation) ;
					//res += variation ;
				} catch (Exception e) {}
			}
			total += res ;
		}
		return((int)total/loopnb) ;

	}

	private static long bigTransactionMultipleRM() {
		try {
			long startTime = System.nanoTime() ;
			int tid = rm.start() ;
			String room = "ROOM" + Integer.toString(tid) ;
			String car = "CAR" + Integer.toString(tid) ;
			//String flight = "FLIGHT" + Integer.toString(tid) ;
			rm.addRooms(tid, room, tid, tid) ;
			rm.addCars(tid, car, tid,  tid) ;
			rm.addFlight(tid, tid, tid, tid) ;
			rm.queryRooms(tid,room);
			rm.queryCars(tid, car);
			rm.queryFlight(tid, tid);
			rm.deleteRooms(tid,room) ;
			rm.deleteCars(tid, car) ;
			rm.deleteFlight(tid, tid) ;
			rm.commit(tid) ;
			long executionTime = System.nanoTime() - startTime;
			return executionTime/1000 ; //return value in microseconds
		} catch ( Exception e) {
			System.err.println("Error during variousTransation execution" + e ) ; 
			e.printStackTrace() ;
			return 0 ;
		}
	}


	private static long bigTransactionOneRM() {
		try {
			long startTime = System.nanoTime() ;
			int tid = rm.start() ;
			String loc1 = "ROOM" + Integer.toString(tid + 1) ;
			String loc2 = "ROOM" + Integer.toString(tid + 2) ;
			String loc3 = "ROOM" + Integer.toString(tid + 3) ;
			rm.addRooms(tid,loc1 , tid, tid) ;
			rm.addRooms(tid, loc2 , tid, tid) ;
			rm.addRooms(tid, loc3 , tid, tid) ;
			rm.queryRooms(tid, loc1);
			rm.queryRooms(tid, loc2);
			rm.queryRooms(tid, loc3);
			rm.deleteRooms(tid, loc1) ;
			rm.deleteRooms(tid, loc2) ;
			rm.deleteRooms(tid, loc3) ;
			rm.commit(tid) ;
			long executionTime = System.nanoTime() - startTime;
			return executionTime/1000 ; //return value in microseconds
		} catch ( Exception e) {
			System.err.println("Error during variousTransation execution" + e ) ; 
			e.printStackTrace() ;
			return 0 ;
		}
	}


	private static long readTransaction() {
		try {
			long startTime = System.nanoTime() ;
			int tid = rm.start() ;
			System.out.println(" GOT ID " + tid ) ;
			Thread.sleep(5);
			String loc1 = "ROOM" + tid ;
			rm.addRooms(tid,loc1 , tid, tid) ;
			rm.addCars(tid, "CAR"+tid, tid,  tid) ;
			rm.addFlight(tid, tid, tid, tid) ;
			rm.queryRooms(tid, loc1);
			rm.queryRooms(tid, loc1);
			rm.queryRooms(tid, loc1);
			rm.queryRooms(tid, loc1);
			rm.queryRooms(tid, loc1);
			rm.queryRooms(tid, loc1);
			rm.queryRooms(tid, loc1);
			rm.commit(tid) ;
			long executionTime = System.nanoTime() - startTime;
			return executionTime/1000 ; //return value in microseconds
		} catch ( Exception e) {
			System.err.println("Error during variousTransation execution" + e ) ; 
			e.printStackTrace() ;
			return 0 ;
		}
	}

	private static long writeTransaction() {
		try {
			long startTime = System.nanoTime() ;
			int tid = rm.start() ;
			int tidd = tid ; 
			rm.addRooms(tid,"ROOM" + tidd++ , tid, tid) ;
			rm.addRooms(tid,"ROOM" + tidd++ , tid, tid) ;
			rm.addRooms(tid,"ROOM" + tidd++ , tid, tid) ;
			rm.addRooms(tid,"ROOM" + tidd++ , tid, tid) ;
			rm.addRooms(tid,"ROOM" + tidd++ , tid, tid) ;
			rm.addRooms(tid,"ROOM" + tidd++ , tid, tid) ;
			rm.addRooms(tid,"ROOM" + tidd++ , tid, tid) ;
			rm.addRooms(tid,"ROOM" + tidd++ , tid, tid) ;
			rm.commit(tid) ;
			long executionTime = System.nanoTime() - startTime;
			return executionTime/1000 ; //return value in microseconds
		} catch ( Exception e) {
			System.err.println("Error during variousTransation execution" + e ) ; 
			e.printStackTrace() ;
			return 0 ;
		}
	}
}

