package ViewModel;


import Exceptions.CantConnectToServerException;
import Exceptions.CodeErrorException;
import Exceptions.UpdateTypes;
import Notifications.DisableMap;
import Notifications.DisplayToConsole;
import Notifications.EndCalculatePath;
import Utilities.AutoPilot.Intepeter.Parser;
import Model.MySimulatorModel;
import Utilities.Properties.CompositeProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Pair;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.concurrent.Executor;

public class MainControllerViewModel extends Observable implements Observer {
    MySimulatorModel simulatorModel;
    public StringProperty autopilotText;
    public StringProperty ipPortText;
    public StringProperty consoleText;
    public DoubleProperty throttle, rudder;
    public CompositeProperty<Double> joyStick;
    public CompositeProperty<Double> clickOnMapLocation;
    public StringProperty path;
    public MainControllerViewModel(){
        autopilotText = new SimpleStringProperty();
        ipPortText = new SimpleStringProperty();
        throttle = new SimpleDoubleProperty();
        path = new SimpleStringProperty();
        consoleText = new SimpleStringProperty();
        joyStick = new CompositeProperty<>(2);
        joyStick.setOnUpdate(()->
                {
                    double aileron = joyStick.get("aileron");
                    double elevator = joyStick.get("elevator");
                    simulatorModel.joystickFly(aileron, elevator);
                }
        );
        throttle.addListener(
                (observable, oldValue, newValue) -> simulatorModel.setThrottle(newValue.doubleValue())
        );
        rudder = new SimpleDoubleProperty();
        rudder.addListener(
                (observable, oldValue, newValue) -> simulatorModel.setRudder(newValue.doubleValue())
        );
        clickOnMapLocation = new CompositeProperty<>(2);
    }
    public void setModel(MySimulatorModel m){
        simulatorModel = m;
    }
    public void sendToParser(){
        simulatorModel.autoFly(autopilotText.get());
    }

    public boolean connectToSimulator(){
        try {
            simulatorModel.connect(ipPortText.get(), true);
            return true;
        } catch (Exception e) {
            setChanged();
            notifyObservers(new CantConnectToServerException());
            return false;
        }
    }
    public boolean connectToSolver(){
        try {
            simulatorModel.connect(ipPortText.get(), false);
            return true;
        } catch (Exception e) {
            setChanged();
            notifyObservers(new CantConnectToServerException());
            return false;
            //can do connected if success like simulatorModel notifies viewModel and it notifies view and popup connected
        }
    }
    @Override
    public void update(java.util.Observable o, Object arg) {
        if(arg instanceof EndCalculatePath){
            path.set(simulatorModel.getPath());
            setChanged();
            notifyObservers(new DisableMap(false));
        }
        else if(arg instanceof DisplayToConsole){
            if(consoleText.getValue() != null) consoleText.set(consoleText.getValue() + ((DisplayToConsole) arg).getMsg() + "\n");
            else consoleText.set(((DisplayToConsole) arg).getMsg() + "\n");
            setChanged();
            notifyObservers(arg);
        }
        else{
            setChanged();
            notifyObservers(arg);
        }
    }

    public double planeLong(){
        return simulatorModel.getLongitude(); //fast
    }

    public double planeLat(){
        return simulatorModel.getLatitude(); //fast
    }

    public void stopAutoPilot(){
        simulatorModel.stopAutoPilot();
    }

    public void startPositionThread(){
        Object obj = new Object();
        simulatorModel.stopPositionsThread();
        simulatorModel.startPosotionsThread(obj);
        synchronized (obj) {
            try {
                obj.wait();
            } catch (InterruptedException e){}
        }
    }
    public void findPath(List<List<Double>> map, Pair<Integer, Integer> start, Pair<Integer, Integer> end){
        simulatorModel.calculatePath(map, start, end); //check if here put thread or there, I really don't care...
        setChanged();
        notifyObservers(new DisableMap(true));
        //path.set(simulatorModel.getPath()); //need to be deleted
    }
    public void stopPositionThread(){
        simulatorModel.stopPositionsThread();
    }
}
