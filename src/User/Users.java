package User;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Users {

    private List<User> mUsers = new ArrayList<User>();
    private User mCurrentUser;

    private static Users instance;

    private Users() {
    }

    public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
        }
        return instance;
    }

    public boolean login(String pUsernameAttempt, String pPasswordAttempt){
        boolean successful = false;
        for(User u: mUsers){
            if(!u.getUsername().equals(pUsernameAttempt)){
                continue;
            }
            if(u.isPassword(pPasswordAttempt)){
                mCurrentUser = u;
                successful = true;
                break;
            }
        }
        return successful; 
    }

    public boolean isUsernameTaken(String pUsernameAttempt){
        System.out.println(mUsers.size());
        for(User u: mUsers){
            if(u != null && u.getUsername().equals(pUsernameAttempt)){
                return true;
            }
        }
        return false;
    }

    private void setCurrentUser(User pCurrentUser){
        this.mCurrentUser = pCurrentUser;
    }

    public User getCurrentUser(){
        return this.mCurrentUser;
    }

    public void addUser(User pUser){
        mUsers.add(pUser);
    }

    private void combineCurrentUser(){
        mUsers.add(mCurrentUser);
        HashSet<User> setUsers = new HashSet<>(mUsers);
        mUsers = new ArrayList<>(setUsers);
    }

    // Making json object order is Users -> user -> accounts -> account (-> transactions -> transaction) AND (-> budgets -> budget)
    public JSONObject toJSONObject() {
        combineCurrentUser();
        JSONObject fullDataObject = new JSONObject();
        JSONArray usersData = new JSONArray();
        
        mUsers.removeAll(Collections.singleton(null));

        for(User u: mUsers){
            JSONObject userObject = new JSONObject();
            userObject.put("Username", u.getUsername());
            userObject.put("Password", u.getPassword());
            userObject.put("Salt", u.getSalt());
            
            JSONArray accountsData = new JSONArray();
            for(Account a: u.getAccounts()){
                JSONObject accountObject = new JSONObject();
                accountObject.put("AccountName", a.getAccountName());
                accountObject.put("AccountNumber", a.getAccountNumber());
                accountObject.put("LastAccessed", a.getLastAccessed().toString());

                JSONArray transactionData = new JSONArray();
                for (Transaction t : a.getTransactions()) {
                    JSONObject transactionObj = new JSONObject();
                    transactionObj.put("Date", t.getDateAsString());
                    transactionObj.put("Payee", t.getPayee());
                    transactionObj.put("Category", t.getCategory());
                    transactionObj.put("Note", t.getNote());
                    transactionObj.put("Amount", t.getAmount());
                    transactionData.add(transactionObj);
                }
                accountObject.put("Transactions", transactionData);

                
                JSONArray budgetData = new JSONArray();
                for (Budget tBudget : a.getBudgets()) {
                    JSONObject budgetObj = new JSONObject();
                    budgetObj.put("Category", tBudget.getCategory());
                    budgetObj.put("Budgeted", tBudget.getBudgeted());
                    budgetObj.put("Spent", tBudget.getSpent());
                    budgetData.add(budgetObj);
                }
                accountObject.put("Budgets", budgetData);
                accountsData.add(accountObject);
            }
            userObject.put("Accounts", accountsData);
            usersData.add(userObject);
        }
        fullDataObject.put("Users", usersData);
        return fullDataObject;
    }

    public void fromJSONObject(JSONObject jsonObject){
        try{
            JSONParser parser = new JSONParser();

            JSONObject usersObject = (JSONObject) parser.parse(jsonObject.toJSONString());
            JSONArray usersData = (JSONArray) usersObject.get("Users");

            for(int i = 0; i < usersData.size(); i++){
                JSONObject userObject = (JSONObject) usersData.get(i);
                User tempUser = new User();

                tempUser.setUsername(userObject.get("Username").toString());
                tempUser.setPassword(userObject.get("Password").toString());
                tempUser.setSalt(userObject.get("Salt").toString());

                // Now to get accounts
                JSONArray accountsData = (JSONArray) userObject.get("Accounts");
                for(int j = 0; j < accountsData.size(); j++){
                    JSONObject accountObject = (JSONObject) accountsData.get(j);
                    Account tempAccount = new Account();

                    tempAccount.setAccountName(accountObject.get("AccountName").toString());
                    tempAccount.setAccountNumber(((Long) accountObject.get("AccountNumber")).intValue());
                    LocalDateTime tDateTime = LocalDateTime.parse(accountObject.get("LastAccessed").toString());
                    tempAccount.setLastAccessed(tDateTime);

                    // Now to get transactions
                    JSONArray transactionsData = (JSONArray) accountObject.get("Transactions");
                    for(int k = 0; k < transactionsData.size(); k++){
                        JSONObject transactionObject = (JSONObject) transactionsData.get(k);

                        String tDate = (String) transactionObject.get("Date");
                        String tPayee = (String) transactionObject.get("Payee");
                        String tCategory = (String) transactionObject.get("Category");
                        String tNote = (String) transactionObject.get("Note");
                        double tAmount = (Double) transactionObject.get("Amount");

                        Transaction tempTransaction = new Transaction(tDate, tPayee, tCategory, tNote, tAmount);

                        tempAccount.addTransaction(tempTransaction);
                    }

                    // Now to get budgets
                    JSONArray budgetsData = (JSONArray) accountObject.get("Budgets");
                    for(int k = 0; k < budgetsData.size(); k++){
                        JSONObject budgetObject = (JSONObject) budgetsData.get(k);

                        String tCategory = (String) budgetObject.get("Category");
                        double tBudgeted = (Double) budgetObject.get("Budgeted");
                        double tSpent = (Double) budgetObject.get("Spent");

                        Budget tempBudget = new Budget(tCategory, tBudgeted, tSpent);

                        tempAccount.addBudget(tempBudget);
                    }

                    tempUser.addAccount(tempAccount);
                }

                addUser(tempUser);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
