package User;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User {
    private String mUsername, mSalt, mPassword;
    private Account mCurrentAccount;
    private List<Account> mAccounts = new ArrayList<Account>();

    public User(){}

    public void CreateUser(String pUsername, String pPassword) {
        this.mUsername = pUsername;
        this.mPassword = pPassword;
        hashPassword();
    }

    private void hashPassword(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        this.mSalt = Base64.getEncoder().encodeToString(salt);

        try {
            KeySpec spec = new PBEKeySpec(mPassword.toCharArray(), Base64.getDecoder().decode(mSalt), 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            this.mPassword = Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPassword(String pPasswordAttempt){
        boolean isPassword = false;
        try {
            KeySpec spec = new PBEKeySpec(pPasswordAttempt.toCharArray(), Base64.getDecoder().decode(mSalt), 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            String tHashedPasswordAttempt = Base64.getEncoder().encodeToString(hash);
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

    public void setUsername(String pUsername){
        this.mUsername = pUsername;
    }

    public void setPassword(String pPassword){
        this.mPassword = pPassword;
    }

    public void setSalt(String pSalt){
        this.mSalt = pSalt;
    }

    public Account getCurrentAccount(){
        return this.mCurrentAccount;
    }
    
    public void setCurrentAccount(Account pCurrentAccount){
        this.mCurrentAccount = pCurrentAccount;
    }

    public List<Account> getAccounts(){
        return mAccounts;
    }
}
