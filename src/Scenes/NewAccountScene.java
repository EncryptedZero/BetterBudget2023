package Scenes;

import Graphical.AlertBox;
import Helper.FileHelper;
import Stages.MainStage;
import User.Account;
import User.Users;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/*
 * Just want to make an introduction, and get a users account name and number.
 */
public class NewAccountScene extends AbstractScene{

    private Account mAccount = new Account();
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
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 5, 10, 5));
    
        // Add an introduction label to the VBox
        Label introLabel = new Label("Create Account");
        introLabel.setStyle("-fx-font-size: 1.5em; -fx-font-weight: bold;");
        Text descriptionText = new Text("An example may be a monthly home budget, a christmas budget, a vacation or a grocery list.");
        descriptionText.setWrappingWidth(350);
        vbox.getChildren().addAll(introLabel, descriptionText);
    
        // Add a label and two text input fields for the account name and number
        Label nameLabel = new Label("Account Name:");
        TextField nameField = new TextField();
        Label numberLabel = new Label("Account Number:");
        TextField numberField = new TextField();
        
        Button createButton = new Button("Create");
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            if(Users.getInstance().getCurrentUser().getAccounts().size() < 1){
                LoginScene.getInstance().initialize();
                MainStage.getInstance().setScene(LoginScene.getInstance().getCurrentScene());
                MainStage.getInstance().show();
            }
            else{
                AccountsScene.getInstance().initialize();
                MainStage.getInstance().setScene(AccountsScene.getInstance().getCurrentScene());
                MainStage.getInstance().show();
            }
        });

        HBox buttonBox = new HBox(5);
        buttonBox.getChildren().addAll(backButton, createButton);

        vbox.getChildren().addAll(nameLabel, nameField, numberLabel, numberField, buttonBox);
    
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
                mAccount.setAccountName(name);

                Users.getInstance().getCurrentUser().setCurrentAccount(mAccount);

                System.out.println(Users.getInstance().getCurrentUser().getAccounts().size());
                FileHelper.writeJSONFile(Users.getInstance().toJSONObject());

                HomeScene.getInstance().initialize();
                MainStage.getInstance().setScene(HomeScene.getInstance().getCurrentScene());
                MainStage.getInstance().show();
            }
            catch(NumberFormatException ex) {
                AlertBox.display("Error", "Invalid account number: " + numberText);
            }
        });

        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #d0bfff, #befed8);");

        Scene tTempScene = new Scene(vbox, 400, 400);
        setCurrentScene(tTempScene);
    }
    
}
