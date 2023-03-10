package Scenes;

import Graphical.AlertBox;
import Stages.MainStage;
import User.Account;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/*
 * Just want to make an introduction, and get a users account name and number.
 */
public class NewAccountScene extends AbstractScene{

        private Account mAccount = Account.getInstance();
        private MainStage mMainStage = MainStage.getInstance();
    
        private static final NewAccountScene INSTANCE = new NewAccountScene();

        protected NewAccountScene(){
            super();
        }
    
        public static synchronized NewAccountScene getInstance() {
            return INSTANCE;
        }

        @Override
        public void initialize() {
            // Create a VBox container with some padding
            VBox vbox = new VBox();
            vbox.setPadding(new Insets(10));
        
            // Add an introduction label to the VBox
            Label introLabel = new Label("Welcome to BetterBudget!");
            vbox.getChildren().add(introLabel);
        
            // Add a label and two text input fields for the account name and number
            Label nameLabel = new Label("Account Name:");
            TextField nameField = new TextField();
            Label numberLabel = new Label("Account Number:");
            TextField numberField = new TextField();
            vbox.getChildren().addAll(nameLabel, nameField, numberLabel, numberField);
        
            // Add a "Create" button to the VBox
            Button createButton = new Button("Create");
            vbox.getChildren().add(createButton);
        
            // Add an action listener to the "Create" button to handle errors and create the account
            createButton.setOnAction(e -> {
                String name = nameField.getText();
                String numberText = numberField.getText();
                if(name.isEmpty()){
                    AlertBox.display("Error", "Account Name cannot be empty");
                    return;
                }
                if(numberText.isEmpty()){
                    AlertBox.display("Error", "Account Number cannot be empty");
                    return;
                }
                try {
                    int number = Integer.parseInt(numberText);
                    mAccount.setAccountNumber(number);
                    mAccount.setName(name);
                    HomeScene.getInstance().initialize();
                    MainStage.getInstance().setScene(HomeScene.getInstance().getCurrentScene());
                    System.out.println("We make it this far1");
                    MainStage.getInstance().show();
                    System.out.println("We make it this far2");
                }
                catch(NumberFormatException ex) {
                    AlertBox.display("Error", "Invalid account number: " + numberText);
                }
            });

            Scene tTempScene = new Scene(vbox, getScreenWidth(), getScreenHeight());
            setCurrentScene(tTempScene);
        }
    
}
