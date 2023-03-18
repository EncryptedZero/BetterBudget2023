package Scenes;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import Graphical.AlertBox;
import Graphical.ConfirmBox;
import Graphical.TransactionEntryBox;
import Helper.FileHelper;
import Stages.MainStage;
import User.Account;
import User.Transaction;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
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
    private LineChart mGraph;
    Label cAccountBalanceLabel = new Label("");
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

        VBox cSceneLeftLayout = new VBox(40);

        Button cDeleteAccountButton = new Button("Delete Account");
        cDeleteAccountButton.setOnAction(e -> {
            try{
                ConfirmBox.display("Confirm Account Delete", "Are you sure you would like to delete your account? If yes is selected, program will be closed and account will be deleted.");
                if(ConfirmBox.Confirmed){
                    FileHelper.deleteFile();
                    MainStage.getInstance().closeStage();
                }
            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
                System.out.println(ex.getStackTrace());
            }
        });

        HBox cSceneLeftButtonHBox = new HBox(10);
        cSceneLeftButtonHBox.getChildren().addAll(getSaveButton(), cDeleteAccountButton);

        VBox cSceneLeftAccountPanel = new VBox(10);
        cSceneLeftAccountPanel.getChildren().addAll(cAccountNameLabel, cAccountNumberLabel, cAccountBalanceLabel, cSceneLeftButtonHBox);

        VBox cSceneLeftTransactionPanel = new VBox(20);
        ListView<Transaction> cSceneLeftTransactionListView = new ListView<Transaction>(mAccount.getTransactions());
        
        // Adding to scroll panel so the listview will have a scrollbar instead of just extending the stage. 
        ScrollPane cSceneLeftTransactionScrollPanel = new ScrollPane();
        // Now I know from a logical or philosophical perspective this is wrong, but it fixes my issues. 
        cSceneLeftTransactionScrollPanel.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        cSceneLeftTransactionScrollPanel.setContent(cSceneLeftTransactionListView);
        cSceneLeftTransactionScrollPanel.setFitToWidth(true);
        cSceneLeftTransactionScrollPanel.setFitToHeight(true);
        cSceneLeftTransactionScrollPanel.setPrefViewportWidth(300);
        cSceneLeftTransactionScrollPanel.setPrefViewportHeight(300);

        Button cAddTransactionButton = new Button("Add Transaction");
        cAddTransactionButton.setOnAction(e -> {
            try{
                TransactionEntryBox tTransactionEntryBox = new TransactionEntryBox();
                tTransactionEntryBox.display();
                Transaction tTransaction = tTransactionEntryBox.getTransaction();
                mAccount.addTransaction(tTransaction);
                FileHelper.writeJSONFile(mAccount.toJSONObject());
                refresh();
            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
                System.out.println(ex.getStackTrace());
                AlertBox.display("Error", "An error was encountered when creating a transaction");
            }
        });

        // Not exactly sure why, but a button added after the scrollPane is eaten
        cSceneLeftTransactionPanel.getChildren().add(cSceneLeftTransactionScrollPanel);

        // Could add delete specific transaction, if you really wanted you could do an edit (maybe part 2).

        // Creating this to solve issue with scrollPane eating buttons
        VBox cSceneLeftTransactionContainer = new VBox(2);
        cSceneLeftTransactionContainer.getChildren().addAll(cSceneLeftTransactionScrollPanel, cAddTransactionButton);

        // Creating a label to go above transaction panel
        Label cSceneLeftTransactionPanelLabel = new Label("Transaction Information");

        cSceneLeftLayout.getChildren().addAll(cSceneLeftAccountPanel, cSceneLeftTransactionPanelLabel, cSceneLeftTransactionContainer);

        // Graph creation, do not worry about setting data; it is set in the refresh method.
        if(mAccount.getTransactions() != null && mAccount.getTransactions().size() > 1){

            NumberAxis yAxis = new NumberAxis();  
            yAxis.setLabel("Account Balance");

            NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel("Time in Years");
            mGraph = new LineChart(yAxis, xAxis);
        }


        HBox cSceneFullLayout = new HBox(20);
        cSceneFullLayout.getChildren().add(cSceneLeftLayout);

        // Now for the right scene 
        VBox cSceneRightLayout = new VBox(10);


        Scene tTempScene = new Scene(cSceneFullLayout, 800, 800);
        setCurrentScene(tTempScene);
        
        refresh();
    }



    private Button getSaveButton(){
        Button cSaveButton = new Button("Save");
        cSaveButton.setOnAction(e -> {
            try{
                FileHelper.writeJSONFile(mAccount.toJSONObject());
                AlertBox.display("Saved", "The data has been saved.");
            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
                System.out.println(ex.getStackTrace());
                AlertBox.display("Save Failed", "The data was unsuccessful saved. Please try again.");
            }
        });
        return cSaveButton;
    }

    // Loads/sorts data and keep data current
    private void refresh(){
        updateAccountBalance();
        sortTransactionsByDate();
        updateGraph();
    }

    private void updateGraph(){
        if(mAccount.getTransactions() != null && mAccount.getTransactions().size() > 1){
            LocalDate minDate = LocalDate.MAX;
            LocalDate maxDate = LocalDate.MIN;
            for (Transaction t : mAccount.getTransactions()) {
                LocalDate date = t.getDateAsDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (date.isBefore(minDate)) {
                    minDate = date;
                }
                if (date.isAfter(maxDate)) {
                    maxDate = date;
                }
            }

            // Determine the minimum and maximum account totals in the transactions list
            double minTotal = Double.MAX_VALUE;
            double maxTotal = Double.MIN_VALUE;
            for (Transaction t : mAccount.getTransactions()) {
                if (Math.abs(t.getAmount()) < minTotal) {
                    minTotal = Math.abs(t.getAmount());
                }
                if (Math.abs(t.getAmount()) > maxTotal) {
                    maxTotal = Math.abs(t.getAmount());
                }
            }

            // Calculate the range of dates and the range of account totals
            Period dateRange = Period.between(minDate, maxDate);
            double totalRange = maxTotal - minTotal;

            // Determine the appropriate tick unit for the x-axis and y-axis
            int dateTickUnit = 1;
            if (dateRange.getYears() > 100) {
                dateTickUnit = 10;
            } 
            else if (dateRange.getYears() > 10) {
                dateTickUnit = 1;
            } 
            else if (dateRange.getYears() > 1) {
                dateTickUnit = 1;
            } 
            else {
                dateTickUnit = 1;
            }

            double totalTickUnit = 5000.0;
            if(totalRange > 1000000.0){
                totalTickUnit = 100000.0;
            }
            else if (totalRange > 500000.0){
                totalTickUnit = 100000.0;
            }
            else if (totalRange > 20000.0) {
                totalTickUnit = 5000.0;
            } 
            else if (totalRange > 5000.0) {
                totalTickUnit = 1000.0;
            } 
            else if (totalRange > 1000.0) {
                totalTickUnit = 500.0;
            } 
            else {
                totalTickUnit = 100.0;
            }

            // Create a series of data points for the line chart
            XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();
            double currentTotal = 0.0;
            for (Transaction t : mAccount.getTransactions()) {
                LocalDate date = t.getDateAsDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                currentTotal += t.getAmount();
                dataSeries.getData().add(new XYChart.Data<Number, Number>(date.getYear(), currentTotal));
            }

            // Add the data series to the line chart
            mGraph.getData().add(dataSeries);

            // Set the tick units for the x-axis and y-axis of the line chart
            ((NumberAxis) mGraph.getXAxis()).setTickUnit(dateTickUnit);
            ((NumberAxis) mGraph.getYAxis()).setTickUnit(totalTickUnit);

        }
    }

    private void updateAccountBalance(){
        try{
            double tAccountBalance = mAccount.getBalance();
            if(tAccountBalance < 0){
                cAccountBalanceLabel.setText("Account Balance: -$" + Math.abs(mAccount.getBalance()));
            }
            else{       
                cAccountBalanceLabel.setText("Account Balance: $" + mAccount.getBalance());
            }
        }
        catch(Exception e){
            cAccountBalanceLabel.setText("Account Balance: $0");
        } 
    }

    private void sortTransactionsByDate() {
        if (mAccount.getTransactions() != null && mAccount.getTransactions().size() > 1) {
            Collections.sort(mAccount.getTransactions(), new Comparator<Transaction>() {
                @Override
                public int compare(Transaction t1, Transaction t2) {
                    Date d1 = t1.getDateAsDate();
                    Date d2 = t2.getDateAsDate();
                    if (d1 == null && d2 == null) {
                        return 0;
                    } else if (d1 == null) {
                        return 1;
                    } else if (d2 == null) {
                        return -1;
                    } else if (t1.getDateAsString().isEmpty() || t2.getDateAsString().isEmpty()) {
                        return 1; // put empty dates at the end
                    } else {
                        return d1.compareTo(d2);
                    }
                }
            });
            Collections.reverse(mAccount.getTransactions());
        }
    }
    
    
}
