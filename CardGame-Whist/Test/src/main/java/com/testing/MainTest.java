package com.testing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/testView.fxml"));
        Parent p = loader.load();

        TestController controller = loader.getController();
        controller.initState();
        stage.setTitle("Testing GUI");
        stage.setScene(new Scene(p));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
