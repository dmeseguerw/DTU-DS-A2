package pwdgenerator;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
public class PasswordGenerator {
    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }
    private static String get_SHA_256_SecurePassword(String passwordToHash,
                                                     String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static void generatePWDs() throws IOException, NoSuchAlgorithmException {
        File f = new File(pwd_file);
        BufferedReader br = new BufferedReader((new FileReader(f)));
        FileWriter fw = new FileWriter("hashedPWDs.txt");
        String line = null;
        String salt = getSalt();

        // Populate hash map
        while ((line = br.readLine()) != null){
            String[] parts = line.split(":");

            String curr_username = parts[0].trim();
            String curr_password = parts[1].trim();

            String hashed_pwd = get_SHA_256_SecurePassword(curr_password, salt);
            fw.write(salt + ":" + hashed_pwd + "\n");

        }
        br.close();
        fw.close();
    }
    private static final String pwd_file = "user_passwords_file.txt";
}
