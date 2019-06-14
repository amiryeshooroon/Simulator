package View;

import Intepeter.Parser;
import ViewModel.MainControllerViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import sample.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable, Observer {
    @FXML
    MapDisplayer mapDisplayer;
    @FXML
    JoyStick joyStick;
    @FXML
    TextArea autoPilotCode;
    StringProperty ipPort;
    MainControllerViewModel vm;
    public void setViewModel(MainControllerViewModel vm){
        this.vm = vm;
    }
    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ipPort = new SimpleStringProperty();
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

    public void onClickConnect(){
        TextInputDialog dialog = new TextInputDialog("");
        if(vm != null) vm.ipPortText.bind(dialog.getEditor().textProperty());
        dialog.setTitle("Connect");
        dialog.setContentText("IP:Port");
        Optional<String> result = dialog.showAndWait();

        if(vm != null && result.isPresent()) vm.connectToServer();
    }
}
