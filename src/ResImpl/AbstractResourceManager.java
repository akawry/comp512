package ResImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import ResInterface.ICustomerResourceManager;
import ResInterface.IResourceManager;

public abstract class AbstractResourceManager {

    protected RMHashtable m_itemHT = new RMHashtable();
    protected int port ;
    protected Registry registry ;
    
    // Reads a data item
    protected RMItem readData( int id, String key )
    {
	synchronized(m_itemHT){
	    return (RMItem) m_itemHT.get(key);
	}
    }

    // Writes a data item
    protected void writeData( int id, String key, RMItem value )
    {
	synchronized(m_itemHT){
	    m_itemHT.put(key, value);
	}
    }
	
    // Remove the item out of storage
    protected RMItem removeData(int id, String key){
	synchronized(m_itemHT){
	    return (RMItem)m_itemHT.remove(key);
	}
    }
	
	
    // deletes the entire item
    protected boolean deleteItem(int id, String key)
    {
	Trace.info("RM::deleteItem(" + id + ", " + key + ") called" );
	ReservableItem curObj = (ReservableItem) readData( id, key );
	// Check if there is such an item in the storage
	if( curObj == null ) {
	    Trace.warn("RM::deleteItem(" + id + ", " + key + ") failed--item doesn't exist" );
	    return false;
	} else {
	    if(curObj.getReserved()==0){
		removeData(id, curObj.getKey());
		Trace.info("RM::deleteItem(" + id + ", " + key + ") item deleted" );
		return true;
	    }
	    else{
		Trace.info("RM::deleteItem(" + id + ", " + key + ") item can't be deleted because some customers reserved it" );
		return false;
	    }
	} // if
    }
	

    // query the number of available seats/rooms/cars
    protected int queryNum(int id, String key) {
	Trace.info("RM::queryNum(" + id + ", " + key + ") called" );
	ReservableItem curObj = (ReservableItem) readData( id, key);
	int value = 0;  
	if( curObj != null ) {
	    value = curObj.getCount();
	} // else
	Trace.info("RM::queryNum(" + id + ", " + key + ") returns count=" + value);
	return value;
    }	
	
    // query the price of an item
    protected int queryPrice(int id, String key){
	Trace.info("RM::queryPrice(" + id + ", " + key + ") called" );
	ReservableItem curObj = (ReservableItem) readData( id, key);
	int value = 0; 
	if( curObj != null ) {
	    value = curObj.getPrice();
	} // else
	Trace.info("RM::queryPrice(" + id + ", " + key + ") returns cost=$" + value );
	return value;		
    }
	
	
    // reserve an item
    protected boolean reserveItem(int id, int customerID, String key, String location){
	Trace.info("RM::reserveItem( " + id + ", customer=" + customerID + ", " +key+ ", "+location+" ) called" );		
	// Read customer object if it exists (and read lock it)
	Customer cust = (Customer) readData( id, Customer.getKey(customerID) );		
	if( cust == null ) {
	    Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", " + key + ", "+location+")  failed--customer doesn't exist" );
	    return false;
	} 
		
	// check if the item is available
	ReservableItem item = (ReservableItem)readData(id, key);
	if(item==null){
	    Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", " + key+", " +location+") failed--item doesn't exist" );
	    return false;
	}else if(item.getCount()==0){
	    Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", " + key+", " + location+") failed--No more items" );
	    return false;
	}else{			
	    cust.reserve( key, location, item.getPrice());		
	    writeData( id, cust.getKey(), cust );
			
	    // decrease the number of available items in the storage
	    item.setCount(item.getCount() - 1);
	    item.setReserved(item.getReserved()+1);
			
	    Trace.info("RM::reserveItem( " + id + ", " + customerID + ", " + key + ", " +location+") succeeded" );
	    return true;
	}		
    }

    // reserve an item
    protected boolean reserveItem(int id, int customerID, ReservableItem item, String location){
		
	// check if the item is available
	if(item==null){
	    Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", " + item +", " +location+") failed--item doesn't exist" );
	    return false;
	}else {
	    String key = item.getKey();
	    if(item.getCount()==0){
		Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", " + key+", " + location+") failed--No more items" );
		return false;
	    }else{	
				
		Trace.info("RM::reserveItem( " + id + ", customer=" + customerID + ", " +key+ ", "+location+" ) called" );		
		// Read customer object if it exists (and read lock it)
		Customer cust = (Customer) readData( id, Customer.getKey(customerID) );		
		if( cust == null ) {
		    Trace.warn("RM::reserveItem( " + id + ", " + customerID + ", " + key + ", "+location+")  failed--customer doesn't exist" );
		    return false;
		} 
				
		cust.reserve( key, location, item.getPrice());		
		writeData( id, cust.getKey(), cust );
				
		// decrease the number of available items in the storage
		item.setCount(item.getCount() - 1);
		item.setReserved(item.getReserved()+1);
				
		Trace.info("RM::reserveItem( " + id + ", " + customerID + ", " + key + ", " +location+") succeeded" );
		return true;
	    }

	}
					
    }

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

    protected void launch(){
	try  {
	    registry = LocateRegistry.getRegistry("localhost", port) ;
	    register();
	    System.err.println("[OK] Server "  + this.toString() + " ready on port " + port);
	}  catch (Exception e) {
	    System.err.println("[ERROR] Server "  + this.toString() + " on port " + port);
	    e.printStackTrace();
	    System.exit(1);
	}

    }
	
}

