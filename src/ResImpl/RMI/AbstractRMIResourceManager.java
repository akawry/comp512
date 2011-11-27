package ResImpl.RMI;

import java.net.InetAddress;
import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import ResImpl.Trace;

public abstract class AbstractRMIResourceManager {

	protected String host;
	protected int port;
	protected Registry registry;
	protected int state;
	
	protected abstract String usage();

	protected abstract void register() throws Exception;

	protected abstract void unregister() throws Exception;
	
	protected void parseArgs(String[] args) {
		// If you don't expect one arg wich is the port, you should override
		// Get correct port from args
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
		} else if (args.length > 1) {
			System.out.println(usage());
			System.exit(1);
		}
	}

	protected void launch() {
		try {
			registry = LocateRegistry.getRegistry("localhost", port);
			host = InetAddress.getLocalHost().getHostName();
			register();
			System.err.println("[OK] Server " + this.toString() + " ready on port " + port);
		} catch (Exception e) {
			System.err.println("[ERROR] Server " + this.toString() + " on port " + port);
			e.printStackTrace();
			System.exit(1);
		}

	}
	
	public void crash() throws RemoteException {
		Trace.info(this+":: Goodbye ...");
		try {
			unregister();
		} catch (Exception e){
			Trace.error("[ERROR] "+this+":: Could not unregister: "+e.getMessage());
		}
		new Thread(){
			
			@Override
			public void run() {
				// wait a little while for the connection to close before shutting down ... 
				try {
					sleep(2000);
				} catch (InterruptedException e){
					
				}
				
				System.exit(1);
			}
		}.start();
	}

	public int ping() throws RemoteException {
		return state;
	}
	
	public String getHost(){
		return host;
	}
	
	public int getPort(){
		return port;
	}

}
