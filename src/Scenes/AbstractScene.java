package Scenes;

import javafx.scene.Scene;

/**
 * An abstract class to represent properties all scenes should have.
 */
public abstract class AbstractScene{

    private Scene mNextScene, mCurrentScene;

    public AbstractScene(Scene pNextScene){
        this.mNextScene = pNextScene;
    }

    public AbstractScene(){
        // Do nothing
    }

    public void setNextScene(Scene pNextScene){
        this.mNextScene = pNextScene;
    }

    public Scene getNextScene(){
        return this.mNextScene;
    }

    public void setCurrentScene(Scene pCurrentScene){
        this.mCurrentScene = pCurrentScene;
    }

    public Scene getCurrentScene(){
        return this.mCurrentScene;
    }

    protected abstract void initialize();
}
