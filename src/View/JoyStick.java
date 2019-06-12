package View;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class JoyStick extends Region {
    private Circle bigCircle;
    private Circle smallCircle;

    public void displayJoystick(){
        Platform.runLater(()-> {
            bigCircle = new Circle(getWidth() / 2, getHeight() / 2, getWidth() / 2, Paint.valueOf("#0000FF"));
            smallCircle = new Circle(getWidth() / 2, getHeight() / 2, getWidth() / 4, Paint.valueOf("#FF0000"));
            super.getChildren().add(bigCircle);
            super.getChildren().add(smallCircle);
        });
    }
}
