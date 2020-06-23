package com.testingFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/testFX.fxml"));

        Parent p = loader.load();
        TestingFXController controller = loader.getController();

        stage.setScene(new Scene(p));
        controller.testCards();

        stage.show();


    }
}
