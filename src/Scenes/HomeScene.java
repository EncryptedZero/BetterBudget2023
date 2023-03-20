package Scenes;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import Graphical.AlertBox;
import Graphical.BudgetEntryBox;
import Graphical.ConfirmBox;
import Graphical.TransactionEntryBox;
import Helper.FileHelper;
import Stages.MainStage;
import User.Account;
import User.Budget;
import User.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
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
    private PieChart mPie;
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
        mAccount.getBudgetByBudgetCode("");
        Label cAccountNameLabel = new Label("Account Name: " + mAccount.getName());
        Label cAccountNumberLabel = new Label("Account Number: " + mAccount.getAccountNumber());

        VBox cSceneLeftLayout = new VBox(20);
        VBox cSceneRightLayout = new VBox(20);

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

        // This just sets it to the Transactions list
        mAccount.setTransactionsWorkingListByBudgetCode("");

        VBox cSceneLeftTransactionPanel = new VBox(20);
        ListView<Transaction> cSceneLeftTransactionListView = new ListView<Transaction>(mAccount.getTransactionsWorkingList());
        
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
                mAccount.setTransactionsWorkingListByBudgetCode("");
                FileHelper.writeJSONFile(mAccount.toJSONObject());
                if(mAccount.getTransactionsWorkingList() != null && mAccount.getTransactionsWorkingList().size() > 2) {
                    cSceneLeftLayout.getChildren().remove(mGraph);
                    mGraph = updateGraph();
                    cSceneLeftLayout.getChildren().add(mGraph);
                } else if(mAccount.getTransactionsWorkingList() != null && mAccount.getTransactionsWorkingList().size() > 1) {
                    mGraph = updateGraph();
                    cSceneLeftLayout.getChildren().add(mGraph);
                }
                cSceneRightLayout.getChildren().remove(mPie);
                mPie = updatePieChart("");
                cSceneRightLayout.getChildren().add(mPie);
                
                refresh();
            }
            catch(Exception ex){
                ex.printStackTrace();
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

        // This solves an issue with there being more than one graph
        if(mAccount.getTransactionsWorkingList() != null && mAccount.getTransactionsWorkingList().size() > 2) {
            cSceneLeftLayout.getChildren().remove(mGraph);
            mGraph = updateGraph();
            cSceneLeftLayout.getChildren().add(mGraph);
        } else if(mAccount.getTransactionsWorkingList() != null && mAccount.getTransactionsWorkingList().size() > 1) {
            mGraph = updateGraph();
            cSceneLeftLayout.getChildren().add(mGraph);
        }

        HBox cSceneFullLayout = new HBox(20);
        cSceneFullLayout.getChildren().add(cSceneLeftLayout);

        // Now for the right scene 

        VBox cSceneRightBudgetLayout = new VBox(10);

        ListView<Budget> cSceneRightBudgetListView = new ListView<Budget>(mAccount.getBudgets());
        
        // Only allow one selection at a time
        cSceneRightBudgetListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        // Adding to scroll panel so the listview will have a scrollbar instead of just extending the stage. 
        ScrollPane cSceneRightBudgetPanel = new ScrollPane();
        // Now I know from a logical or philosophical perspective this is wrong, but it fixes my issues. 
        cSceneRightBudgetPanel.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        cSceneRightBudgetPanel.setContent(cSceneRightBudgetListView);
        cSceneRightBudgetPanel.setFitToWidth(true);
        cSceneRightBudgetPanel.setFitToHeight(true);
        cSceneRightBudgetPanel.setPrefViewportWidth(300);
        cSceneRightBudgetPanel.setPrefViewportHeight(300);

        Button cAddBudgetButton = new Button("Add Budget");
        cAddBudgetButton.setOnAction(e -> {
            try{
                BudgetEntryBox tBudgetEntryBox = new BudgetEntryBox();
                tBudgetEntryBox.display();
                Budget tBudget = tBudgetEntryBox.getBudget();
                mAccount.addBudget(tBudget);
                FileHelper.writeJSONFile(mAccount.toJSONObject());

                // This is where I update/call check on the pie chart, or maybe in the update budget balances actually... that is later though
                
                refresh();
            }
            catch(Exception ex){
                ex.printStackTrace();
                AlertBox.display("Error", "An error was encountered when creating a budget");
            }
        });

        cSceneRightBudgetListView.setCellFactory(lv -> {
            ListCell<Budget> cell = new ListCell<Budget>() {
                @Override
                protected void updateItem(Budget item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        
            cell.setOnMouseClicked(event -> {
                cSceneRightLayout.getChildren().clear();
                cSceneRightLayout.getChildren().add(cSceneRightBudgetLayout);
                cSceneLeftLayout.getChildren().remove(mGraph);
                // Check if the mouse click count is 1 (single-click)
                if (event.getClickCount() == 2) {
                    // Item is already selected, deselect it
                    cSceneRightBudgetListView.getSelectionModel().clearSelection();
                    // Handling deselection here
                    mAccount.setTransactionsWorkingListByBudgetCode("");
                    mPie = updatePieChart("");
                } 
                else if(event.getClickCount() == 1){
                    // Item is not selected, select it and clear any other selections
                    cSceneRightBudgetListView.getSelectionModel().clearAndSelect(cell.getIndex());
                    // Handling selection here
                    Budget selectedBudget = cSceneRightBudgetListView.getSelectionModel().getSelectedItem();
                    if(selectedBudget != null){
                        mAccount.setTransactionsWorkingListByBudgetCode(selectedBudget.getCategory());
                        mPie = updatePieChart(selectedBudget.getCategory());
                    }
                    else{
                        mAccount.setTransactionsWorkingListByBudgetCode("");
                        mPie = updatePieChart("");
                    }
                }
                System.out.println(mAccount.getTransactionsWorkingList().size());
                mGraph = updateGraph();
                sortTransactionsByDate();
                
                try{
                    cSceneLeftLayout.getChildren().add(mGraph);
                }
                catch(Exception e){
                    // Don't need to log, we want it to through an exception here if it only has one or less points to graph.
                }
                cSceneRightLayout.getChildren().add(mPie);
            });
        
            return cell;
        });

        cSceneRightBudgetLayout.getChildren().addAll(cSceneRightBudgetPanel, cAddBudgetButton);

        mPie = updatePieChart("");
        
        cSceneRightLayout.getChildren().addAll(cSceneRightBudgetLayout, mPie);

        cSceneFullLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #d0bfff, #befed8);");
        
        cSceneFullLayout.getChildren().add(cSceneRightLayout);

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
                refresh();
            }
            catch(Exception ex){
                ex.printStackTrace();
                AlertBox.display("Save Failed", "The data was unsuccessful saved. Please try again.");
            }
        });
        return cSaveButton;
    }

    // Loads/sorts data and keep data current
    // Showed be called when program is loaded, transaction is added, or budget is added, or budget is selected (to refresh everything with the budget code)
    private void refresh(){
        updateAccountBalance();
        sortTransactionsByDate();
        updateBudgetBalances();
    }

    private LineChart updateGraph(){
        LineChart tGraph;
        if(mAccount.getTransactionsWorkingList() != null && mAccount.getTransactionsWorkingList().size() > 1){
            LocalDate minDate = LocalDate.MAX;
            LocalDate maxDate = LocalDate.MIN;
            for (Transaction t : mAccount.getTransactionsWorkingList()) {
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
            for (Transaction t : mAccount.getTransactionsWorkingList()) {
                if (t.getAmount() < minTotal) {
                    minTotal = t.getAmount();
                }
                if (t.getAmount() > maxTotal) {
                    maxTotal = t.getAmount();
                }
            }
    
            // Calculate the range of dates and the range of account totals
            Period dateRange = Period.between(minDate, maxDate);
            double totalRange = maxTotal - minTotal;
    
            // Determine the appropriate tick unit for the x-axis and y-axis
            int dateTickUnit = 1;
            if (dateRange.getYears() > 100) {
                dateTickUnit = 10;
            } else if (dateRange.getYears() > 10) {
                dateTickUnit = 1;
            } else if (dateRange.getYears() > 1) {
                dateTickUnit = 1;
            } else {
                dateTickUnit = 12;
            }

            if(dateTickUnit != 12){

                NumberAxis yAxis2 = new NumberAxis();  
                yAxis2.setLabel("Account Balance");
                
                yAxis2.setLowerBound(minTotal);
                yAxis2.setUpperBound(maxTotal); 
            
                NumberAxis xAxis2 = new NumberAxis();
                xAxis2.setLabel("Time in Years");
                
                xAxis2.setLowerBound(minDate.getYear());
                xAxis2.setUpperBound(maxDate.getYear()); 
                xAxis2.setAutoRanging(false);
                System.out.println(maxDate.getYear());

                tGraph = new LineChart(xAxis2, yAxis2);

            } else {
                NumberAxis yAxis2 = new NumberAxis();  
                yAxis2.setLabel("Account Balance");
                
                yAxis2.setLowerBound(minTotal);
                yAxis2.setUpperBound(maxTotal); 
            
                NumberAxis xAxis2 = new NumberAxis();
                xAxis2.setLabel("Time in Months");

                xAxis2.setLowerBound(minDate.getMonthValue());
                xAxis2.setUpperBound(maxDate.getMonthValue()); 

                tGraph = new LineChart(xAxis2, yAxis2);
            }
            
            double totalTickUnit = 5000.0;
            if(totalRange > 1000000.0){
                totalTickUnit = 100000.0;
            } else if (totalRange > 500000.0){
                totalTickUnit = 100000.0;
            } else if (totalRange > 20000.0) {
                totalTickUnit = 5000.0;
            } else if (totalRange > 5000.0) {
                totalTickUnit = 1000.0;
            } else if (totalRange > 1000.0) {
                totalTickUnit = 500.0;
            } else {
                totalTickUnit = 100.0;
            }
            
            // Create a series of data points for the line chart
            XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();
            dataSeries.setName("Account Balance over Time");
            double currentTotal = 0.0;
            // Need to reverse because as of now, the newest dates are at the top of the list
            // This would throw of the currentTotal if not tempary reversed.
            Collections.reverse(mAccount.getTransactionsWorkingList());
            for (Transaction t : mAccount.getTransactionsWorkingList()) {
                LocalDate date = t.getDateAsDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                currentTotal += t.getAmount();
                if(dateTickUnit != 12){
                    dataSeries.getData().add(new XYChart.Data<Number, Number>(date.getYear(), currentTotal));
                } else {
                    dataSeries.getData().add(new XYChart.Data<Number, Number>(date.getMonthValue(), currentTotal));
                }
            }
            Collections.reverse(mAccount.getTransactionsWorkingList());
            
            // Add the data series to the line chart
            tGraph.getData().add(dataSeries);
            
            // Set the tick units for the x-axis and y-axis of the line chart
            ((NumberAxis) tGraph.getXAxis()).setTickUnit(dateTickUnit);
            ((NumberAxis) tGraph.getYAxis()).setTickUnit(totalTickUnit);
            return tGraph;
        }
        return null;
    }
    
    private void updateBudgetBalances(){
        try{
            for(Budget b: mAccount.getBudgets()){
                b.setSpent(0.00);
                // Keep at getTransactions, we want all not working list
                // If working list is got, when one budget is selected, the rest will break.
                for(Transaction t: mAccount.getTransactions()){
                    if(t.getCategory().equals(b.getCategory())){
                        b.setSpent(b.getSpent() - t.getAmount());
                    }
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private PieChart updatePieChart(String pBudgetCode){
        mPie = new PieChart();
        if(pBudgetCode == null || pBudgetCode.equals("")){
            // Add data to the chart
            Budget budget = mAccount.findClosestToFilled();
            
            System.out.println("Closest to filled: " + budget.getCategory());

            String spentString = "Spent";
            if(budget.getSpent() < 0){
                spentString = "Extra Saved";
            }

            // create PieChart data with two slices
            if(budget != null){
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(  
                    new PieChart.Data("Budgeted", budget.getBudgeted()),
                    new PieChart.Data(spentString, budget.getSpent())
                );
                mPie.setData(pieChartData);
            }
        }
        else{
             // Add data to the chart
             Budget budget = mAccount.getBudgetByBudgetCode(pBudgetCode);

             String spentString = "Spent";
             if(budget.getSpent() < 0){
                spentString = "Extra Saved";
             }
             
             // create PieChart data with two slices
             if(budget != null){
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Budgeted", budget.getBudgeted()),
                    new PieChart.Data(spentString, budget.getSpent())
                );

                mPie.setData(pieChartData);
             }
 
        }
        return mPie;
    }

    private void updateAccountBalance(){
        try{
            double tAccountBalance = Math.round(mAccount.getBalance() * 100.00);
            tAccountBalance = tAccountBalance / 100.00;

            if(tAccountBalance < 0){
                cAccountBalanceLabel.setText("Account Balance: -$" + Math.abs(tAccountBalance));
            }
            else{       
                cAccountBalanceLabel.setText("Account Balance: $" + tAccountBalance);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            cAccountBalanceLabel.setText("Account Balance: $0");
        } 
    }

    private void sortTransactionsByDate() {
        if (mAccount.getTransactionsWorkingList() != null && mAccount.getTransactionsWorkingList().size() > 1) {
            Collections.sort(mAccount.getTransactionsWorkingList(), new Comparator<Transaction>() {
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
            Collections.reverse(mAccount.getTransactionsWorkingList());
        }
    }
    
    
}
