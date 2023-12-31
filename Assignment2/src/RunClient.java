import client.PrintClient;
import printinterface.PrintInterface;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunClient {
    public static void main(String[] args) throws IOException {
//        PasswordGenerator.hashPasswords(); //Generate hashed PWDs file
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(null);

            // Looking up the registry for the remote object
            PrintInterface stub = (PrintInterface) registry.lookup("PrintInterface");
            PrintClient clientManager = new PrintClient();
            clientManager.UserMenu(stub);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
