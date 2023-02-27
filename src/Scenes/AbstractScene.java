package Scenes;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * An abstract class to represent properties all scenes should have.
 */
public abstract class AbstractScene{

    private Scene mCurrentScene;
    
    private Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    private static AbstractScene instance;

    protected AbstractScene() {
        // Prevent external instantiation
    }

    public static synchronized AbstractScene getInstance() {
        if (instance == null) {
            // Replace with the concrete subclass you want to instantiate
            instance = new HomeScene();
        }
        return instance;
    }

    public void setCurrentScene(Scene pCurrentScene){
        this.mCurrentScene = pCurrentScene;
    }

    public Scene getCurrentScene(){
        return this.mCurrentScene;
    }

    public abstract Stage initialize(Stage pStage);

    protected double getScreenHeight(){
        return screenBounds.getHeight();
    }

    protected double getScreenWidth(){
        return screenBounds.getWidth();
    }
}
