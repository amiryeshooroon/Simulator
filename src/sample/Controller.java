package sample;

import View.JoyStick;
import View.MapDisplayer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    MapDisplayer mapDisplayer;
    @FXML
    JoyStick joyStick;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapDisplayer.displayMap(Arrays.asList(Arrays.asList(1.0,1.0,1.0), Arrays.asList(1.0,1.0,1.0), Arrays.asList(1.0,1.0,1.0)));
        joyStick.displayJoystick();
    }
}
