package com;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;


public class LoginController {




    private MainController mainController;

    //FXML CONTROLS
    @FXML
    private TextField nicknameTxt;
    @FXML
    private Label errorMessage;

    @FXML
    void login(ActionEvent e){
        try {
            initData();
            validateData();
            String nickname = nicknameTxt.getText();

            mainController.login(nickname);
            mainController.getSceneManager().setOnCloseRequest(new EventHandler() {
                @Override
                public void handle(Event event) {
                    try {
                        mainController.getServer().logout(mainController.getPlayer());
                    } catch (AppException appException) {
                        appException.printStackTrace();
                    }
                    System.exit(0);
                }
            });
            mainController.getSceneManager().setTitle(nickname);

        } catch (AppException | IOException appException) {
            errorMessage.setText(appException.getMessage());
            errorMessage.setVisible(true);


        }


    }



    private void validateData() throws AppException {
        String nickname = nicknameTxt.getText();
        if(nickname.length()< 4)
            throw new AppException("Your name must have at least 4 characters");
    }


    public void initData() {
        errorMessage.setVisible(false);

    }





    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }
}
