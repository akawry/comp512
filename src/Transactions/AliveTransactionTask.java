package Transactions;

import java.rmi.RemoteException;
import java.util.* ;

import FaultTolerance.CrashException;

public class AliveTransactionTask extends TimerTask {

    private final Map<Integer,Long> transactions ;
    public static final int TRANSACTION_TIMEOUT_SECONDS = 30 ;
    private final ITransactionManager transactionManager;
    
    public AliveTransactionTask(Map<Integer,Long> t, ITransactionManager tm) {
      super() ;
      transactions = t ;
      transactionManager = tm;
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
		    try {
				transactionManager.abort(key);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidTransactionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CrashException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

    }
}
