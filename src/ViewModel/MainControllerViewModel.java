package ViewModel;


import Exceptions.CantConnectToServerException;
import Intepeter.Parser;
import Model.MySimulatorModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MainControllerViewModel extends Observable implements Observer {
    MySimulatorModel simulatorModel;
    public StringProperty autopilotText;
    public StringProperty ipPortText;

    public MainControllerViewModel(){
        simulatorModel = new MySimulatorModel();
        autopilotText = new SimpleStringProperty();
        ipPortText = new SimpleStringProperty();
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
