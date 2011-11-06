package Client;

import java.rmi.*;

import ResImpl.TCP.TCPMiddleWareClient;
import ResInterface.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.*;
import java.io.*;


public class TCPClient extends AbstractClient
{

	public static void main(String args[])
	{
		String serverhost = "localhost";
		int serverport = 1099; 

		if (args.length != 1) {
			System.out.println ("Usage: java client [rmihost]"); 
			System.exit(1); 
		} else {
			serverhost = args[0].split(":")[0]; 
			serverport = Integer.parseInt( args[0].split(":")[1] ); 
			rm = new TCPMiddleWareClient(serverhost, serverport);
			manualInput();
			
		}

	}
}
