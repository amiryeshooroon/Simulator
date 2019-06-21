package Model;

import Exceptions.CodeErrorException;
import Utilities.AutoPilot.Exceptions.NotConnectedToServerException;
import Utilities.AutoPilot.Intepeter.Parser;
import Search.Pair;
import Utilities.ServersUtilities.SimulatorServer;
import Utilities.ServersUtilities.SolverCommunicator;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MySimulatorModel extends Observable implements SimulatorModel {
    private String ip;
    private int port;
    private String path;
    private SolverCommunicator solver;
    private boolean stop;
    private AtomicReference<Double> longitude;
    private AtomicReference<Double> latitude;

    public MySimulatorModel() {
        solver = new SolverCommunicator();
        path = null;
    }

    @Override
    public void autoFly(String code) throws CodeErrorException {
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
        //What to do in the situations:
        //Up:
        //Down:
        //Left:
        //Right:
        //it depends let put index for each case Up will be 3 Down will be 4 Left will be 2 Right will be 1
        //now we need to build a function, we can assume that we won't get up and than down or the opposite same thing
        //about right and left so what we going to do?
        //0 to 1 90
        //0 to 3 -90
        //1 to 2 90
        //1 to 0 -90
        //2 to 3 90
        //2 to 1 -90
        //3 to 0 90
        //3 to 2 -90
        //(where-from)*90
        //if(from > where) (-1)^(1+
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
        try {
            SimulatorServer.getServer().setVariable("/controls/engines/current-engine/throttle", value);
        } catch (NotConnectedToServerException e) {
            notifyObservers();
        }
    }

    @Override
    public void setRudder(double value) {
        try {
            SimulatorServer.getServer().setVariable("/controls/flight/rudder", value);
        } catch (NotConnectedToServerException e) {
            notifyObservers(e);
        }

    }
    @Override
    public void joystickFly(double aileron, double elevator) {
        try{
            SimulatorServer.getServer().setVariable("/controls/flight/aileron", aileron);
            SimulatorServer.getServer().setVariable("/controls/flight/elevator", elevator);
        }catch (NotConnectedToServerException e) {
            notifyObservers(e);
        }
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
    @Override
    public void engine(){
        try {
            SimulatorServer.getServer().setVariable("/controls/engines/engine/magnetos", 3);
            SimulatorServer.getServer().setVariable("/controls/switches/starter", true);
        } catch (NotConnectedToServerException e) {
            //e.printStackTrace();
        }
    }
    public void startPosotionsThread(){
        stop = false;
        latitude = new AtomicReference<>();
        longitude = new AtomicReference<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!stop){
                    try {
                        longitude.set(SimulatorServer.getServer().getVariable("position/longitude-deg"));
                        latitude.set(SimulatorServer.getServer().getVariable("position/latitude-deg"));
                        Thread.sleep(250);
                    } catch (NotConnectedToServerException | InterruptedException e) {}
                }
            }
        }).start();
    }
    public void stopPositionsThread(){
        stop = true;
    }
    public double getLatitude(){
        return latitude.get();
    }
    public double getLongitude(){
        return longitude.get();
    }
}
