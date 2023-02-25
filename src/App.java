
import Helper.FileHelper;
import Tests.FileHelperTest;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
public class App extends Application {

    Stage mStage;
    Scene mScene1, mScene2;

    @Override
    public void start(Stage pStage) {
        this.mStage = pStage;

        Label tScene1Label = new Label("File Already found.");
        Button tScene1Button = new Button("Scene 2");
        tScene1Button.setOnAction(e -> mStage.setScene(mScene2));
        VBox tScene1Layout = new VBox(20);
        tScene1Layout.getChildren().addAll(tScene1Label, tScene1Button);

        Label tScene2Label = new Label("File not found.");
        Button tScene2Button = new Button("Scene 1");
        tScene2Button.setOnAction(e -> mStage.setScene(mScene1));
        VBox tScene2Layout = new VBox(20);
        tScene2Layout.getChildren().addAll(tScene2Label, tScene2Button);
  
        /*
        Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        Scene scene = new Scene(root);
        */
  
        mScene1 = new Scene(tScene1Layout, 300, 250);
        mScene2 = new Scene(tScene2Layout, 300, 250);

        this.mStage.setTitle("Hello World!");
        if(FileHelper.dataFileExists()){
            this.mStage.setScene(mScene1);
        }
        else{
            this.mStage.setScene(mScene2);
        }
        this.mStage.show();
    }
 
 public static void main(String[] args) {
        launch(args);
    }
}