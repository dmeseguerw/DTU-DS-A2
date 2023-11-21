package server;

import printinterface.PrintInterface;
import services.Printer;
import services.Sessions;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class PrintServer implements PrintInterface {
    ArrayList<Printer> printers = new ArrayList<Printer>();
    Sessions session = new Sessions();

    public String login(String username, String password) throws FileNotFoundException, RemoteException {
        if (session.authUser(username, password)) {
            String sessionToken = session.generateToken();
            session.addNewUserSession(sessionToken, username);
            return sessionToken;
        }
        return null;
    }

    public String logout(String token) throws RemoteException {
        String ret_statement = "Not logged in";
        if (session.verifyValidAccess(token, "logout")) {
            session.removeSessionToken(token);
            ret_statement = "Logged out";
        }
        System.out.println(ret_statement);
        return ret_statement;
    }


    public String print(String filename, String printer, String token) {
        String ret_statement = "Operation failed";
        if (session.verifyValidAccess(token, "print") & session.checkRole(token, "print")) {
            ret_statement = "";
            for (Printer p : printers) {
                if (p.getPrinterName().equals(printer)) {
                    p.addQueue(filename);
                    ret_statement = "File " + filename + " added on printer " + printer;
                    System.out.println(ret_statement);
                    break;
                }
            }
        }
        System.out.println(ret_statement);
        return ret_statement;
    }

    // prints file filename on the specified printer

    public String queue(String printer, String token) {
        String queue = "Operation failed";
        if (session.verifyValidAccess(token, "queue") & session.checkRole(token, "queue")) {
            queue = "";
            for (Printer p : printers) {
                if (p.getPrinterName().equals(printer)) {
                    for (int i = 0; i < p.getQueue().size(); i++) {
                        queue += "<" + i + "> <" + p.getQueue().get(i) + ">\n";
                    }
                    break;
                }
            }
        }
        System.out.println(queue);
        return queue;
    }

    // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>

    public String topQueue(String printer, int job, String token) {
        String ret_statement = "Operation failed";
        if (session.verifyValidAccess(token, "topQueue") & session.checkRole(token, "topQueue")) {
            for (Printer p : printers) {
                if (p.getPrinterName().equals(printer)) {
                    p.moveTopQueue(job);
                    break;
                }
            }
            ret_statement = "Job " + job + " in printer " + printer + " moved to the top";
        }
        System.out.println(ret_statement);
        return ret_statement;
    }

    // moves job to the top of the queue

    public String start(String token) {
        String ret_statement = "Operation failed";
        if (session.verifyValidAccess(token, "start") & session.checkRole(token, "start")) {
            // Start database and initialize printers
            Printer p1 = new Printer("p1");
            Printer p2 = new Printer("p2");
            Printer p3 = new Printer("p3");
            Printer p4 = new Printer("p4");
            printers.add(p1);
            printers.add(p2);
            printers.add(p3);
            printers.add(p4);
            ret_statement = "services.Printer server started";
        }
        System.out.println(ret_statement);
        return ret_statement;
    }

    // starts the print server

    public String stop(String token) {
        String ret_statement = "Operation failed";
        if (session.verifyValidAccess(token, "stop") & session.checkRole(token, "stop")) {
            ret_statement = "Print server stopped";
        }
        System.out.println(ret_statement);
        return ret_statement;
    }

    // stops the print server

    public String restart(String token) {
        String ret_statement = "Operation failed";
//        System.out.println("stops the print server, clears the print queue and starts the print server again");
        if (session.verifyValidAccess(token, "restart") & session.checkRole(token, "restart")) {
            for (Printer p : printers) {
                p.clearQueue();
            }
            ret_statement = "Print server restarted";
        }
        System.out.println(ret_statement);
        return ret_statement;
    }

    // stops the print server, clears the print queue and starts the print server again

    public String status(String printer, String token) {
        String ret_statement = "Operation failed";
        System.out.println("prints status of printer on the user's display");
        if (session.verifyValidAccess(token, "status") & session.checkRole(token, "status")) {
            for (Printer p : printers) {
                if (p.getPrinterName().equals(printer)) {
                    ret_statement = p.getstatus();
                    break;
                }
            }
        }
        System.out.println(ret_statement);
        return ret_statement;
    }

    // prints status of printer on the user's display

    public String readConfig(String parameter, String token) {
        String ret_statement = "Operation failed";
        if (session.verifyValidAccess(token, "readConfig") & session.checkRole(token, "readConfig")) {
            ret_statement = "GET CONFIG FROM SERVER PRINTER";
        }
        System.out.println(ret_statement);
        return ret_statement;

    }

    // prints the value of the parameter on the print server to the user's display

    public String setConfig(String parameter, String value, String token) {
        String ret_statement = "Operation failed";
        if (session.verifyValidAccess(token, "setConfig") & session.checkRole(token, "setConfig")) {
            ret_statement = "GET CONFIG FROM SERVER PRINTER";
        }
        System.out.println(ret_statement);
        return ret_statement;
    }


    public String editUserRoles(String user_id, String new_role, String token) {
        String ret_statement = "Operation failed";
        if (session.verifyValidAccess(token, "editRoles") & session.checkRole(token, "editRoles")) {
            ret_statement = "EDIT USER ROLES";
            session.editUserRoles(user_id, new_role);
        }
        System.out.println(ret_statement);
        return ret_statement;
    }

    public String editRolePermissions(String role_id, String new_permission, String token) {
        String ret_statement = "Operation failed";
        if (session.verifyValidAccess(token, "editPermissions") & session.checkRole(token, "editPermissions")) {
            ret_statement = "EDIT ROLE PERMISSIONS";
            session.editRolePermissions(role_id, new_permission);
        }
        System.out.println(ret_statement);
        return ret_statement;
    }
}