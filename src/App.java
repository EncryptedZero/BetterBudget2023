
import Helper.FileHelper;
import Scenes.HomeScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
public class App extends Application {

    Stage mStage;
    Scene mNewScene;
    HomeScene tHomeScene = new HomeScene();

    @Override
    public void start(Stage pStage) {
        this.mStage = pStage;

        // THINKING ABOUT TURNING EACH SCENE INTO CLASSES.
        // See HomeScene class and usage here as an example. 

        // Not completely sure what this is for... All testing until next slashes I think
        Label label1 = new Label("Name:");
        TextField textField = new TextField();
        HBox tHBNewInput1 = new HBox();
        tHBNewInput1.getChildren().addAll(label1, textField);
        tHBNewInput1.setSpacing(10);
        //

        // This will be the create new account starting scene
        // Will be moved to a class.
        Label tScene2Label = new Label("File not found.");
        Button tScene2Button = new Button("Home");
        tScene2Button.setOnAction(e -> mStage.setScene(tHomeScene.getCurrentScene()));
        VBox tScene2Layout = new VBox(20);
        tScene2Layout.getChildren().addAll(tScene2Label, tScene2Button);

        mNewScene = new Scene(tScene2Layout, 300, 250);

        this.mStage.setTitle("Hello World!");

        CalculateStartingScene();
        this.mStage.show();
    }
 
    public void CalculateStartingScene(){
        /**
         * This determines if user has an account of if file is found
         */
        if(FileHelper.dataFileExists()){
            // If file cannot be read this will fail
            try{
                // Read file first using fileHelper, and try to turn into object to test.
                // Make sure that if exceptions are possible they are thrown up to here in the catch block

                // If that works then set scene
                this.mStage.setScene(tHomeScene.getCurrentScene());
            }
            catch(Exception e){
                System.out.println("Error reading file: " + e.getStackTrace());
                this.mStage.setScene(mNewScene);
            }
        }
        else{
            this.mStage.setScene(mNewScene);
        }
    }
 public static void main(String[] args) {
        launch(args);
    }
}