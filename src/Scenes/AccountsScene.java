package Scenes;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Stages.MainStage;
import User.Account;
import User.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AccountsScene extends AbstractScene {

    private static final AccountsScene INSTANCE = new AccountsScene();

    protected AccountsScene(){
        super();
    }

    public static synchronized AccountsScene getInstance() {
        return INSTANCE;
    }

    @Override
    public void initialize() {
        // main vbox
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 5, 10, 5));

        Label labelAccounts = new Label("Select Account from Below");

        List<Account> tAccounts = Users.getInstance().getCurrentUser().getAccounts();

        // Sort accounts based on date using comparator, assuming uses has a last access DATETIME field
        tAccounts = sortAccountsListByDatetime(tAccounts);

        ListView<Account> accountsListView = new ListView<Account>();
        ObservableList<Account> observableAccounts = FXCollections.observableList(tAccounts);
        accountsListView.setItems(observableAccounts);
        
        accountsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        accountsListView.setCellFactory(lv -> {
            ListCell<Account> cell = new ListCell<Account>() {
                @Override
                protected void updateItem(Account item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        
            cell.setOnMouseClicked(event -> {
                // Item is not selected, select it and clear any other selections
                accountsListView.getSelectionModel().clearAndSelect(cell.getIndex());

                // Handling selection here
                Account selectedAccount = accountsListView.getSelectionModel().getSelectedItem();                
                Users.getInstance().getCurrentUser().setCurrentAccount(selectedAccount);

                HomeScene.getInstance().initialize();
                MainStage.getInstance().setScene(HomeScene.getInstance().getCurrentScene());
                MainStage.getInstance().show();
            });
        
            return cell;
        });
        
    
        // Adding to scroll panel so the listview will have a scrollbar instead of just extending the stage. 
        ScrollPane cScrollPanel = new ScrollPane();

        cScrollPanel.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        cScrollPanel.setContent(accountsListView);
        cScrollPanel.setFitToWidth(true);
        cScrollPanel.setFitToHeight(true);

        Button logout = new Button("Logout");
        logout.setOnAction(e -> {
            LoginScene.getInstance().initialize();
            MainStage.getInstance().setScene(LoginScene.getInstance().getCurrentScene());
            MainStage.getInstance().show();
        });

        Button newAccount = new Button("New Account");
        newAccount.setOnAction(e -> {
            NewAccountScene.getInstance().initialize();
            MainStage.getInstance().setScene(NewAccountScene.getInstance().getCurrentScene());
            MainStage.getInstance().show();
        });

        HBox buttonHBox = new HBox(5);
        buttonHBox.getChildren().addAll(logout, newAccount);

        vbox.getChildren().addAll(labelAccounts, cScrollPanel, buttonHBox);

        vbox.setStyle("-fx-background-color: linear-gradient(to right, #d0bfff, #befed8);");

        Scene tTempScene = new Scene(vbox, 400, 400);
        setCurrentScene(tTempScene);
    }
    
    private List<Account> sortAccountsListByDatetime(List<Account> pUnsortedAccounts) {
        Collections.sort(pUnsortedAccounts, new Comparator<Account>() {
            @Override
            public int compare(Account t1, Account t2) {
                LocalDateTime lastAccessed1 = t1.getLastAccessed();
                LocalDateTime lastAccessed2 = t2.getLastAccessed();
                return lastAccessed1.compareTo(lastAccessed2);
            }
        });
        Collections.reverse(pUnsortedAccounts);
        return pUnsortedAccounts;
    } 
}
