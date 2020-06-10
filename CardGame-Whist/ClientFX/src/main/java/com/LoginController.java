package com;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class LoginController {

    private IServer server;

    private LobbyController lobbyController;
    private Parent lobbyView;

    //FXML BUTTONS
    @FXML
    private TextField nicknameTxt;

    @FXML
    void login(ActionEvent e){
        try {
            validateData();
            String nickname = nicknameTxt.getText();

            Player player = server.login(nickname,lobbyController);


            lobbyController.setServer(server);
            lobbyController.setPlayer(player);
            lobbyController.initState();

            Stage lobbyStage = new Stage();
            lobbyStage.setScene(new Scene(lobbyView));

            lobbyStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    try {
                        server.logout(player);
                        System.exit(0);
                    } catch (AppException appException) {
                        appException.printStackTrace();
                    }

                }
            });

            lobbyStage.show();

            ((Node)(e.getSource())).getScene().getWindow().hide();

        } catch (AppException appException) {
            appException.printStackTrace();
        }


    }



    private void validateData() throws AppException {
        String nickname = nicknameTxt.getText();
        if(nickname.length()< 4)
            throw new AppException("Your name must have at least 4 characters");
    }


    public void initData() {
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    public void setLobbyData(LobbyController lobbyController, Parent lobbyView) {
        this.lobbyController = lobbyController;
        this.lobbyView = lobbyView;
    }
}
