package Graphical;

import Helper.GeneralHelper;
import User.Budget;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BudgetEntryBox {
     
    Stage mStage = new Stage();
    private boolean validated = true;
    private Budget mBudgetResult = new Budget();

    
    public Budget getBudget(){
        // Prevents error with empty budget getting into data if closed incorrectly
        if(mBudgetResult.getCategory().isEmpty()){
            return null;
        }
        else{
            return mBudgetResult;
        }
    }

    public void display(){
        // Create a VBox container with some padding
        VBox vbox = new VBox(10);
        
        Label introLabel = new Label("Please enter budget information");

        // Can be empty on submit
        Label cBudgetCodeLabel = new Label("Budget Code:");
        TextField cBudgetCodeField = new TextField();

        // Can be empty on submit
        Label cBudgetAmountLabel = new Label("Budget Amount:");
        TextField cBudgetAmountField = new TextField();

        Button createButton = new Button("Enter");
        createButton.setOnAction(e -> {
            // Resetting text fills
            cBudgetAmountLabel.setTextFill(Color.BLACK);
            cBudgetCodeLabel.setTextFill(Color.BLACK);

            // Resetting boarder colors
            cBudgetAmountField.setStyle("-fx-border-color: black;");
            cBudgetCodeField.setStyle("-fx-border-color: black;");

            // setting validated to true
            validated = true;

            // Next goal is to assemble Budget object with error trapping
            double oAmount = 0.0;
            String oBudgetCode = "";

            // Getting/Validating amount
            try{
                oAmount = Math.abs(Double.parseDouble(cBudgetAmountField.getText()));
            }
            catch(Exception ex){
                validated = false;
                cBudgetAmountLabel.setText("Please enter the budget amount as a positive number:");
                cBudgetAmountLabel.setTextFill(Color.color(1, 0, 0));
                cBudgetAmountField.setStyle("-fx-border-color: red;");
            }

            // Getting/Validating budgetCode
            // Trying to get note, and budget code
            if(!cBudgetCodeField.getText().equals("")){
                oBudgetCode = GeneralHelper.cleanCategoryString(cBudgetCodeField.getText());
            }
            else{
                validated = false;
                cBudgetCodeLabel.setText("Please enter the budget code:");
                cBudgetCodeLabel.setTextFill(Color.color(1, 0, 0));
                cBudgetCodeField.setStyle("-fx-border-color: red;");
            }
            
            if(validated){
                mBudgetResult.setBudgeted(oAmount);
                mBudgetResult.setCategory(oBudgetCode);
                mStage.close();
            }
        });

        vbox.getChildren().addAll(introLabel, cBudgetCodeLabel, cBudgetCodeField);
        vbox.getChildren().addAll(cBudgetAmountLabel, cBudgetAmountField, createButton);
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #d0bfff, #befed8);");

        // Forces focus to this window
        mStage.initModality(Modality.APPLICATION_MODAL);
        mStage.setTitle("Transaction Entry");
        mStage.setMinWidth(200);

        Scene mTransactionEntryScene = new Scene(vbox, 400, 600);
        mStage.setScene(mTransactionEntryScene);
        mStage.showAndWait();
    }
}
