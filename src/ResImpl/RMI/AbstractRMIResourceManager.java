package ResImpl.RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public abstract class AbstractRMIResourceManager {

	protected int port;
	protected Registry registry;
	protected int state;
	
	protected abstract String usage();

	protected abstract void register() throws Exception;

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
			register();
			System.err.println("[OK] Server " + this.toString() + " ready on port " + port);
		} catch (Exception e) {
			System.err.println("[ERROR] Server " + this.toString() + " on port " + port);
			e.printStackTrace();
			System.exit(1);
		}

	}
	
	public void crash() throws RemoteException {
		System.exit(1);
	}

	public int ping() throws RemoteException {
		return state;
	}

}
