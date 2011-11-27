package FaultTolerance;

public class Suspect {

	public final static int CAR = 1;
	public final static int FLIGHT = 2;
	public final static int ROOM = 3;
	public final static int MIDDLEWARE = 4;
	
	private String host;
	private int port;
	private int type;
	
	public Suspect(String host, int port, int type){
		this.host = host;
		this.port = port;
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getType() {
		return type;
	}
	
}
