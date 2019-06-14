package Model;

import Search.Pair;

import java.util.List;

public interface SimulatorModel {
    void autoFly(String code);
    void calculatePath(List<List<Double>> map, Pair<Double, Double> start, Pair<Double, Double> end);
    void pathFly(List<List<Double>> map, Pair<Double, Double> start, Pair<Double, Double> end);
    void joystickFly(double angle, double radius, double maxRadius, double throttle, double rudder);
    void connect(String connect);
}
