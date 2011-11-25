package FaultTolerance;

import java.rmi.ConnectException;

public class CrashException extends ConnectException{

	private Object rm;
	private boolean doRetry;
	
	public CrashException(String msg) {
		super(msg);
	}

	public CrashException(String msg, Object rm){
		super(msg);
		this.rm = rm;
	}
	
	public CrashException(String msg, Object rm, boolean doRetry){
		super(msg);
		this.rm = rm;
		this.doRetry = doRetry;
	}
	
	public Object getOffendingRM(){
		return rm;
	}
	
	public boolean shouldRetry(){
		return doRetry;
	}
	
}
