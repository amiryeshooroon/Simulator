package View;

import Exceptions.CantConnectToServerException;
import Exceptions.CodeErrorException;
import Exceptions.WrongLimitError;
import Notifications.DisableMap;
import Utilities.Properties.CompositeProperty;
import Utilities.Properties.MyProperty;
import ViewModel.MainControllerViewModel;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import sample.Main;
import javafx.scene.image.ImageView;
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
    @FXML
    private ImageView planeImage;
    @FXML
    private Button loadData;
    @FXML
    private Button calculatePath;
    private MainControllerViewModel vm;
    private CompositeProperty<Double> clickOnMapLocation;
    private StringProperty path;
    private MyProperty<Double> x,y;
    private AnimationTimer timer;
    private boolean isMapLoaded, isConnectedToSimulator, isConnectedToSolver;

    public void setViewModel(MainControllerViewModel vm){
        this.vm = vm;
        vm.throttle.bind(throttleSlider.valueProperty());
        vm.rudder.bind(rudderSlider.valueProperty());
        vm.autopilotText.bind(autoPilotCode.textProperty());
        path.bind(vm.path); //not sure if it's mvvm but ok
        Platform.runLater(()-> {
            try {
                vm.joyStick.bind(joyStick.getProperty());
            } catch (WrongLimitError ignored) {}
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
        else if(arg instanceof DisableMap){
            DisableMap disableMap = (DisableMap)arg;
            if(!disableMap.get()) mapDisplayer.drawPath(path.get());
            mapDisplayer.setDisable(disableMap.get());
        }
        else{
            Integer i = (Integer)arg;
            if(i.equals(1)){
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm");
                    alert.setHeaderText("Please confirm");
                    alert.setContentText("Please confirm us when the simulator is running.");
                    alert.showAndWait();
                    synchronized (arg) {
                        arg.notifyAll();
                    }
                });
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        joyStick.displayJoystick();
        autoPilotPane.setPrefWidth(400);
        clickOnMapLocation = new CompositeProperty<>(2);
        x = new MyProperty<>();
        y = new MyProperty<>();
        path = new SimpleStringProperty();
        isMapLoaded = false;
        isConnectedToSolver = false;
        isConnectedToSimulator = false;
        planeImage.setImage(new Image(getClass().getResource("plane.png").toString()));
        planeImage.setVisible(false);
        try {
            clickOnMapLocation.bind(new Pair<String, MyProperty<Double>>("x", x), new Pair<String, MyProperty<Double>>("y", y));
        } catch (WrongLimitError ignored) {}
    }

    public void onOpenFileClick(){
        double longtitude = 0, latitude = 0, area = 0;
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Main.stage);
        if(file!=null){//
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
                        area = new Scanner(str).useDelimiter(",").nextDouble();
                        i++;
                    }
                }
            } catch (FileNotFoundException ignored) {}
            mapDisplayer.displayMap(records, longtitude, latitude, area);
            isMapLoaded = true;
            if(isConnectedToSimulator) planeMover();
            loadData.setDisable(true);
            calculatePath.setDisable(false);
        }
    }

    public void onClickConnect(){
        boolean succeed = connector(true);
        if(succeed) {
            isConnectedToSimulator = true;
            if(isMapLoaded) planeMover();
        }

    }
    public void onClickCalculatePath(){
        if(connector(false)) {
            isConnectedToSolver = true;
            Pair<Integer, Integer> dstIndex = mapDisplayer.getClosestIndexes(mapDisplayer.getPrevXY(), mapDisplayer.getPrevXX()),
                    srcIndex = mapDisplayer.getClosestIndexes(mapDisplayer.getPlaneY(), mapDisplayer.getPlaneX());
            vm.findPath(mapDisplayer.getMap(), srcIndex, dstIndex);
            //new Thread(() -> mapDisplayer.drawPath(path.get())).start(); //need to be deleted
        }
    }

    private boolean connector(boolean flag){
        TextInputDialog dialog = new TextInputDialog("");
        if(vm != null) vm.ipPortText.bind(dialog.getEditor().textProperty());
        dialog.setTitle("Connect");
        dialog.setContentText("IP:Port");
        Optional<String> result = dialog.showAndWait();
        boolean succeed = false;
        if(vm != null) {
            if (result.isPresent()) {
                if (flag) succeed = vm.connectToSimulator();
                else succeed = vm.connectToSolver();
                return succeed;
            }
        }
        return false;
    }

    public void clickOnMap(MouseEvent mouseEvent) {
        if(!mapDisplayer.isDisable()) {
            x.set(mouseEvent.getX());
            y.set(mouseEvent.getY());
            mapDisplayer.drawX(mouseEvent.getX(), mouseEvent.getY());
            if (isConnectedToSolver) {
                Pair<Integer, Integer> dstIndex = mapDisplayer.getClosestIndexes(mapDisplayer.getPrevXY(), mapDisplayer.getPrevXX()),
                        srcIndex = mapDisplayer.getClosestIndexes(mapDisplayer.getPlaneY(), mapDisplayer.getPlaneX());
                vm.findPath(mapDisplayer.getMap(), srcIndex, dstIndex);
                mapDisplayer.drawPath(path.get()); //need to be deleted
            }
        }
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


    public void fly(ActionEvent actionEvent) {
        vm.sendToParser();
    }

    public void selectManual(ActionEvent actionEvent) {
        vm.stopAutoPilot();
        autoPilotPane.setDisable(true);
        joystickPane.setDisable(false);
    }

    public void selectAutoPilot(ActionEvent actionEvent) {
        autoPilotPane.setDisable(false);
        joystickPane.setDisable(true);
    }

    public void planeMover(){
        stopPlaneMoving();
        vm.startPositionThread();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
        }
        //need to test
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                mapDisplayer.displayAirplane(planeImage, vm.planeLong(), vm.planeLat());
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ignored) {}
            }
        };
        timer.start();
    }

    public void stopPlaneMoving(){
        if(timer != null) timer.stop();
    }

    public void finalize(){
        stopPlaneMoving();
    }
}
