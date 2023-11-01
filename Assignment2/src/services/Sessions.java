package services;

import authentication.Authentication;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

public class Sessions extends Authentication {
    private final HashMap<String,String> usersTokens = new HashMap<>();
    private final HashMap<String, Long> tokensExpiration = new HashMap<>();
    private static final long SESSION_TIMEOUT = 10*60*1000;
    public String generateToken(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);


    }

    public void addNewUserSession(String token, String username){
        System.out.println("New token added");
        usersTokens.put(token, username);

        long expTime = System.currentTimeMillis() + SESSION_TIMEOUT;
        tokensExpiration.put(token,expTime);
    }

    public String getUsername(String token){
        return usersTokens.get(token);
    }

    public void removeSessionToken(String token){
        System.out.println("Session for user " + usersTokens.get(token) + " removed");
        usersTokens.remove(token);
        tokensExpiration.remove(token);
    }

    public boolean verifyActiveSession(String token){
        if(usersTokens.get(token) != null){
            if(tokensExpiration.get(token) != null & tokensExpiration.get(token) < System.currentTimeMillis()) {
                return true; // Token is valid and not expired
            }
            // Token is valid but expired
            removeSessionToken(token);
            return false;
        }
        return false; // Token is not valid
    }
}
