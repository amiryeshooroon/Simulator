package View;

import Utilities.Math.CircleLine;
import Utilities.MyT;
import Utilities.Math.Point;
import Utilities.Properties.CompositeProperty;
import Utilities.Properties.MyProperty;
import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.util.Timer;
import java.util.TimerTask;

public class JoyStick extends Region {
    private Circle bigCircle;
    private Circle smallCircle;
    private CompositeProperty<Double> property = new CompositeProperty<>(2);
    private MyProperty<Double> aileron, elevator;
    public void displayJoystick(){
        Platform.runLater(()-> {
            bigCircle = new Circle(getWidth() / 2, getHeight() / 2, getWidth() / 2, Paint.valueOf("#0000FF"));
            smallCircle = new Circle(getWidth() / 2, getHeight() / 2, getWidth() / 4, Paint.valueOf("#FF0000"));
            super.getChildren().add(bigCircle);
            super.getChildren().add(smallCircle);
            aileron = new MyProperty<>();
            elevator = new MyProperty<>();
            property.bind(new Pair<>("aileron", aileron), new Pair<>("elevator", elevator));
            smallCircle.setOnMouseDragged(event->{
                if(Math.hypot(Math.abs(event.getX() - bigCircle.getCenterX()), Math.abs(event.getY() - bigCircle.getCenterY())) > bigCircle.getRadius()){
                    Point p = CircleLine.getCircleLineIntersectionPoint(new Point(bigCircle.getCenterX(), bigCircle.getCenterY()), new Point(event.getX(), event.getY())
                            , new Point(bigCircle.getCenterX(), bigCircle.getCenterY()), bigCircle.getRadius());
                    smallCircle.setCenterX(p.getX());
                    smallCircle.setCenterY(p.getY());
                    aileron.set((p.getX() - bigCircle.getCenterX()) / bigCircle.getRadius());
                    elevator.set((p.getY() - bigCircle.getCenterY()) / bigCircle.getRadius());
                    return;
                }
                smallCircle.setCenterX(event.getX());
                smallCircle.setCenterY(event.getY());
                aileron.set((event.getX() - bigCircle.getCenterX()) / bigCircle.getRadius());
                elevator.set((event.getY() - bigCircle.getCenterY()) / bigCircle.getRadius());
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
                            aileron.set((x - bigCircle.getCenterX()) / bigCircle.getRadius());
                            elevator.set((y - bigCircle.getCenterY()) / bigCircle.getRadius());
                            i.setT(i.getT() + 1);
                            if(x == bigCircle.getCenterX() && y == bigCircle.getCenterY()) t.cancel();
                        }
                    }, 0, 3);
            });
        });
    }

    public CompositeProperty<Double> getProperty() {
        return property;
    }
}
