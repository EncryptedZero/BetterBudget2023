package Graphical;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A class meant to make working with Comfirm Boxes easy
 */
public class ConfirmBox {

    public static boolean Confirmed;
    
    public static void display(String pTitle, String pMessage){
        Stage mStage = new Stage();

        // Forces focus to this window
        mStage.initModality(Modality.APPLICATION_MODAL);
        mStage.setTitle(pTitle);
        mStage.setMinWidth(200);

        Label mLabelMessage = new Label(pMessage);

        Button mYesButton = new Button("Yes");
        mYesButton.setOnAction(e -> {
            Confirmed = true;
            mStage.close();
        });
        Button mNoButton = new Button("No");
        mNoButton.setOnAction(e -> {
            Confirmed = false;
            mStage.close();
        });

        VBox mVBox1 = new VBox(10);
        HBox mHBox = new HBox(10);
        mHBox.getChildren().addAll(mYesButton, mNoButton);
        mVBox1.getChildren().addAll(mLabelMessage, mHBox);
        mVBox1.setAlignment(Pos.CENTER);

        Scene mAlertScene = new Scene(mVBox1);
        mStage.setScene(mAlertScene);
        mStage.showAndWait();
    }
}
