
import org.json.simple.JSONObject;

import Graphical.AlertBox;
import Helper.FileHelper;
import Scenes.HomeScene;
import Scenes.NewAccountScene;
import Stages.MainStage;
import User.Account;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class App extends Application {
    NewAccountScene mNewScene = NewAccountScene.getInstance();
    Scene tNewScene;
    HomeScene tHomeScene = HomeScene.getInstance();

    @Override
    public void start(Stage pStage) {
        MainStage.getInstance().setStage(pStage);
        mNewScene.initialize();
        tNewScene = mNewScene.getCurrentScene();
        CalculateStartingScene();
        MainStage.getInstance().show();
    }
 
    public void CalculateStartingScene(){
        /**
         * This determines if user has an account of if file is found
         */
        if(FileHelper.dataFileExists()){
            // If file cannot be read this will fail
            try{
                // Reading file into JSONObject
                JSONObject tTempObject = FileHelper.readJSONFile();

                // Getting instance of singleton class
                Account tAccount = Account.getInstance();

                // Setting account from JSON Object, now we can work with the account from anywhere.
                // Make sure this logic is set after user creates new account as well.
                tAccount.SetUserAccountFromJSONObject(tTempObject);

                tHomeScene.initialize();

                // If that works then set scene
                MainStage.getInstance().setScene(tHomeScene.getCurrentScene());
            }
            catch(Exception e){
                AlertBox.display("Error", "Error reading file, a new account will need to be made.");
                if(FileHelper.dataFileExists()){
                    // This fixes some how corrupted data, such as user editing data.json file
                    FileHelper.deleteFile();
                }
                e.printStackTrace();
                MainStage.getInstance().setScene(tNewScene);
            }
        }
        else{
            MainStage.getInstance().setScene(tNewScene);
        }
    }
 public static void main(String[] args) {
        launch(args);
    }
}