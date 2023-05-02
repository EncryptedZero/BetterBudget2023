package User;

import java.util.ArrayList;
import java.util.List;

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

    public Account(){}

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

    public String getAccountName() {
        return this.mName;
    }

    public void setAccountName(String pName) {
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
}