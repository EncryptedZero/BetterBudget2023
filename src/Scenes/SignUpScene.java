package Scenes;

import Helper.FileHelper;
import Stages.MainStage;
import User.User;
import User.Users;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class SignUpScene extends AbstractScene {
    private static final SignUpScene INSTANCE = new SignUpScene();

    protected SignUpScene(){
        super();
    }

    public static synchronized SignUpScene getInstance() {
        return INSTANCE;
    }

    @Override
    public void initialize() {
         // main vbox
         VBox vbox = new VBox(10);
    
         Label introLabel = new Label("Sign Up");
         introLabel.setStyle("-fx-font-size: 1.5em; -fx-font-weight: bold;");
         vbox.getChildren().add(introLabel);
     
         Label usernameLabel = new Label("Username:");
         TextField usernameField = new TextField();
         Label passwordLabel = new Label("Password:");
         PasswordField passwordField = new PasswordField();
 
         Label errorTextLabel = new Label("");
         
         Button backButton = new Button("Login");
         Button signupButton = new Button("Sign up");
 
         HBox hboxButtons = new HBox(5);
         hboxButtons.getChildren().addAll(signupButton, backButton);
 
         vbox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, errorTextLabel, hboxButtons);
     
         signupButton.setOnAction(e -> {

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

            if(!Users.getInstance().isUsernameTaken(usernameField.getText())){
                User tempUser = new User();
                tempUser.CreateUser(usernameField.getText(), passwordField.getText());
                Users.getInstance().addUser(tempUser);

                errorTextLabel.setText("");
                usernameField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                passwordField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

                // all of this is because I want to show the borders green for success instead of launching an ugly alertbox.
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.25), event -> {
                        // Going back to login screen
                        try{
                            FileHelper.writeJSONFile(Users.getInstance().toJSONObject());
                            LoginScene.getInstance().initialize();
                            MainStage.getInstance().setScene(LoginScene.getInstance().getCurrentScene());
                            MainStage.getInstance().show();
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                }));
                
                timeline.play();
            }
            else{
                errorTextLabel.setText("Error: Username taken");
                errorTextLabel.setTextFill(Color.color(1, 0, 0));
                usernameField.setStyle("-fx-border-color: red;");
            }
         });
 
        backButton.setOnAction(e -> {
             // Going back to login screen
             LoginScene.getInstance().initialize();
             MainStage.getInstance().setScene(LoginScene.getInstance().getCurrentScene());
             MainStage.getInstance().show();
         });
 
         // Setting background color
         vbox.setStyle("-fx-background-color: linear-gradient(to bottom,  #befed8, #d0bfff);");
 
         Scene tTempScene = new Scene(vbox, 400, 400);
         setCurrentScene(tTempScene);
    }
}
