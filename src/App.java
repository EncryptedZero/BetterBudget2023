
import Helper.FileHelper;
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
    Scene mHomeScene, mNewScene;

    @Override
    public void start(Stage pStage) {
        this.mStage = pStage;


        // THINKING ABOUT TURNING EACH SCENE INTO CLASSES.
        // WOULD PASSING IN NEXT SCENE IN CONSTRUCTOR.
        Label tScene1Label = new Label("File Already found.");
        Button tScene1Button = new Button("New Scene");
        tScene1Button.setOnAction(e -> mStage.setScene(mNewScene));
        VBox tScene1Layout = new VBox(20);
        tScene1Layout.getChildren().addAll(tScene1Label, tScene1Button);

        // Creating labels
        Label label1 = new Label("Name:");
        TextField textField = new TextField();
        HBox tHBNewInput1 = new HBox();
        tHBNewInput1.getChildren().addAll(label1, textField);
        tHBNewInput1.setSpacing(10);

        Label tScene2Label = new Label("File not found.");
        Button tScene2Button = new Button("Home");
        tScene2Button.setOnAction(e -> mStage.setScene(mHomeScene));
        VBox tScene2Layout = new VBox(20);
        tScene2Layout.getChildren().addAll(tScene2Label, tScene2Button);
  
        /*
        Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        Scene scene = new Scene(root);
        */
  
        mHomeScene = new Scene(tScene1Layout, 300, 250);
        mNewScene = new Scene(tScene2Layout, 300, 250);

        this.mStage.setTitle("Hello World!");
        if(FileHelper.dataFileExists()){
            this.mStage.setScene(mHomeScene);
        }
        else{
            this.mStage.setScene(mNewScene);
        }
        this.mStage.show();
    }
 
 public static void main(String[] args) {
        launch(args);
    }
}