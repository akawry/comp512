package Transactions;

import ResImpl.RMItem;

public class Operation {

	private String key;
	private Object value;
	private int type;
	
	public static final int WRITE = 1;
	public static final int ADD = 2;
	public static final int DELETE = 3;
	public static final int UNRESERVE = 4;
	
	public Operation(int type, String key, Object value){
		this.type = type;
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}
	
	public int getType(){
		return type;
	}
	
}
