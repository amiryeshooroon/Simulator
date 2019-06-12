package View;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class JoyStick extends Pane {
    private Circle bigCircle;
    private Circle smallCircle;

    public void displayJoystick(){
        setWidth(200);
        setHeight(200);
        bigCircle = new Circle(getWidth() / 2, getHeight() / 2, getWidth() / 2, Paint.valueOf("#0000FF"));
        smallCircle = new Circle(getWidth() / 2, getHeight() / 2, getWidth() / 4, Paint.valueOf("#FF0000"));
        getChildren().add(bigCircle);
        getChildren().add(smallCircle);
    }
}
