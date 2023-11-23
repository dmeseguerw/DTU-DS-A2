package services;

import authentication.Authentication;
import server.PrintServer;

import java.io.*;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.*;

public class Sessions extends Authentication {
    private static final long SESSION_TIMEOUT = 10 * 60 * 1000;
    private final HashMap<String, String> usersTokens = new HashMap<>();
    private final HashMap<String, Long> tokensExpiration = new HashMap<>();
    Map<String, ArrayList<String>> userMethodsPermissions = new HashMap<>();

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
        try (FileWriter fileWriter = new FileWriter("resources/" + username + "_LOG.txt", true)) {
            fileWriter.write(operation + " operation performed - Time: " + System.currentTimeMillis() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public boolean checkMethod(String token, String methodName)
    {
        readPermissionFile();
        ArrayList<String> retrievedUserMethods = userMethodsPermissions.get(getUsername(token));
        boolean authorization = false;
        if (retrievedUserMethods == null)
        {
            System.out.println("No permissions for the user");
            return false;
        }
        else
        {
            for (String retrievedMethodName:retrievedUserMethods)
            {
                if (retrievedMethodName.contains(methodName))
                {
                    authorization = true;
                    break;
                }
            }
            return authorization;
        }
    }

    private void readPermissionFile()
    {
        userMethodsPermissions.clear();
        String permissions_file = "resources/userMethods_permissions.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(permissions_file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String[] permissionsArray = parts[1].split(",");
                    ArrayList<String> permissions = new ArrayList<>();

                    for (String operation : permissionsArray) {
                        permissions.add(operation.trim());
                    }

                    userMethodsPermissions.put(username, permissions);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editUserMethods(String username, String methodToAdd)
    {
        ArrayList<String> userPermissions = userMethodsPermissions.get(username);
        if (userPermissions == null)
        {
            userPermissions = new ArrayList<>();
            userPermissions.add(methodToAdd);
            userMethodsPermissions.put(username,userPermissions);
        }
        else
        {
            boolean present = false;
            for (String methodInUserPermissions:userPermissions)
            {
                if (methodInUserPermissions.contains(methodToAdd))
                {
                    present=true;
                    break;
                }
            }
            if (!present)
            {
                userPermissions.add(methodToAdd);
                userMethodsPermissions.put(username,userPermissions);
            }
            else
                System.out.println("Permission already exists.");

        }

        String permissions_file = "resources/userMethods_permissions.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(permissions_file))) {
            for (Map.Entry<String, ArrayList<String>> entry : userMethodsPermissions.entrySet()) {
                writer.write(entry.getKey() + ":" + String.join(",", entry.getValue()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("User " + username + " has access to a new method: " + methodToAdd);
    }
    public ArrayList<String> getUserMissingMethods(String username)
    {
        readPermissionFile();
        ArrayList<String> currentUserMethods = userMethodsPermissions.get(username);
        PrintServer printServer = new PrintServer();
        Method[] printServerMethods = printServer.getClass().getDeclaredMethods();
        ArrayList<String> missingUserMethods = new ArrayList<>();
        if (currentUserMethods ==null)
        {
            for (Method method:printServerMethods)
            {
                missingUserMethods.add(method.getName());
            }
        }
        else
        {
            for (Method method:printServerMethods)
            {
                boolean present = false;
                for (String methodName:currentUserMethods)
                {
                    if (method.getName().contains(methodName))
                    {
                        present=true;
                        break;
                    }
                }
                if (!present)
                {
                    missingUserMethods.add(method.getName());
                }
            }
        }
        return missingUserMethods;
    }
}