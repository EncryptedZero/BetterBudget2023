package Graphical;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A class meant to make working with Alert Boxes easy
 */
public class AlertBox {
    
    public static void display(String pTitle, String pMessage){
        Stage mStage = new Stage();

        // Forces focus to this window
        mStage.initModality(Modality.APPLICATION_MODAL);
        mStage.setTitle(pTitle);
        mStage.setMinWidth(200);

        Label mLabelMessage = new Label(pMessage);

        Button mCloseButton = new Button("Close");
        mCloseButton.setOnAction(e -> mStage.close());

        VBox mVBox1 = new VBox(10);
        mVBox1.setPadding(new Insets(10, 5, 10, 5));
        
        mVBox1.getChildren().addAll(mLabelMessage, mCloseButton);
        mVBox1.setAlignment(Pos.CENTER);

        Scene mAlertScene = new Scene(mVBox1);
        mStage.setScene(mAlertScene);
        mStage.showAndWait();
    }
}
