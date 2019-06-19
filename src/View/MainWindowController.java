package View;

import Exceptions.CantConnectToServerException;
import Exceptions.WrongLimitError;
import Intepeter.Parser;
import ViewModel.MainControllerViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import sample.Main;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable, Observer {
    @FXML
    private MapDisplayer mapDisplayer;
    @FXML
    private JoyStick joyStick;
    @FXML
    private TextArea autoPilotCode;
    @FXML
    private Slider throttleSlider;
    @FXML
    private Slider rudderSlider;
    @FXML
    private Pane joystickPane;
    private MainControllerViewModel vm;
    private KeyCombination up, down, left, right;
    private Timer tUp, tDown, tLeft, tRight;
    public void setViewModel(MainControllerViewModel vm){
        this.vm = vm;
        vm.throttle.bind(throttleSlider.valueProperty());
        vm.rudder.bind(rudderSlider.valueProperty());
        Platform.runLater(()-> {
            try {
                vm.joyStick.bind(joyStick.getProperty());
            } catch (WrongLimitError wrongLimitError) {
            }
        });
    }
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof CantConnectToServerException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong IP/port");
            alert.setContentText("Please try to change your input and try again.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        joyStick.displayJoystick();
        up = new KeyCodeCombination(KeyCode.W);
        down = new KeyCodeCombination(KeyCode.S);
        left = new KeyCodeCombination(KeyCode.A);
        right = new KeyCodeCombination(KeyCode.D);
    }

    public void onOpenFileClick(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Main.stage);
        if(file!=null){
            int i=0;
            List<List<Double>> records = new ArrayList<>();
            try (Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(file)))) {
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

    public void clickOnMap(MouseEvent mouseEvent) {
        System.out.println("("+mouseEvent.getX()+","+mouseEvent.getY()+")");
    }

    public void keyPressedPane(KeyEvent keyEvent) {
        if(up.match(keyEvent)) throttleSlider.setValue(throttleSlider.getValue()+0.05);
        if(down.match(keyEvent)) throttleSlider.setValue(throttleSlider.getValue()-0.05);
        if(left.match(keyEvent)) rudderSlider.setValue(rudderSlider.getValue()-0.05);
        if(right.match(keyEvent)) rudderSlider.setValue(rudderSlider.getValue()+0.05);

    }

    public void engine(ActionEvent actionEvent) {
        vm.engine();
    }

    public void fly(ActionEvent actionEvent) {
    }

    public void selectManual(ActionEvent actionEvent) {
        autoPilotCode.setDisable(true);
        joystickPane.setDisable(false);
    }

    public void selectAutoPilot(ActionEvent actionEvent) {
        autoPilotCode.setDisable(false);
        joystickPane.setDisable(true);
    }
}
