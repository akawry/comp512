package Transactions;

@SuppressWarnings("serial")
public class TransactionAbortedException extends TransactionException {

	public TransactionAbortedException(String msg){
		super(msg);
	}
}
