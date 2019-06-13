package View;

import Utilities.CircleLine;
import Utilities.MyT;
import Utilities.Point;
import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import java.util.Timer;
import java.util.TimerTask;

public class JoyStick extends Region {
    private Circle bigCircle;
    private Circle smallCircle;

    public void displayJoystick(){
        Platform.runLater(()-> {
            bigCircle = new Circle(getWidth() / 2, getHeight() / 2, getWidth() / 2, Paint.valueOf("#0000FF"));
            smallCircle = new Circle(getWidth() / 2, getHeight() / 2, getWidth() / 4, Paint.valueOf("#FF0000"));
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
                Point p;
                if(Math.hypot(Math.abs(event.getX() - bigCircle.getCenterX()), Math.abs(event.getY() - bigCircle.getCenterY())) > bigCircle.getRadius()) {
                    p = CircleLine.getCircleLineIntersectionPoint(new Point(bigCircle.getCenterX(), bigCircle.getCenterY()), new Point(event.getX(), event.getY())
                            , new Point(bigCircle.getCenterX(), bigCircle.getCenterY()), bigCircle.getRadius());
                }
                else p = new Point(event.getX(), event.getY());
                    Timer t = new Timer();
                    MyT<Integer> i = new MyT<>(0);
                    t.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            double x = (bigCircle.getCenterX() * i.getT() + p.getX() * (100 - i.getT())) / 100;
                            double y = (bigCircle.getCenterY() * i.getT() + p.getY() * (100 - i.getT())) / 100;
                            smallCircle.setCenterX(x);
                            smallCircle.setCenterY(y);
                            i.setT(i.getT() + 1);
                            if(x == bigCircle.getCenterX() && y == bigCircle.getCenterY()) t.cancel();
                        }
                    }, 0, 3);
            });
        });
    }
}
