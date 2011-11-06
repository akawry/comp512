package Transactions;

import java.util.* ;

public class AliveTransactionTask extends TimerTask {

    private final Map<Integer,Long> transactions ;
    private static final int TRANSACTION_TIMEOUT_SECONDS = 10 ;

    public AliveTransactionTask(Map<Integer,Long> t) {
      super() ;
      transactions = t ;
    }

    public void run() {
		List<Integer> timeouted = new LinkedList<Integer>() ;
	
		for (Map.Entry<Integer, Long> e : transactions.entrySet()){
		    Calendar now = Calendar.getInstance() ;
		    long tnow = now.getTime().getTime() ;
		    long time = e.getValue() ;
		    long diff = tnow - time ;
		    if (  diff > ( TRANSACTION_TIMEOUT_SECONDS * 1000) ) {
			//System.out.println(" LAST PING : "+ time + " HEURE ACTUEL" + tnow + "DIFF =" + diff) ;
			timeouted.add(e.getKey() ) ;
			System.out.println("[ERROR] " + this + " : TransactionID " + e.getKey() + " timed out") ;
		    }
		}
		// We delete them
		for (int key : timeouted) {
		    transactions.remove(key) ;
		}

    }
}
