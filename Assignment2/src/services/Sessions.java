package services;

import authentication.Authentication;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;

public class Sessions extends Authentication {
    private final HashMap<String, String> usersTokens = new HashMap<>();
    private final HashMap<String, Long> tokensExpiration = new HashMap<>();
    private static final long SESSION_TIMEOUT = 10 * 60 * 1000;

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
        try (FileWriter fileWriter = new FileWriter("LOGS/" + username + "_LOG.txt", true)) {
            fileWriter.write(operation + " operation performed - Time: " + System.currentTimeMillis() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public boolean checkRole(String token, String operation) {
        // First we need to get the role from the username.
        readUserRolesFile();
        String username = getUsername(token);
        String role = userRoleMap.get(username);

        // Now we need to check if the role can perform the given operation.
        readRolePermissions();
        if (rolePermissions.get(role).contains(operation)) {
            System.out.println("Role able to perform operation");
            return true;
        } else {
            System.out.println("FAIL: Role not able to perform operation");
            return false;
        }
    }

    public void readUserRolesFile() {
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

    public void readRolePermissions() {
        rolePermissions.clear();
        String permissions_file = "src/role_permissions.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(permissions_file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String role = parts[0].trim();
                    String[] permissionsArray = parts[1].split(",");
                    ArrayList<String> permissions = new ArrayList<>();

                    for (String function : permissionsArray) {
                        permissions.add(function.trim());
                    }

                    rolePermissions.put(role, permissions);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, ArrayList<String>> entry : rolePermissions.entrySet()) {
            System.out.println("Role: " + entry.getKey() + ", Permissions: " + entry.getValue());
        }
    }


    public void editUserRoles(String user_id, String new_role) {
        readRolePermissions();

        if (Objects.equals(new_role, "delete")) {
            userRoleMap.remove(user_id);
        } else {
            userRoleMap.put(user_id, new_role);
        }
        String roles_file = "src/user_roles.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(roles_file))) {
            for (Map.Entry<String, String> entry : userRoleMap.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    Map<String, String> userRoleMap = new HashMap<>();
    Map<String, ArrayList<String>> rolePermissions = new HashMap<>();
}