package View;

import Exceptions.CantConnectToServerException;
import Exceptions.CodeErrorException;
import Exceptions.WrongLimitError;
import Intepeter.Parser;
import Utilities.Properties.CompositeProperty;
import Utilities.Properties.MyProperty;
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
import javafx.util.Pair;
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
    @FXML
    private Pane autoPilotPane;
    private MainControllerViewModel vm;
    private CompositeProperty<Double> clickOnMapLocation;
    private MyProperty<Double> x,y;
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
        else if(arg instanceof CodeErrorException){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Code Error");
            alert.setContentText("Could not run code. Please check for errors.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        joyStick.displayJoystick();
        autoPilotPane.setPrefWidth(400);
        clickOnMapLocation = new CompositeProperty<>(2);
        x = new MyProperty<>();
        y = new MyProperty<>();
        try {
            clickOnMapLocation.bind(new Pair<String, MyProperty<Double>>("x", x), new Pair<String, MyProperty<Double>>("y", y));
        } catch (WrongLimitError wrongLimitError) {
        }
    }

    public void onOpenFileClick(){
        double longtitude = 0, latitude = 0, area = 0;
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Main.stage);
        if(file!=null){
            int i=0;
            List<List<Double>> records = new ArrayList<>();
            try (Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(file)))) {
                while (scanner.hasNextLine()) {
                    String str = scanner.nextLine();
                    if(i >= 2){
                        records.add(Arrays.stream(str.split(",")).map(Double::valueOf).collect(Collectors.toList()));
                        i++;
                    }
                    else if(i==0) {
                        List<Double> numbers = Arrays.stream(str.split(",")).map(Double::valueOf).collect(Collectors.toList());
                        longtitude = numbers.get(0);
                        latitude = numbers.get(1);
                        i++;
                    }
                    else if(i == 1){
                        area = Double.valueOf(str);
                        i++;
                    }
                }
            } catch (FileNotFoundException e) {}
            mapDisplayer.displayMap(records, longtitude, latitude, area);
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
        x.set(mouseEvent.getX());
        y.set(mouseEvent.getY());
        mapDisplayer.drawX(mouseEvent.getX(), mouseEvent.getY());
    }

    public void keyPressedPane(KeyEvent keyEvent) {


    }
    public void loadScript(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Main.stage);
        if(file!=null){
            StringBuilder sb = new StringBuilder();
            try (Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(file)))){
                while (scanner.hasNextLine()) sb.append(scanner.nextLine()).append("\n");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            autoPilotCode.setText(sb.toString());
        }
    }
    public void engine(ActionEvent actionEvent) {
        vm.engine();
    }

    public void fly(ActionEvent actionEvent) {
        vm.sendToParser();
    }

    public void selectManual(ActionEvent actionEvent) {
        autoPilotPane.setDisable(true);
        joystickPane.setDisable(false);
    }

    public void selectAutoPilot(ActionEvent actionEvent) {
        autoPilotPane.setDisable(false);
        joystickPane.setDisable(true);
    }
}
