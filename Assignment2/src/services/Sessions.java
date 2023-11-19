package services;

import authentication.Authentication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

public class Sessions extends Authentication {

    private static final long SESSION_TIMEOUT = 10 * 60 * 1000;
    private final HashMap<String, String> usersTokens = new HashMap<>();
    private final HashMap<String, Long> tokensExpiration = new HashMap<>();
    Map<String, String> userRoleMap = new HashMap<>();
    Map<String, List<String>> roleHierarchy = new HashMap<>();

    public Sessions() {

        roleHierarchy.put("manager", List.of(new String[]{"manager", "technician", "poweruser", "user"}));
        roleHierarchy.put("poweruser", List.of(new String[]{"poweruser", "user"}));
        roleHierarchy.put("user", List.of(new String[]{"user"}));
        roleHierarchy.put("technician", List.of(new String[]{"technician"}));
        System.out.println(roleHierarchy);
    }

    public String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);


    }

    public void addNewUserSession(String token, String username) {
        System.out.println("New token added");
        usersTokens.put(token, username);
        long expTime = System.currentTimeMillis() + SESSION_TIMEOUT;
        tokensExpiration.put(token, expTime);
    }

    public String getUsername(String token) {
        return usersTokens.get(token);
    }

    public void removeSessionToken(String token) {
        System.out.println("Session for user " + usersTokens.get(token) + " removed");
        usersTokens.remove(token);
        tokensExpiration.remove(token);
    }

    public boolean verifyValidAccess(String token, String operation) {
        createLogFile(usersTokens.get(token), operation);
        if (usersTokens.get(token) != null) {
            if (tokensExpiration.get(token) != null & tokensExpiration.get(token) > System.currentTimeMillis()) {
                return true; // Token is valid and not expired
            }
            // Token is valid but expired
            removeSessionToken(token);
            return false;
        }
        return false; // Token is not valid
    }

    public void createLogFile(String username, String operation) {
        try (FileWriter fileWriter = new FileWriter("logs/" + username + "_log.txt", true)) {
            fileWriter.write("[" + operation + "]" + " operation performed - Time: " + System.currentTimeMillis() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void readRolesFile() {
        userRoleMap.clear();
        String roles_file = "src/user_roles.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(roles_file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String role = parts[1].trim();
                    userRoleMap.put(username, role);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Example: Print user-role mapping
        for (Map.Entry<String, String> entry : userRoleMap.entrySet()) {
            System.out.println("User: " + entry.getKey() + ", Role: " + entry.getValue());
        }
    }

    public boolean checkRole(String token, List<String> required_role) {
        readRolesFile();
        String username = getUsername(token);
        String role = userRoleMap.get(username);
        ArrayList<String> user_roles = new ArrayList<>(roleHierarchy.get(role));
        required_role = new ArrayList<>(required_role);
        return user_roles.retainAll(required_role);
    }
}
