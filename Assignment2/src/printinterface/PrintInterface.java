package printinterface;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

// Creating Remote interface for our application
public interface PrintInterface extends Remote {
    String login(String username, String password) throws RemoteException, FileNotFoundException;

    String logout(String token) throws RemoteException;

    String print(String filename, String printer, String token) throws RemoteException;

    String queue(String printer, String token) throws RemoteException;

    String topQueue(String printer, int job, String token) throws RemoteException;

    String start(String token) throws RemoteException;

    String stop(String token) throws RemoteException;

    String restart(String token) throws RemoteException;

    String status(String printer, String token) throws RemoteException;

    String readConfig(String parameter, String token) throws RemoteException;

    String setConfig(String parameter, String value, String token) throws RemoteException;

    String editUserMethods(String username, String methodToAdd, String token) throws RemoteException;

    ArrayList<String> getUserMissingMethods(String username, String token) throws RemoteException;


}