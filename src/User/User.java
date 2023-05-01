package User;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User {
    private String mUsername, mSalt, mPassword;
    private List<Account> mAccounts = new ArrayList<Account>();

    private static User instance;

    private User() {
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public void CreateUser(String pUsername, String pPassword) {
        this.mUsername = pUsername;
        this.mPassword = pPassword;
        hashPassword();
    }

    private void hashPassword(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        this.mSalt = salt.toString();

        try {
            KeySpec spec = new PBEKeySpec(mPassword.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            mPassword = hash.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPassword(String pPasswordAttempt){
        boolean isPassword = false;
        try {
            KeySpec spec = new PBEKeySpec(pPasswordAttempt.toCharArray(), mSalt.getBytes(), 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            String tHashedPasswordAttempt = hash.toString();
            if(tHashedPasswordAttempt.equals(mPassword)){
                isPassword = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPassword;
    }

    public void addAccount(Account pAccount){
        mAccounts.add(pAccount);
    }

    public String getUsername(){
        if (mUsername == null) {
            return "";
        }
        return mUsername;
    }

    public String getSalt(){
        if (mSalt == null) {
            return "";
        }
        return mSalt;
    }

    public String getPassword(){
        if (mPassword == null) {
            return "";
        }
        return mPassword;
    }

    public List<Account> getAccounts(){
        return mAccounts;
    }
}
