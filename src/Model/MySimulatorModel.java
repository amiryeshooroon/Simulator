package Model;

import Intepeter.Parser;
import Search.Pair;
import Utilities.SimulatorServer;
import Utilities.SolverCommunicator;
import java.io.IOException;
import java.util.List;
import java.util.Observable;

public class MySimulatorModel extends Observable implements SimulatorModel {
    private double airplaneX;
    private double airplaneY;
    private String ip;
    private int port;
    private String path;
    private SolverCommunicator solver;

    public MySimulatorModel() {
        solver = new SolverCommunicator();
        path = null;
    }

    @Override
    public void autoFly(String code) {
        Parser.getInstance().parse(code);
        //get airplaneX and airplaneY every some sec for the map and update, optional
    }

    @Override
    public void calculatePath(List<List<Double>> map, Pair<Double, Double> start, Pair<Double, Double> end) {
        //can maybe announce on label that we calculating
        for (List<Double> doubles : map) {
            for (int j = 0; j < doubles.size() - 1; j++) {
                solver.sendLineWithNoLineSeperator(doubles.get(j) + ",");
            }
            solver.sendLineWithLineSeperator(doubles.get(doubles.size() - 1).toString());
        }
        solver.sendLineWithLineSeperator("end");
        solver.sendLineWithLineSeperator(start.getKey().toString() + "," + start.getValue().toString());
        solver.sendLineWithLineSeperator(end.getKey().toString() + "," + end.getValue().toString());
        solver.flushOutput();
        path = solver.readLine();
    }

    @Override
    public void pathFly(List<List<Double>> map, Pair<Double, Double> start, Pair<Double, Double> end) {
        if(path == null){
            calculatePath(map, start, end);
        }
        //thread that move according to the string we got
        //thread gets the location every 500ms can be in timer and then updated airplaneX, airplaneY they can be i,j (airplaneX for j and airplaneY for i)
    }

    @Override
    public void setThrottle(double value) {
        System.out.println(value);
    }

    @Override
    public void setRudder(double value) {
        System.out.println(value);
    }

    @Override
    public void joystickFly(double angle, double radius, double maxRadius, double throttle, double rudder) {

    }

    @Override
    public void connect(String connect, boolean flag) throws Exception {
        String[] ipPort = connect.split(":");
        ip = ipPort[0];
        port = Integer.valueOf(ipPort[1]);
        //connect to simulator
        if(flag) SimulatorServer.getServer().open(ip, port);
        //connect to solver
        else solver.connect(ip, port);
    }
}
