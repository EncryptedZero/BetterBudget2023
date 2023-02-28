package Scenes;

import Graphical.AlertBox;
import Helper.FileHelper;
import User.Account;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This should be the one of the most complex classes of our program.
 * ***
 * This class should display account number, account name at the top of the scene.
 * ***
 * TABLE FOR TRANSACTOIONS
 * 
 * Then it should display transactions over time, in order of date.
 * We can have buttons sort by oldest/newest buttons to filter just deposits or widthdrawls.
 * Maybe a select budget code.
 * Have a graph of account balance over time
 * Can add transaction OR launch a pop up to handle adding transactions.
 * Should be button to delete transactions... edit may be hard if someone can figure out
 * 
 * ***
 * TABLE FOR BUDGETS
 * 
 * Recalculates budget spent based on new transactions added.
 * Can add budgets.
 * Can delete budgets.
 * 
 */
public class HomeScene extends AbstractScene{

    private Account mAccount = Account.getInstance();

    private static final HomeScene INSTANCE = new HomeScene();

    protected HomeScene(){
        super();
    }

    public static synchronized HomeScene getInstance() {
        return INSTANCE;
    }

    @Override
    public void initialize() {
        Label cAccountNameLabel = new Label("Account Name: " + mAccount.getName());
        Label cAccountNumberLabel = new Label("Account Number: " + mAccount.getAccountNumber());
        Label cAccountBalanceLabel;
        try{
            double tAccountBalance = mAccount.getBalance();
            if(tAccountBalance < 0){
                cAccountBalanceLabel = new Label("Account Balance: -$" + Math.abs(mAccount.getBalance()));
            }
            else{       
                cAccountBalanceLabel = new Label("Account Balance: $" + mAccount.getBalance());
            }
        }
        catch(Exception e){
            cAccountBalanceLabel = new Label("Add transactions to see balance.");
        } 

        VBox cSceneLeftLayout = new VBox(20);
        cSceneLeftLayout.getChildren().addAll(cAccountNameLabel, cAccountNumberLabel, cAccountBalanceLabel, getSaveButton());

        VBox cSceneRightLayout = new VBox(20);

        // This is testing for now
        cSceneRightLayout.getChildren().addAll(cAccountNameLabel, cAccountNumberLabel, cAccountBalanceLabel, getSaveButton());

        HBox cSceneFullLayout = new HBox(20);
        cSceneFullLayout.getChildren().addAll(cSceneLeftLayout, cSceneRightLayout);

        Scene tTempScene = new Scene(cSceneFullLayout, getScreenWidth(), getScreenHeight());
        setCurrentScene(tTempScene);
    }

    private Button getSaveButton(){
        Button cSaveButton = new Button("Save");
        cSaveButton.setOnAction(e -> {
            FileHelper.writeJSONFile(mAccount.toJSONObject());
            AlertBox.display("Saved", "Data has been saved");
        });
        return cSaveButton;
    }
}
