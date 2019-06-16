package ViewModel;


import Exceptions.CantConnectToServerException;
import Intepeter.Parser;
import Model.MySimulatorModel;
import Utilities.Properties.CompositeProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class MainControllerViewModel extends Observable implements Observer {
    MySimulatorModel simulatorModel;
    public StringProperty autopilotText;
    public StringProperty ipPortText;
    public DoubleProperty throttle, rudder;
    public CompositeProperty<Double> joyStick;
    public MainControllerViewModel(){
        simulatorModel = new MySimulatorModel();
        autopilotText = new SimpleStringProperty();
        ipPortText = new SimpleStringProperty();
        throttle = new SimpleDoubleProperty();
        joyStick = new CompositeProperty<>(2);
        throttle.addListener(
                (observable, oldValue, newValue) -> simulatorModel.setThrottle(newValue.doubleValue())
        );
        rudder = new SimpleDoubleProperty();
        rudder.addListener(
                (observable, oldValue, newValue) -> simulatorModel.setRudder(newValue.doubleValue())
        );
    }

    public void sendToParser(){
        simulatorModel.autoFly(autopilotText.get());
    }
    public void connectToSimulator(){
        try {
            simulatorModel.connect(ipPortText.get(), true);
        } catch (Exception e) {
            setChanged();
            notifyObservers(new CantConnectToServerException());
        }
    }
    public void connectToSolver(){
        try {
            simulatorModel.connect(ipPortText.get(), false);
        } catch (Exception e) {
            setChanged();
            notifyObservers(new CantConnectToServerException());
        }
    }

    @Override
    public void update(java.util.Observable o, Object arg) {

    }
}
