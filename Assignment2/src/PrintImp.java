public class PrintImp implements PrintInterface {
    public void printMsg() {
        System.out.println("This is an example RMI program");
    }

    public void print(String filename, String printer) {
        System.out.println("prints file filename on the specified printer");
    }

    ;   // prints file filename on the specified printer

    public void queue(String printer) {
        System.out.println("lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>");
    }

    ;   // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>

    public void topQueue(String printer, int job) {
        System.out.println("moves job to the top of the queue");
    }

    ;   // moves job to the top of the queue

    public void start() {
        System.out.println("starts the print server");
    }

    ;   // starts the print server

    public void stop() {
        System.out.println("stops the print server");
    }

    ;   // stops the print server

    public void restart() {
        System.out.println("stops the print server, clears the print queue and starts the print server again");
    }

    ;   // stops the print server, clears the print queue and starts the print server again

    public void status(String printer) {
        System.out.println("prints status of printer on the user's display");
    }

    ;  // prints status of printer on the user's display

    public void readConfig(String parameter) {
        System.out.println("");
    }

    ;   // prints the value of the parameter on the print server to the user's display

    public void setConfig(String parameter, String value) {
        System.out.println("");
    }
}
