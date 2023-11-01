package pwdgenerator;

import BCrypt.BCrypt;

import java.io.*;

public class PasswordGenerator {
    private static final String pwd_file = "user_passwords_file.txt";

    public static void hashPasswords() throws IOException {
        File f = new File(pwd_file);
        BufferedReader br = new BufferedReader((new FileReader(f)));
        FileWriter hash_f = new FileWriter("hashed_PWDs.txt",true);
        String line = null;

        // Populate hash map
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(":");
            String curr_username = parts[0].trim();
            String curr_password = BCrypt.hashpw(parts[1].trim(),BCrypt.gensalt());

            hash_f.write(curr_username + ":" + curr_password+"\n");
        }
        br.close();
        hash_f.close();
    }
}
