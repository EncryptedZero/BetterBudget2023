package Scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    public HomeScene(Scene pNextScene){
        super(pNextScene);
        initialize();
    }

    public HomeScene(){
        super();
        initialize();
    }

    @Override
    protected void initialize() {
        Label tScene1Label = new Label("File Already found.");
        Button tScene1Button = new Button("New Scene");
        VBox tScene1Layout = new VBox(20);
        tScene1Layout.getChildren().addAll(tScene1Label, tScene1Button);

        Scene tTempScene = new Scene(tScene1Layout);
        setCurrentScene(tTempScene);
    }
}
