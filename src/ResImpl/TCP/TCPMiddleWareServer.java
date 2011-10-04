package ResImpl.TCP;

import ResInterface.IResourceManager;

public class TCPMiddleWareServer extends AbstractTCPResourceManager {

	private String carRMHost = "localhost";
	private String flightRMHost = "localhost";
	private String roomRMHost = "localhost";
	
	@Override
	public String processInput(String line) {
		System.out.println("MiddlewareServer got message: "+ line);
		return new Boolean(true).toString();
	}
	
	public static void main(String[] args) {
		final int port = Integer.parseInt(args[0]);
		new Thread(new Runnable(){
			public void run(){
				(new TCPMiddleWareServer()).listen(port);
			}
		}).start();
		
	}

}
