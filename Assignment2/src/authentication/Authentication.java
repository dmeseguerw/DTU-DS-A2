package authentication;

import services.Sessions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class Authentication {

    public static Map<String, String> getPwdHashMap() throws RemoteException, FileNotFoundException {
        // Create hash
        Map<String, String> map = new HashMap<String, String>();

        try {
            // Read file with hash map
            File f = new File(pwd_file);
            BufferedReader br = new BufferedReader((new FileReader(f)));
            String line = null;

            // Populate hash map
            while ((line = br.readLine()) != null){
                String[] parts = line.split(":");

                String curr_username = parts[0].trim();
                String curr_password = parts[1].trim();

                if (!curr_username.isEmpty() && !curr_password.isEmpty())
                    map.put(curr_username, curr_password);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    public static boolean authUser(String username, String password) throws RemoteException, FileNotFoundException {
        Map<String, String> pwd_hash = getPwdHashMap();

        if(!pwd_hash.containsKey(username)){
            System.out.println("User not found");
            return false;
        }

        String user_password = pwd_hash.get(username);

        try {
            if (password.equals(user_password)) {
                System.out.println("User authenticated correctly");
                return true;
            }
            else{
                System.out.println("User authentication failed");
                return false;
            }
        }
        catch (Exception e) {
            System.out.println("Error in user authentication");
            e.printStackTrace();
            return false;
        }

    }

    public static boolean authSession(String token){
        if(Sessions.verifyActiveSession(token)) System.out.println("Authorized session");
        else System.out.println("Unauthorized session");
        return Sessions.verifyActiveSession(token);
    }


    private static final String pwd_file = "user_passwords_file.txt";

}
