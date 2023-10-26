import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
public class PrintClient {
    private PrintClient() {}
    public static void main(String[] args) {
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(null);

            // Looking up the registry for the remote object
            PrintInterface stub = (PrintInterface) registry.lookup("PrintInterface");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Print file");
                System.out.println("2. Get queue");
                System.out.println("3. Move job to top of queue");
                System.out.println("4. Start the print server");
                System.out.println("5. Stop the print server");
                System.out.println("6. Restart the print server");
                System.out.println("7. Print status of printer in user's display");
                System.out.println("8. Print value of parameter on print server to the user's display");
                System.out.println("9. Set parameter on the print server to a value ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        stub.print("filename","printer");
                        choice = scanner.nextInt();
                        break;
                    case 2:
                        stub.queue("printer");
                        choice = scanner.nextInt();
                        break;
                    case 3:
                        stub.topQueue("printer",1);
                        choice = scanner.nextInt();
                        break;
                    case 4:
                        stub.start();
                        choice = scanner.nextInt();
                        break;
                    case 5:
                        stub.stop();
                        choice = scanner.nextInt();
                        break;
                    case 6:
                        stub.restart();
                        choice = scanner.nextInt();
                        break;
                    case 7:
                        stub.status("printer");
                        choice = scanner.nextInt();
                        break;
                    case 8:
                        stub.readConfig("parameter");
                        choice = scanner.nextInt();
                        break;
                    case 9:
                        stub.setConfig("parameter","value");
                        choice = scanner.nextInt();
                        break;
                    case 10:
                        System.exit(0);
                    default: System.out.println("Choose again");

                }
            }



            // Calling the remote method using the obtained object
//            stub.printMsg();
//            stub.print("filename","printer");
//            stub.queue("printer");
//            stub.topQueue("printer",1);
//            stub.start();
//            stub.stop();
//            stub.restart();
//            stub.status("printer");
//            stub.readConfig("parameter");
//            stub.setConfig("parameter","value");

            // System.out.println("Remote method invoked");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}