package Transactions;

public class Operation {

	private Object key;
	private Object value;
	private int type;
	
	public static final int WRITE = 1;
	public static final int ADD = 2;
	public static final int DELETE = 3;
	
	public Operation(int type, Object key, Object value){
		this.type = type;
		this.key = key;
		this.value = value;
	}

	public Object getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}
	
	public int getType(){
		return type;
	}
	
}
