package Graphical;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import Helper.GeneralHelper;
import User.Transaction;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TransactionEntryBox {
    
    Stage mStage = new Stage();
    private Boolean mIsPositive = false;
    private Transaction mTransactionResult = new Transaction();
    public Transaction getTransaction(){
        // Prevents error with empty transaction getting into data if closed incorrectly
        if(mTransactionResult.getDateAsString().isEmpty()){
            return null;
        }
        else{
            return mTransactionResult;
        }
    }

    public void display(){
        
        // Create a VBox container with some padding
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 5, 10, 5));
        
        Label introLabel = new Label("Please enter transaction information");

        // Add a label and two text input fields for the account name and number
        Label cMonthLabel = new Label("Month:");
        TextField cMonthField = new TextField();
        
        Label cDayLabel = new Label("Day:");
        TextField cDayField = new TextField();

        Label cYearLabel = new Label("Year:");
        TextField cYearField = new TextField();

        Label cPayeeLabel = new Label("To:");
        TextField cPayeeField = new TextField();

        // Can be empty on submit
        Label cBudgetCodeLabel = new Label("Budget Code:");
        TextField cBudgetCodeField = new TextField();

        // Can be empty on submit
        Label cNoteLabel = new Label("Note:");
        TextArea cNoteTextArea = new TextArea();
        
        Label cAmountLabel = new Label("Amount:");
        TextField cAmountField = new TextField();
        RadioButton cIsPositive = new RadioButton("Deposit?");
        cIsPositive.setOnAction(e -> {
            mIsPositive = !mIsPositive;
            if(mIsPositive){
                cPayeeLabel.setText("From:");
            }
            else{
                cPayeeLabel.setText("To:");
            }
        });
            
        Button createButton = new Button("Enter");
        createButton.setOnAction(e -> {
            // Resetting text fills
            cDayLabel.setTextFill(Color.BLACK);
            cMonthLabel.setTextFill(Color.BLACK);
            cYearLabel.setTextFill(Color.BLACK);
            cPayeeLabel.setTextFill(Color.BLACK);
            cAmountLabel.setTextFill(Color.BLACK);
            cNoteLabel.setTextFill(Color.BLACK);
            cBudgetCodeLabel.setTextFill(Color.BLACK);

            // Resetting boarder colors
            cDayField.setStyle("-fx-border-color: black;");
            cMonthField.setStyle("-fx-border-color: black;");
            cYearField.setStyle("-fx-border-color: black;");
            cPayeeField.setStyle("-fx-border-color: black;");
            cAmountField.setStyle("-fx-border-color: black;");
            cNoteTextArea.setStyle("-fx-border-color: black;");
            cBudgetCodeField.setStyle("-fx-border-color: black;");

            // Next goal is to assemble Transaction object with error trapping

            // t is for temp
            int tMonth = 1, tDay = 1, tYear = 2020;

            Boolean validated = true;

            // o is for object part
            String oDate = "";
            String oNote = ""; 
            String oPayee = ""; 
            String oBudgetCode = "";
            Double oAmount = 0.0;

            // Getting/validing month
            try{
                tMonth = Integer.parseInt(cMonthField.getText());
                if(tMonth < 1 || tMonth > 12){
                    // Don't want to  repeat color changing code, so just throwing an exception to get to the catch
                    throw new Exception();
                }
                else{
                    // Want to reset value if successful
                    cMonthLabel.setText("Month:");
                }
            }
            catch(Exception ex){
                validated = false;
                cMonthLabel.setText("Please select month as a number 1 through 12");
                cMonthLabel.setTextFill(Color.color(1, 0, 0));
                cMonthField.setStyle("-fx-border-color: red;");
            }

            // Getting/validing day
            try{
                tDay = Integer.parseInt(cDayField.getText());
                if(tDay < 1 || tDay > getDaysInMonth(tMonth)){
                    // Don't want to  repeat color changing code, so just throwing an exception to get to the catch
                    throw new Exception();
                }
                else{
                    // Want to reset value if successful
                    cDayLabel.setText("Day:");
                }
            }
            catch(Exception ex){
                validated = false;
                if(tMonth == 2){
                    // Could support but would be annoying/messy for a 1/1400 edge case with an easy user solution.
                    cDayLabel.setText("Please select day as a number 1 through " + getDaysInMonth(tMonth) + ". For a leap year, just use the 28th.");
                }
                else{
                    cDayLabel.setText("Please select day as a number 1 through " + getDaysInMonth(tMonth));
                }
                cDayLabel.setTextFill(Color.color(1, 0, 0));
                cDayField.setStyle("-fx-border-color: red;");
            }

            // Getting/validing year
            try{
                tYear = Integer.parseInt(cYearField.getText());
                if(tYear < 1900){
                    // Don't want to  repeat color changing code, so just throwing an exception to get to the catch
                    throw new Exception();
                }
                else{
                    // Want to reset value if successful
                    cYearLabel.setText("Year:");
                }
            }
            catch(Exception ex){
                validated = false;
                cYearLabel.setText("Please select year as a number, most recent year accepted is 1900.");
                cYearLabel.setTextFill(Color.color(1, 0, 0));
                cYearField.setStyle("-fx-border-color: red;");
            }
            
            // Testing if final date works. May seem extreme but if a broken date gets it, it can corrupt the whole file.
            if(validated){
                oDate = tMonth + "/" + tDay + "/" + tYear;
                DateFormat df = new SimpleDateFormat("M/d/yyyy"); 
                Date tDate;
                try {
                    tDate = df.parse(oDate);
                    String newDateString = df.format(tDate);
                }
                catch (Exception ex) {
                    validated = false;
                    ex.printStackTrace();
                    AlertBox.display("Error", "Hello, it appears you have some how managed to beat the date validation :P I have no idea " + 
                    "how, and I don't know what went wrong. Please re-enter the date fields correctly.");
                }
            }

            // Validating/Getting payee field
            if("".equals(cPayeeField.getText())){
                validated = false;
                cPayeeField.setStyle("-fx-border-color: red;");
                cPayeeLabel.setTextFill(Color.color(1, 0, 0));
            }
            else{
                oPayee = cPayeeField.getText();
            }

            // Getting/Validating amount
            try{
                oAmount = Double.parseDouble(cAmountField.getText());
                // Making it negative if it needs to be
                if(!mIsPositive){
                    oAmount = 0.0 - oAmount;
                }
            }
            catch(Exception ex){
                validated = false;
                cAmountLabel.setText("Please enter the transaction amount as a positive number:");
                cAmountLabel.setTextFill(Color.color(1, 0, 0));
                cAmountField.setStyle("-fx-border-color: red;");
            }

            // Trying to get note, and budget code
            if(!cBudgetCodeField.getText().equals("")){
                oBudgetCode = GeneralHelper.cleanCategoryString(cBudgetCodeField.getText());
            }

            oNote = cNoteTextArea.getText();

            if(validated){
                mTransactionResult.setDate(oDate);
                mTransactionResult.setAmount(oAmount);
                mTransactionResult.setCategory(oBudgetCode);
                mTransactionResult.setDate(oDate);
                mTransactionResult.setPayee(oPayee);
                mTransactionResult.setNote(oNote);
                mStage.close();
            }
        });

        vbox.getChildren().addAll(introLabel, cMonthLabel, cMonthField, cDayLabel, cDayField, cYearLabel, cYearField, cIsPositive);
        vbox.getChildren().addAll(cPayeeLabel, cPayeeField, cBudgetCodeLabel, cBudgetCodeField, cNoteLabel, cNoteTextArea);
        vbox.getChildren().addAll(cAmountLabel, cAmountField, createButton);
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #d0bfff, #befed8);");

        // Forces focus to this window
        mStage.initModality(Modality.APPLICATION_MODAL);
        mStage.setTitle("Transaction Entry");
        mStage.setMinWidth(200);

        Scene mTransactionEntryScene = new Scene(vbox, 400, 600);
        mStage.setScene(mTransactionEntryScene);
        mStage.showAndWait();
    }

    private static int getDaysInMonth(int month) {
        int days;
        switch (month) {
            case 2:
                days = 28; // Best way to handle leap years... to ignore them
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            default:
                days = 31;
                break;
        }
        return days;
    }
}
