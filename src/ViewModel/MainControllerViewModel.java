package ViewModel;


import Intepeter.Parser;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MainControllerViewModel {
    public StringProperty autopilotText;
    public StringProperty ipPortText;

    public MainControllerViewModel(){
        autopilotText = new SimpleStringProperty();
        ipPortText = new SimpleStringProperty();
    }

    public void sendToParser(){
        Parser.getInstance().parse(autopilotText.get());
    }
    public void cennectToServer(){

    }
}
