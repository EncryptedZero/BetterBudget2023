package Stages;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainStage {
    
    private MainStage mMainStageSingltonVar = MainStage.getInstance();
    private Stage mStage;

    private static final MainStage INSTANCE = new MainStage();

    private MainStage(){}

    public static synchronized MainStage getInstance() {
        return INSTANCE;
    }

    public void setStage(Stage pStage){
        this.mStage = pStage;
        this.mStage.setTitle("Better Budget");
    }

    public void setScene(Scene pScene){
        this.mStage.setScene(pScene);
    }

    public void show(){
        this.mStage.show();
    }

    public void closeStage(){
        this.mStage.close();
    }
}
