package ResInterface;

import java.rmi.RemoteException;

import ResImpl.Hotel;

public interface RoomBackend {
    public Hotel getRoom(int id, String location) throws RemoteException;
    
    public void updateRoom(int id, String location, Hotel room) throws RemoteException;
}
