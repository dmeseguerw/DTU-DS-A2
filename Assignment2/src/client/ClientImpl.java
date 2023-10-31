package client;

import printinterface.PrintInterface;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientImpl {
    private String sessionToken;
    public static String login(PrintInterface stub) throws FileNotFoundException, RemoteException {
        System.out.println("Enter username");
//        String username = String.valueOf(new Scanner(System.in));
        String username = (new Scanner(System.in)).nextLine();  // Read user input
        System.out.println("Enter password");
        String password = (new Scanner(System.in)).nextLine();  // Read user input
        String sessionToken = stub.login(username, password);
        if(sessionToken != null) System.out.println("Login successful. Welcome!");
        else System.out.println("Login failed. You suck!");
        return sessionToken;
    }

    public static void print(String filename, String printer, String token, PrintInterface stub) throws RemoteException {
        System.out.println(stub.print(filename, printer, token));
    }

    public static void queue(String printer, String token, PrintInterface stub) throws RemoteException {
        System.out.println(stub.queue(printer,token));
    }

    public static void topQueue(String printer, int job, String token, PrintInterface stub) throws RemoteException {
        System.out.println(stub.topQueue(printer, job, token));
    }
    public static void start(String token, PrintInterface stub) throws RemoteException {
        System.out.println(stub.start(token));
    }
    public static void stop(String token, PrintInterface stub) throws RemoteException {
        System.out.println(stub.stop(token));
    }
    public static void restart(String token, PrintInterface stub) throws RemoteException {
        System.out.println(stub.restart(token));
    }
    public static void status(String printer, String token, PrintInterface stub) throws RemoteException {
        System.out.println(stub.status(printer,token));
    }
    public static void readConfig(String parameter, String token, PrintInterface stub) throws RemoteException {
        System.out.println(stub.readConfig(parameter,token));
    }
    public static void setConfig(String parameter, String value, String token, PrintInterface stub) throws RemoteException {
        System.out.println(stub.setConfig(parameter, value, token));
    }
}
