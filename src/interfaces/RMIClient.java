package src.interfaces;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface RMIClient {
    RMIServer getServerStub() throws RemoteException, NotBoundException;
    void onJoin () throws RemoteException;
}
