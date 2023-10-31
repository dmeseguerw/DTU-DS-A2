package services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

public class Sessions {
    private static HashMap<String,String> usersTokens = new HashMap<>();

    public String generateToken(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }

    public void addNewUserSession(String token, String username){
        System.out.println("New token added");
        usersTokens.put(token, username);
    }

    public String getUsername(String token){
        return usersTokens.get(token);
    }

    public void removeSessionToken(String token){
        usersTokens.remove(token);
    }

    public static boolean verifyActiveSession(String token){
        return (usersTokens.get(token) != null);
    }
}
