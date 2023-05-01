package User;


import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Users {

    public List<User> mUsers = new ArrayList<User>();

    private static Users instance;

    private Users() {
    }

    public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
        }
        return instance;
    }

    public User getUserByUsername(String pUsernameAttempt){
        for(User u: mUsers){
            if(u.getUsername().equals(pUsernameAttempt)){
                return u;
            }
        }
        // Returning empty object if no user is found
        return null;
    }

    public JSONObject toJSONObject() {
        JSONObject fullDataObject = new JSONObject();
        JSONArray usersData = new JSONArray();
        for(User u: mUsers){
            JSONObject userObject = new JSONObject();
            userObject.put("Username", u.getUsername());
            userObject.put("Password", u.getPassword());
            userObject.put("Salt", u.getSalt());
            
            JSONArray accountsData = new JSONArray();
            for(Account a: u.getAccounts()){
                JSONObject accountObject = new JSONObject();
                accountObject.put("AccountName", a.getName());
                accountObject.put("AccountNumber", a.getAccountNumber());

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
            }
            userObject.put("Accounts", accountsData)
            usersData.add(userObject);
        }
        fullDataObject.put("Users", usersData);
        return fullDataObject;
    }
}
