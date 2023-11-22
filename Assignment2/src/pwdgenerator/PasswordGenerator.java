package pwdgenerator;

import BCrypt.BCrypt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PasswordGenerator {
    private static final String pwd_file = "./resources/user_passwords.txt";

    public static void hashPasswords() throws IOException {
        BufferedReader br = new BufferedReader((new FileReader(pwd_file)));
        FileWriter hash_f = new FileWriter("./hashed_PWDs.txt", true);
        String line = null;

        // Populate hash map
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(":");
            String curr_username = parts[0].trim();
            String curr_password = BCrypt.hashpw(parts[1].trim(), BCrypt.gensalt());

            hash_f.write(curr_username + ":" + curr_password + "\n");
        }
        br.close();
        hash_f.close();
    }
}
