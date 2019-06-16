package View;

import Exceptions.CantConnectToServerException;
import Intepeter.Parser;
import ViewModel.MainControllerViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
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
    @FXML
    Slider throttleSlider;
    @FXML
    Slider rudderSlider;
    MainControllerViewModel vm;

    public void setViewModel(MainControllerViewModel vm){
        this.vm = vm;
        vm.throttle.bind(throttleSlider.valueProperty());
        vm.rudder.bind(rudderSlider.valueProperty());
    }
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof CantConnectToServerException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong IP/port");
            alert.setContentText("Please try change your input and try again.");
            alert.showAndWait();
        }
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
            } catch (FileNotFoundException e) {}
            mapDisplayer.displayMap(records);
        }
    }

    public void onClickConnect(){
        connector(true);
    }
    public void onClickCalculatePath(){
        connector(false);
    }

    private void connector(boolean flag){
        TextInputDialog dialog = new TextInputDialog("");
        if(vm != null) vm.ipPortText.bind(dialog.getEditor().textProperty());
        dialog.setTitle("Connect");
        dialog.setContentText("IP:Port");
        Optional<String> result = dialog.showAndWait();

        if(vm != null)
            if(flag) vm.connectToSimulator();
            else vm.connectToSolver();
    }

    public void dragThrottleSlider() {
        //System.out.println(throttleSlider.getValue());
    }

    public void dragRudderSlider(MouseEvent mouseEvent) {
      //  System.out.println(rudderSlider.getValue());

    }
}
