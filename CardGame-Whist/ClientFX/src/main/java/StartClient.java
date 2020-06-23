import com.*;
import com.utils.ApplicationState;
import com.utils.SceneManager;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartClient extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        //initialize views and controllers
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/views/loginView.fxml"));
        Parent loginView = loginLoader.load();
        LoginController loginController = loginLoader.getController();


        FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/views/lobbyView.fxml"));
        Parent lobbyView = lobbyLoader.load();
        LobbyController lobbyController = lobbyLoader.getController();

        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/views/gameView.fxml"));
        Parent gameView = gameLoader.load();
        GameController gameController = gameLoader.getController();


        MainController mainController = new MainController();


        //adding the scenes for application
        mainController.setSceneManager(new SceneManager(stage));
        mainController.getSceneManager().addScene(ApplicationState.LOGIN,loginView);
        mainController.getSceneManager().addScene(ApplicationState.LOBBY,lobbyView);
        mainController.getSceneManager().addScene(ApplicationState.GAME,gameView);


        //configure controllers
        loginController.setMainController(mainController);
        lobbyController.setMainController(mainController);
        gameController.setMainController(mainController);

        //initialize controllers start state
        loginController.initData();




        //initialize mainController
        mainController.setLobbyController(lobbyController);
        mainController.setLoginController(loginController);
        mainController.setGameController(gameController);



        //show window
        mainController.getSceneManager().changeActiveScene(ApplicationState.LOGIN);

        mainController.getSceneManager().show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
