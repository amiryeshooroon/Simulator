package sample;

import Model.MySimulatorModel;
import Model.SimulatorModel;
import View.MainWindowController;
import ViewModel.MainControllerViewModel;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage stage;
    public MySimulatorModel m;
    @Override
    public void start(Stage primaryStage) throws Exception{
            stage = primaryStage;
            m = new MySimulatorModel();
            MainControllerViewModel vm = new MainControllerViewModel();
            m.addObserver(vm);
            vm.setModel(m);
            FXMLLoader fxl = new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("sample.fxml").openStream());
            //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            MainWindowController mwc = fxl.getController();
            vm.addObserver(mwc);
            mwc.setViewModel(vm);
            primaryStage.setTitle("Simulator");
            primaryStage.setScene(new Scene(root, 1150, 450));
            primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        m.getSolver().disconnect();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
