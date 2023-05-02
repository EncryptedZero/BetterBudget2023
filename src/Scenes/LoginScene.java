package Scenes;

import Stages.MainStage;
import User.Users;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LoginScene extends AbstractScene {

    
    private static final LoginScene INSTANCE = new LoginScene();

    protected LoginScene(){
        super();
    }

    public static synchronized LoginScene getInstance() {
        return INSTANCE;
    }

    @Override
    public void initialize() {
        // main vbox
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 5, 10, 5));
    
        Label introLabel = new Label("Login");
        introLabel.setStyle("-fx-font-size: 1.5em; -fx-font-weight: bold;");
        vbox.getChildren().add(introLabel);
    
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Label errorTextLabel = new Label("");
        
        Button loginButton = new Button("Login");
        Button signupButton = new Button("Sign up");

        HBox hboxButtons = new HBox(5);
        hboxButtons.getChildren().addAll(loginButton, signupButton);

        vbox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, errorTextLabel, hboxButtons);
    
        loginButton.setOnAction(e -> {
            if(usernameField.getText().isEmpty()){
                errorTextLabel.setText("Error: Username cannot be empty");
                errorTextLabel.setTextFill(Color.color(1, 0, 0));
                usernameField.setStyle("-fx-border-color: red;");
                passwordField.setStyle("-fx-border-color: black;");
                return;
            }

            if(passwordField.getText().isEmpty()){
                errorTextLabel.setText("Error: Password cannot be empty");
                errorTextLabel.setTextFill(Color.color(1, 0, 0));
                passwordField.setStyle("-fx-border-color: red;");
                usernameField.setStyle("-fx-border-color: black;");
                return;
            }

            if(Users.getInstance().isUsernameTaken(usernameField.getText())){
                if(Users.getInstance().login(usernameField.getText(), passwordField.getText())){
                        // Launch accounts scene
                        usernameField.setStyle("-fx-border-color: green;");
                        passwordField.setStyle("-fx-border-color: green;");

                        // If they don't have any account we will put them right to the create account screen. 
                        if(Users.getInstance().getCurrentUser().getAccounts().size() < 1){
                            NewAccountScene.getInstance().initialize();
                            MainStage.getInstance().setScene(NewAccountScene.getInstance().getCurrentScene());
                            MainStage.getInstance().show();
                        }
                        else{
                            AccountsScene.getInstance().initialize();
                            MainStage.getInstance().setScene(AccountsScene.getInstance().getCurrentScene());
                            MainStage.getInstance().show();
                        }
                }
                else{
                    errorTextLabel.setText("Error: Password is incorrect");
                    errorTextLabel.setTextFill(Color.color(1, 0, 0));
                    passwordField.setStyle("-fx-border-color: red;");
                    usernameField.setStyle("-fx-border-color: black;");
                }
            }
            else{
                errorTextLabel.setText("Error: Username not found");
                errorTextLabel.setTextFill(Color.color(1, 0, 0));
                usernameField.setStyle("-fx-border-color: red;");
                passwordField.setStyle("-fx-border-color: black;");
            }
        });

        signupButton.setOnAction(e -> {
            // Launch sign up scene
            SignUpScene.getInstance().initialize();
            MainStage.getInstance().setScene(SignUpScene.getInstance().getCurrentScene());
            MainStage.getInstance().show();
        });

        // Setting background color
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #d0bfff, #befed8);");

        Scene tTempScene = new Scene(vbox, 400, 400);
        setCurrentScene(tTempScene);
    }
    
}
