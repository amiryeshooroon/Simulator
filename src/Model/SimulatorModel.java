package Model;

import Exceptions.CodeErrorException;
import javafx.util.Pair;
import java.io.IOException;
import java.util.List;

public interface SimulatorModel {
    void autoFly(String code) throws Exception;
    void calculatePath(List<List<Double>> map, Pair<Integer, Integer> start, Pair<Integer, Integer> end);
    void pathFly(List<List<Double>> map, Pair<Integer, Integer> start, Pair<Integer, Integer> end);
    void joystickFly(double aileron, double elevator);
    void setThrottle(double value);
    void setRudder(double value);
    void connect(String connect, boolean flag) throws Exception; //true = connect to simulator, false = path calculate
    void engine();
}
