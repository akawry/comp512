package Transactions;

@SuppressWarnings("serial")
public class InvalidTransactionException extends TransactionException{

	public InvalidTransactionException(String msg){
		super(msg);
	}
	
}
