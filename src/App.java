
import org.json.simple.JSONObject;

import Graphical.AlertBox;
import Helper.FileHelper;
import Scenes.HomeScene;
import Scenes.LoginScene;
import Scenes.SignUpScene;
import Stages.MainStage;
import User.Users;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class App extends Application {
    SignUpScene mSignUpScene = SignUpScene.getInstance();
    Scene signupScene;
    HomeScene tHomeScene = HomeScene.getInstance();

    @Override
    public void start(Stage pStage) {
        MainStage.getInstance().setStage(pStage);
        mSignUpScene.initialize();
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

                Users.getInstance().fromJSONObject(tTempObject);

                LoginScene loginScene = LoginScene.getInstance();
                loginScene.initialize();

                // If that works then set scene
                MainStage.getInstance().setScene(loginScene.getCurrentScene());
            }
            catch(Exception e){
                AlertBox.display("Error", "Error reading file, a new account will need to be made.");
                if(FileHelper.dataFileExists()){
                    // This fixes some how corrupted data, such as user editing data.json file
                    FileHelper.deleteFile();
                }
                e.printStackTrace();
                MainStage.getInstance().setScene(mSignUpScene.getCurrentScene());
            }
        }
        else{
            MainStage.getInstance().setScene(mSignUpScene.getCurrentScene());
        }
    }
 public static void main(String[] args) {
        launch(args);
    }
}