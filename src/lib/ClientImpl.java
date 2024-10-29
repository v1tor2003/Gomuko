package src.lib;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import src.interfaces.RMIClient;
import src.interfaces.RMIServer;

public class ClientImpl implements RMIClient, Serializable{
    private String serverName;

    public ClientImpl(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public RMIServer getServerStub() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        return (RMIServer) registry.lookup(this.serverName);
    }

    @Override
    public void onJoin() throws RemoteException {
       System.out.println("onjoin ran");
        synchronized (this) {
           this.notify();
       }
    }
}
