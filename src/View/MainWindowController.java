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
        List<List<Double>> records = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Main.stage);
        if(file!=null){
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String str = scanner.nextLine();
                    records.add(Arrays.stream(str.split(",")).map(Double::valueOf).collect(Collectors.toList()));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        records.forEach(lst->{
            lst.forEach(System.out::print);
            System.out.println();
        });
    }
}
