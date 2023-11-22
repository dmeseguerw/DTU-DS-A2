package services;

import authentication.Authentication;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;

public class Sessions extends Authentication {
    private static final long SESSION_TIMEOUT = 10 * 60 * 1000;
    private final HashMap<String, String> usersTokens = new HashMap<>();
    private final HashMap<String, Long> tokensExpiration = new HashMap<>();
    Map<String, String> userRoleMap = new HashMap<>();
    Map<String, ArrayList<String>> rolePermissions = new HashMap<>();

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
        try (FileWriter fileWriter = new FileWriter("resources/LOGS/" + username + "_LOG.txt", true)) {
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
        System.out.println("\nUser: " + username + ", Role: " + role);
        // Now we need to check if the role can perform the given operation.
        readRolePermissions();
        System.out.println("Role: " + role + ", Permissions: " + rolePermissions.get(role));
        if (rolePermissions.get(role).contains(operation)) {
            System.out.println("Role able to perform operation: " + operation);
            return true;
        } else {
            System.out.println("FAIL: Role not able to perform operation -> " + operation);
            return false;
        }
    }

    public void readUserRolesFile() {
        userRoleMap.clear();
        String roles_file = "resources/user_roles.txt";
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
    }

    public void readRolePermissions() {
        rolePermissions.clear();
        String permissions_file = "resources/role_permissions.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(permissions_file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String role = parts[0].trim();
                    String[] permissionsArray = parts[1].split(",");
                    ArrayList<String> permissions = new ArrayList<>();

                    for (String operation : permissionsArray) {
                        permissions.add(operation.trim());
                    }

                    rolePermissions.put(role, permissions);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editUserRoles(String user_id, String new_role) {
        readUserRolesFile();

        if (Objects.equals(new_role, "delete")) {
            userRoleMap.remove(user_id);
        } else {
            userRoleMap.put(user_id, new_role);
        }
        String roles_file = "resources/user_roles.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(roles_file))) {
            for (Map.Entry<String, String> entry : userRoleMap.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void editRolePermissions(String role_id, String new_permission) {
        readRolePermissions();

        ArrayList<String> permissions_for_role = rolePermissions.get(role_id);

        if (permissions_for_role == null) {
            permissions_for_role = new ArrayList<>();
        }

        if (permissions_for_role.contains(new_permission)) {
            permissions_for_role.remove(new_permission);
        } else {
            permissions_for_role.add(new_permission);
        }

        rolePermissions.put(role_id, permissions_for_role);

        String permissions_file = "resources/role_permissions.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(permissions_file))) {
            for (Map.Entry<String, ArrayList<String>> entry : rolePermissions.entrySet()) {
                writer.write(entry.getKey() + ":" + String.join(",", entry.getValue()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Role " + role_id + " permissions modified to: " + rolePermissions.get(role_id));

    }
}