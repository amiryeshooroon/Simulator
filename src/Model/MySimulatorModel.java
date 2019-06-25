package Model;

import Exceptions.CodeErrorException;
import Utilities.AutoPilot.Exceptions.NotConnectedToServerException;
import Utilities.AutoPilot.Intepeter.Parser;
import javafx.util.Pair;
import Utilities.MyT;
import Utilities.ServersUtilities.SimulatorServer;
import Utilities.ServersUtilities.SolverCommunicator;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MySimulatorModel extends Observable implements SimulatorModel {
    private String ip;
    private int port;
    private String path;
    private SolverCommunicator solver;
    private volatile boolean stop;
    private AtomicReference<Double> longitude;
    private AtomicReference<Double> latitude;
    private volatile boolean isAutoPilotOn;
    private CompletableFuture<Boolean> f;

    public MySimulatorModel() {
        solver = new SolverCommunicator();
        path = null;
        isAutoPilotOn = false;
    }
    public void setModelChanged(){
        setChanged();
    }

    @Override
    public void autoFly(String code)  {
        f=CompletableFuture.supplyAsync(()-> {
            try {
                Parser.getInstance().parse(code, this);
                isAutoPilotOn = true;
            } catch (CodeErrorException e) {
                return false;
            } catch (CancellationException e){
            }
            return true;
        });
        f.thenAccept(b -> { //
            if(!b) notifyObservers(new CodeErrorException());
        });
        //get airplaneX and airplaneY every some sec for the map and update, optional
    }

    public SolverCommunicator getSolver() {
        return solver;
    }

    @Override
    public void calculatePath(List<List<Double>> map, Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
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
    public String getPath(){
        return path;
    }
    @Override
    public void pathFly(List<List<Double>> map, Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
        if(path == null){
            calculatePath(map, start, end);
        }
    }

    @Override
    public void setThrottle(double value) {
        try {
            SimulatorServer.getServer().setVariable("/controls/engines/current-engine/throttle", value);
        } catch (NotConnectedToServerException e) {
            notifyObservers(e);
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
    public void startPosotionsThread(Object obj){
        stop = false;
        latitude = new AtomicReference<>();
        longitude = new AtomicReference<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean sentOnce = false;
                while(!stop){
                    try {
                        longitude.set(SimulatorServer.getServer().getVariable("position/longitude-deg"));
                        latitude.set(SimulatorServer.getServer().getVariable("position/latitude-deg"));
                        if(!sentOnce){
                            synchronized (obj){
                                obj.notifyAll();
                            }
                            sentOnce = true;
                        }
                        if(stop) break;
                        Thread.sleep(250);
                    } catch (NotConnectedToServerException | InterruptedException ignored) {}
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
    public void stopAutoPilot(){
        if(isAutoPilotOn) f.cancel(true);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        stopAutoPilot();
        stopPositionsThread();
    }
}
