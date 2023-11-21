package client;

import printinterface.PrintInterface;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class PrintClient {
    private String sessionToken;


    public void UserMenu(PrintInterface stub) throws FileNotFoundException, RemoteException {
        Scanner scanner = new Scanner(System.in);
        boolean run = true;
        System.out.println("Welcome to the printer server. Please login:");
        String sessionToken = login(stub);
        if (sessionToken == null) {
            run = false;
            while (!run) {
                System.out.println("Invalid credentials. Please login again:");
                sessionToken = login(stub);
                if (sessionToken != null) run = true;
            }
        }

        while (run) {
            System.out.println("\nWelcome to the printer server. What would you like to do?");
            System.out.println("1. Print filename");
            System.out.println("2. Print printer queue");
            System.out.println("3. Move job to top of printer queue");
            System.out.println("4. Start the printer server");
            System.out.println("5. Stop the printer server");
            System.out.println("6. Restart the printer server");
            System.out.println("7. Show status of printer");
            System.out.println("8. Read configuration of server");
            System.out.println("9. Set configuration of server");
            System.out.println("10. Logout");
            System.out.println("11. Exit");
            System.out.println("12. Login");
            System.out.println("13 Edit user roles");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    print(stub);
                    break;
                case 2:
                    queue(stub);
                    break;
                case 3:
                    topQueue(stub);
                    break;
                case 4:
                    start(stub);
                    break;
                case 5:
                    stop(stub);
                    break;
                case 6:
                    restart(stub);
                    break;
                case 7:
                    status(stub);
                    break;
                case 8:
                    readConfig(stub);
                    break;
                case 9:
                    setConfig(stub);
                    break;
                case 10:
                    logout(stub);
                    break;
                case 11:
                    System.out.println("Exiting the menu");
                    run = false;
                    break;
                case 12:
                    login(stub);
                    break;
                case 13:
                    editUserRoles(stub);
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
                    break;
            }
        }

        scanner.close();
    }

    public String login(PrintInterface stub) throws FileNotFoundException, RemoteException {
        System.out.println("Enter username");
//        String username = String.valueOf(new Scanner(System.in));
        String username = (new Scanner(System.in)).nextLine();  // Read user input
        System.out.println("Enter password");
        String password = (new Scanner(System.in)).nextLine();  // Read user input
        sessionToken = stub.login(username, password);
        if (sessionToken != null) System.out.println("Login successful. Welcome!");
        else System.out.println("Login failed. You suck!");
        return sessionToken;
    }

    public void logout(PrintInterface stub) throws RemoteException {
        System.out.println(stub.logout(sessionToken));
    }

    public void print(PrintInterface stub) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert name of file: ");
        String filename = scanner.next();
        System.out.println("Insert name of printer: ");
        String printer = scanner.next();
        System.out.println(stub.print(filename, printer, sessionToken));
    }

    public void queue(PrintInterface stub) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert name of printer: ");
        String printer = scanner.next();
        System.out.println(stub.queue(printer, sessionToken));
    }

    public void topQueue(PrintInterface stub) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert name of printer: ");
        String printer = scanner.next();
        System.out.println("Insert number of job: ");
        int job = scanner.nextInt();
        System.out.println(stub.topQueue(printer, job, sessionToken));
    }

    public void start(PrintInterface stub) throws RemoteException {
        System.out.println(stub.start(sessionToken));
    }

    public void stop(PrintInterface stub) throws RemoteException {
        System.out.println(stub.stop(sessionToken));
    }

    public void restart(PrintInterface stub) throws RemoteException {
        System.out.println(stub.restart(sessionToken));
    }

    public void status(PrintInterface stub) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert name of printer: ");
        String printer = scanner.next();
        System.out.println(stub.status(printer, sessionToken));
    }

    public void readConfig(PrintInterface stub) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert name of parameter: ");
        String parameter = scanner.next();
        System.out.println(stub.readConfig(parameter, sessionToken));
    }

    public void setConfig(PrintInterface stub) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert name of parameter: ");
        String parameter = scanner.next();
        System.out.println("Insert new value: ");
        String value = scanner.next();
        System.out.println(stub.setConfig(parameter, value, sessionToken));
    }

    public void editUserRoles(PrintInterface stub) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert name of user you want to change permissions: ");
        String user_id = scanner.next();
        System.out.println("Insert role name or type 'delete' to remove the user: ");
        String new_role = scanner.next();
        System.out.println(stub.editUserRoles(user_id, new_role, sessionToken));
    }
}
