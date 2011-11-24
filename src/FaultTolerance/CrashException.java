package FaultTolerance;

@SuppressWarnings("serial")
public class CrashException extends Exception {

	public CrashException(){
		super();
	}
	
	public CrashException(String msg){
		super(msg);
	}
	
}
