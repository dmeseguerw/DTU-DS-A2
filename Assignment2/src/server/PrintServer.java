package server;

import printinterface.PrintInterface;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PrintServer extends PrintImp {
    public PrintServer() throws FileNotFoundException, RemoteException {}; // sets the parameter on the print server to value


    // Methods

    // ------------------------------


    public static void main(String args[]) {
        try {
            // Instantiating the implementation class
            PrintInterface obj = (PrintInterface) new PrintServer();

            // Exporting the object of implementation class
            // (here we are exporting the remote object to the stub)
            PrintInterface stub = (PrintInterface) UnicastRemoteObject.exportObject(obj, 0);

            // Binding the remote object (stub) in the registry
            Registry registry = LocateRegistry.getRegistry();

            registry.rebind("PrintInterface", stub);
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}