package View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import sample.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
        joyStick.displayJoystick();
    }

    public void onOpenFileClick(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Main.stage);
        if(file!=null){
            int i=0;
            List<List<Double>> records = new ArrayList<>();
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String str = scanner.nextLine();
                    if(i==0 || i==1) {
                        i++; continue;
                    }
                    records.add(Arrays.stream(str.split(",")).map(Double::valueOf).collect(Collectors.toList()));
                    i++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mapDisplayer.displayMap(records);
        }

    }
}
