package ViewModel;


import Intepeter.Parser;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Observable;
import java.util.Observer;

public class MainControllerViewModel extends Observable implements Observer {
    public StringProperty autopilotText;
    public StringProperty ipPortText;

    public MainControllerViewModel(){
        autopilotText = new SimpleStringProperty();
        ipPortText = new SimpleStringProperty();
    }

    public void sendToParser(){

    }
    public void cennectToServer(){

    }

    @Override
    public void update(java.util.Observable o, Object arg) {

    }
}
