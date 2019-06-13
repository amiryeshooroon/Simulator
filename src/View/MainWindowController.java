package View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import sample.Main;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable, Observer {
    @FXML
    MapDisplayer mapDisplayer;
    @FXML
    JoyStick joyStick;

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapDisplayer.displayMap(Arrays.asList(Arrays.asList(1.0,1.0,1.0), Arrays.asList(1.0,1.0,1.0), Arrays.asList(1.0,1.0,1.0)));
        joyStick.displayJoystick();
    }

    public void onOpenFileClick(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Main.stage);

    }
}
