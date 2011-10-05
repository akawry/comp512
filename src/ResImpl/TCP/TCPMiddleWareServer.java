package ResImpl.TCP;

import ResImpl.RMI.RMIMiddleWare;
import ResInterface.IResourceManager;

public class TCPMiddleWareServer extends AbstractTCPResourceManager {

	private String carRMHost = "localhost";
	private String flightRMHost = "localhost";
	private String roomRMHost = "localhost";
	private int carRMPort;
	private int flightRMPort;
	private int roomRMPort;
	private int port;
	private IResourceManager rm;
	
	public TCPMiddleWareServer(IResourceManager rm){
		this.rm = rm;
	}
	
	@Override
	public String processInput(String line) {
		String res = null;
		String type = line.split(",")[0];
		
		// forward request to car rm
		if (type.contains("car")){
			res = send(line, carRMHost, carRMPort);
	
		// forward request to flight rm 	
		} else if (type.contains("flight")){
			res = send(line, flightRMHost, flightRMPort);
			
		// forward request to room rm
		} else if (type.contains("room")){
			res = send(line, roomRMHost, roomRMPort);
		
		// handle locally 
		} else if (type.contains("customer")){
			
			
		}
		
		return res; 
	}
	
	private String usage(){
		return "";
	}
	
	public void parseArgs(String[] args){
		if (args.length != 4 && args.length != 3){
			System.err.println(usage());
			System.exit(1);
		}

		String[] valid = {"car", "flight", "room", "port"};
		String flag;


		for (String arg : args){

			for (String s : valid){
				flag = "-" + s + "=";
				if (flag.equals(arg.substring(0, flag.length())) && arg.length() > flag.length()){
					String argval = arg.split("=")[1];
					// if : setting the port where this middleware runs  
					// else : we need to parse the server name and server port	
					if (s.equals("port")){
						port = Integer.parseInt(argval);
					} else if (s.equals("car") ) {
						carRMHost = argval.split(":")[0] ;
						carRMPort = Integer.parseInt(argval.split(":")[1]) ;
					} else if (s.equals("room") ) {
						roomRMHost = argval.split(":")[0] ;
						roomRMPort = Integer.parseInt(argval.split(":")[1]) ;
					} else if (s.equals("flight") ) {
						flightRMHost = argval.split(":")[0] ;
						flightRMPort = Integer.parseInt(argval.split(":")[1]) ;
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		TCPMiddleWareServer mw = new TCPMiddleWareServer(new RMIMiddleWare());
		mw.parseArgs(args);
		mw.listen(mw.port);
	}

}
