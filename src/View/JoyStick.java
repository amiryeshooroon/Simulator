package View;

import Utilities.CircleLine;
import Utilities.Point;
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
            smallCircle = new Circle(getWidth() / 2, getHeight() / 2, getWidth() / 8, Paint.valueOf("#FF0000"));
            super.getChildren().add(bigCircle);
            super.getChildren().add(smallCircle);
            smallCircle.setOnMouseDragged(event->{
                if(Math.hypot(Math.abs(event.getX() - bigCircle.getCenterX()), Math.abs(event.getY() - bigCircle.getCenterY())) > bigCircle.getRadius()){
                    Point p = CircleLine.getCircleLineIntersectionPoint(new Point(bigCircle.getCenterX(), bigCircle.getCenterY()), new Point(event.getX(), event.getY())
                            , new Point(bigCircle.getCenterX(), bigCircle.getCenterY()), bigCircle.getRadius());
                    smallCircle.setCenterX(p.getX());
                    smallCircle.setCenterY(p.getY());
                    return;
                }
                smallCircle.setCenterX(event.getX());
                smallCircle.setCenterY(event.getY());
            });
            smallCircle.setOnMouseReleased(event->{
                Point p = null;
                if(Math.hypot(Math.abs(event.getX() - bigCircle.getCenterX()), Math.abs(event.getY() - bigCircle.getCenterY())) > bigCircle.getRadius()) {
                    p = CircleLine.getCircleLineIntersectionPoint(new Point(bigCircle.getCenterX(), bigCircle.getCenterY()), new Point(event.getX(), event.getY())
                            , new Point(bigCircle.getCenterX(), bigCircle.getCenterY()), bigCircle.getRadius());
                }
                else p = new Point(event.getX(), event.getY());
                for(int i=1;i<=9999999;i++) {
                    double x = (bigCircle.getCenterX() * i + p.getX() * (9999999-i)) / 9999999;
                    double y = (bigCircle.getCenterY() * i + p.getY() * (9999999-i)) / 9999999;
                    smallCircle.setCenterX(x);
                    smallCircle.setCenterY(y);
                }
                smallCircle.setCenterX(bigCircle.getCenterX());
                smallCircle.setCenterY(bigCircle.getCenterY());
            });
        });
    }
}
