package com;

import com.dto.GameData;
import com.dto.LobbyData;
import com.utils.ApplicationState;
import com.utils.SceneManager;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MainController extends UnicastRemoteObject implements IClientObserver {
    private IServer server;
    private Player player;
    private SceneManager sceneManager;

    private LoginController loginController;
    private LobbyController lobbyController;





    public MainController() throws RemoteException {
    }

    public MainController(IServer server, Player player, Stage stage) throws RemoteException {
        super();
        this.server = server;
        this.player = player;
        this.sceneManager = new SceneManager(stage);
    }

    @Override
    public void update(GameData data) throws RemoteException {
        if(data instanceof LobbyData){
            lobbyController.update(data);
        }
    }


    public void login(String nickname) throws AppException {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
            server = (IServer) factory.getBean("appServer");
            this.player = server.login(nickname,this);
            this.lobbyController.initState();
            this.sceneManager.changeActiveScene(ApplicationState.LOBBY);
    }

    public void markAsReady() throws AppException {
        server.markAsActive(player);
        player.setReady(true);
    }


    public IServer getServer() {
        return server;
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public LobbyController getLobbyController() {
        return lobbyController;
    }

    public void setLobbyController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }





}
