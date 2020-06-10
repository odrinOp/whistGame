import com.IServer;
import com.LobbyController;
import com.LoginController;
import com.MainController;
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
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/views/loginView.fxml"));
        Parent loginView = loginLoader.load();
        LoginController loginController = loginLoader.getController();


        FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/views/lobbyView.fxml"));
        Parent lobbyView = lobbyLoader.load();
        LobbyController lobbyController = lobbyLoader.getController();


        /*
        loginController.initData();
        loginController.setServer(server);
        loginController.setLobbyData(lobbyController,lobbyView);

        stage.setScene(new Scene(loginView));
        stage.show();
        */

        MainController mainController = new MainController();

        //mainController.setServer(server);
        mainController.setSceneManager(new SceneManager(stage));

        mainController.getSceneManager().addScene(ApplicationState.LOGIN,loginView);
        mainController.getSceneManager().addScene(ApplicationState.LOBBY,lobbyView);


        loginController.setMainController(mainController);
        lobbyController.setMainController(mainController);

        loginController.initData();

        mainController.setLobbyController(lobbyController);
        mainController.setLoginController(loginController);

        mainController.getSceneManager().changeActiveScene(ApplicationState.LOGIN);


        mainController.getSceneManager().show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
