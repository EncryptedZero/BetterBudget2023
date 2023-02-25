package Tests;
import java.util.ArrayList;
import java.util.List;

import Helper.FileHelper;
import User.Account;
import User.Budget;
import User.Transaction;

// Right now this class just runs a test through constructor. Not a great one,
public class FileHelperTest {

    public FileHelperTest(){
        System.out.println("Testing: ");

        // First Creating Test data. 
        String tAccountName;
        int tAccountNumber;
        List<Transaction> tTransactions = new ArrayList<Transaction>();
        List<Budget> tBudgets = new ArrayList<Budget>();

        tAccountName = "Test Account";
        tAccountNumber = 3;
        
        Transaction tTransaction1 = new Transaction("4/20/2023", "SnoopDog", "Fun", "Um... This is fun hehe", 200.00);
        tTransactions.add(tTransaction1);

        Budget tBudget1 = new Budget("Fun", 500, 50);
        tBudgets.add(tBudget1);

        // Now testing account to string
        Account account1 = new Account(tAccountName, tAccountNumber, tTransactions, tBudgets);
        System.out.println(account1.toString());

        // Writing/Saving data.json file. 
        FileHelper.writeJSONFile(account1.toJSONObject());

        // Creating a second account to read from file.
        Account account2 = new Account();
        account2.SetUserAccountFromJSONObject(FileHelper.readJSONFile());

        // Should have the same data as account1
        System.out.println(account2.toString());
    }
}
