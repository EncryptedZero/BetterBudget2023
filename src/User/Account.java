package User;

import java.time.LocalDateTime;
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
    private LocalDateTime lastAccessed;
    private ObservableList<Transaction> mTransactions = FXCollections.observableArrayList();
    private ObservableList<Transaction> mTransactionsWorkingList = FXCollections.observableArrayList();
    private ObservableList<Budget> mBudgets = FXCollections.observableArrayList();

    public Account(){
    }

    @Override
    public String toString() {
        StringBuilder tStringBuilderWorkingVar = new StringBuilder();
        String tSeparator = System.lineSeparator();

        tStringBuilderWorkingVar.append("Account Name: ");
        tStringBuilderWorkingVar.append(this.mName);
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Account Number: ");
        tStringBuilderWorkingVar.append(this.mAccountNumber);
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Account Balance: ");
        tStringBuilderWorkingVar.append(this.getBalance());
        tStringBuilderWorkingVar.append(tSeparator);

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

    public void setLastAccessed(LocalDateTime pLocalDateTime) {
        this.lastAccessed = pLocalDateTime;
    }

    public LocalDateTime getLastAccessed() {
        if(this.lastAccessed == null){
            this.lastAccessed = LocalDateTime.now();
        }
        return this.lastAccessed;
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
        if(pTransaction != null){
            this.mTransactions.add(pTransaction);
        }
    }

    public void addBudget(Budget pBudget){
        if(pBudget != null){
            this.mBudgets.add(pBudget);
        }
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