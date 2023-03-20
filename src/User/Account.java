package User;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Helper.GeneralHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A class to represent the users account. Is singleton for now, if more accounts are allowed change it.
 */
public class Account {
    private String mName;
    private int mAccountNumber;
    private ObservableList<Transaction> mTransactions = FXCollections.observableArrayList();
    private ObservableList<Transaction> mTransactionsWorkingList = FXCollections.observableArrayList();
    private ObservableList<Budget> mBudgets = FXCollections.observableArrayList();
    
    private static Account single_instance = null;
  
    // Static method
    // Static method to create instance of Singleton class
    public static Account getInstance()
    {
        if (single_instance == null)
            single_instance = new Account();
  
        return single_instance;
    }

    private Account(){}

    /**
     * Primarily for testing. 
     */
    @Override
    public String toString() {
        StringBuilder tStringBuilderWorkingVar = new StringBuilder();
        String tSeparator = System.lineSeparator();
        tStringBuilderWorkingVar.append("Account Information");
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Name: ");
        tStringBuilderWorkingVar.append(this.mName);
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Number: ");
        tStringBuilderWorkingVar.append(this.mAccountNumber);
        tStringBuilderWorkingVar.append(tSeparator);

        if(this.mTransactions.size() > 0){
            tStringBuilderWorkingVar.append("Transactions: ");
            tStringBuilderWorkingVar.append(tSeparator);
            for(Transaction tTransaction: this.mTransactions){
                tStringBuilderWorkingVar.append(tTransaction.toString());
            }
        }   

        if(this.mBudgets.size() > 0){
            tStringBuilderWorkingVar.append("Budgets: ");
            tStringBuilderWorkingVar.append(tSeparator);
            for(Budget tBudget: this.mBudgets){
                tStringBuilderWorkingVar.append(tBudget.toString());
            }
        }

        return tStringBuilderWorkingVar.toString();
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String pName) {
        this.mName = pName;
    }

    public int getAccountNumber() {
        return this.mAccountNumber;
    }

    public void setAccountNumber(int pAccountNumber) {
        this.mAccountNumber = pAccountNumber;
    }
    
    public ObservableList<Transaction> getTransactions() {
        return this.mTransactions;
    }

    public void setTransactions(ObservableList<Transaction> pTransactions) {
        this.mTransactions = pTransactions;
        setTransactionsWorkingListFromTransactions();
    }
    
    public ObservableList<Transaction> getTransactionsWorkingList() {
        return this.mTransactionsWorkingList;
    }

    public void setTransactionsWorkingListByBudgetCode(String pBudgetCode) {
        if(pBudgetCode == null || "".equals(pBudgetCode)){
            setTransactionsWorkingListFromTransactions();
        }
        else{
            this.mTransactionsWorkingList.clear();
            for(Transaction t: this.mTransactions){
                if(t != null && t.getCategory().equals(GeneralHelper.cleanCategoryString(pBudgetCode))){
                    this.mTransactionsWorkingList.add(t);
                }
            }
        }
    }

    public void setTransactionsWorkingListFromTransactions() {
        this.mTransactionsWorkingList.clear();
        for(Transaction t: this.mTransactions){
            this.mTransactionsWorkingList.add(t);
        }
    }

    public ObservableList<Budget> getBudgets() {
        return this.mBudgets;
    }

    public void setBudgets(ObservableList<Budget> pBudgets) {
        this.mBudgets = pBudgets;
    }

    public void addTransaction(Transaction pTransaction){
        this.mTransactions.add(pTransaction);
    }

    public void addBudget(Budget pBudget){
        this.mBudgets.add(pBudget);
    }

    public Double getBalance(){
        Double tSum = 0.0;
        for(Transaction tTransaction: getTransactions()){
            tSum += tTransaction.getAmount();
        }
        return tSum;
    }

    /**
     * Calculates balance of category.
     * @param pCategory - The category type
     * @return
     */
    public Double getBalanceByCategory(String pCategory){
        Double tSum = 0.0;
        for(Transaction tTransaction: mTransactions){
            if(GeneralHelper.cleanCategoryString(pCategory).equals(tTransaction.getCategory())){
                tSum += tTransaction.getAmount();
            }
        }
        return tSum;
    }
    
    public void SetUserAccountFromJSONObject(JSONObject jsonObject) {
        try{
            JSONParser parser = new JSONParser();
            JSONObject accountData = (JSONObject) parser.parse(jsonObject.toJSONString());

            this.mName = (String) accountData.get("AccountName");

            // No clue why, but this fixes an error.
            this.mAccountNumber = ((Long) accountData.get("AccountNumber")).intValue();
        
            // Getting transaction data from JSONObject
            JSONArray transactionData = (JSONArray) accountData.get("Transactions");
            this.mTransactions.clear();
            for(int i = 0; i < transactionData.size(); i++) {
                JSONObject transactionObj = (JSONObject) transactionData.get(i);
                String tDate = (String) transactionObj.get("Date");
                String tPayee = (String) transactionObj.get("Payee");
                String tCategory = (String) transactionObj.get("Category");
                String tNote = (String) transactionObj.get("Note");
                double tAmount = (Double) transactionObj.get("Amount");
                Transaction tTransaction = new Transaction(tDate, tPayee, tCategory, tNote, tAmount);
                this.mTransactions.add(tTransaction);
            }
            
            // Getting budget data from JSONObject
            JSONArray budgetData = (JSONArray) accountData.get("Budgets");
            this.mBudgets.clear();
            for(int i = 0; i < budgetData.size(); i++) {
                JSONObject budgetObj = (JSONObject) budgetData.get(i);
                String tCategory = (String) budgetObj.get("Category");
                double tBudgeted = (Double) budgetObj.get("Budgeted");
                double tSpent = (Double) budgetObj.get("Spent");
                Budget tBudget = new Budget(tCategory, tBudgeted, tSpent);
                this.mBudgets.add(tBudget);
            }
        }
        catch(Exception e){
            System.out.println("An error has occured: " + e.getMessage());
            System.out.println("Stack Trace: " + e.getStackTrace());
        }
    }

    public Budget getBudgetByBudgetCode(String pBudgetCode){
        // Users fault currently if they create more then one budget with the same code. 
        List<Budget> budgets = new ArrayList<Budget>();
        if(pBudgetCode != null){
            for(Budget b: mBudgets){
                if(pBudgetCode.equals(b.getCategory())){
                    budgets.add(b);
                }
            }
        }
        if(budgets.size() > 0){
            return budgets.get(0);
        }
        return null;
    }

    public Budget findClosestToFilled() {
        Budget closestBudget = null;
        double closestRatio = Double.MAX_VALUE;
    
        for (Budget budget : mBudgets) {
            double ratio = budget.getSpent() / budget.getBudgeted();
    
            if (ratio >= 1.0) {
                // The budget is already filled, so we can return it immediately.
                return budget;
            }
    
            if (1.0 - ratio < closestRatio) {
                closestRatio = 1.0 - ratio;
                closestBudget = budget;
            }
        }
    
        return closestBudget;
    }

    public JSONObject toJSONObject() {
        JSONObject accountData = new JSONObject();
        accountData.put("AccountName", this.mName);
        accountData.put("AccountNumber", this.mAccountNumber);

        // Generate transaction data as a JSONArray
        JSONArray transactionData = new JSONArray();
        for (Transaction tTransaction : this.mTransactions) {
            JSONObject transactionObj = new JSONObject();
            transactionObj.put("Date", tTransaction.getDateAsString());
            transactionObj.put("Payee", tTransaction.getPayee());
            transactionObj.put("Category", tTransaction.getCategory());
            transactionObj.put("Note", tTransaction.getNote());
            transactionObj.put("Amount", tTransaction.getAmount());
            transactionData.add(transactionObj);
        }
        accountData.put("Transactions", transactionData);

        // Generate budget data as a JSONArray
        JSONArray budgetData = new JSONArray();
        for (Budget tBudget : this.mBudgets) {
            JSONObject budgetObj = new JSONObject();
            budgetObj.put("Category", tBudget.getCategory());
            budgetObj.put("Budgeted", tBudget.getBudgeted());
            budgetObj.put("Spent", tBudget.getSpent());
            budgetData.add(budgetObj);
        }
        accountData.put("Budgets", budgetData);

        return accountData;
    }
}