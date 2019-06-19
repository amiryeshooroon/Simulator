package sample;

import Model.MySimulatorModel;
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
    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            stage = primaryStage;
            MySimulatorModel m = new MySimulatorModel();
            MainControllerViewModel vm = new MainControllerViewModel();
            m.addObserver(vm);
            FXMLLoader fxl = new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("sample.fxml").openStream());
            //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            MainWindowController mwc = fxl.getController();
            vm.addObserver(mwc);
            mwc.setViewModel(vm);
            primaryStage.setTitle("Simulator");
            primaryStage.setScene(new Scene(root, 1150, 375));
            primaryStage.show();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
