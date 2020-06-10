import com.IServer;
import com.LobbyController;
import com.LoginController;
import javafx.application.Application;
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


        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
        IServer server = (IServer) factory.getBean("appServer");

        loginController.initData();
        loginController.setServer(server);
        loginController.setLobbyData(lobbyController,lobbyView);

        stage.setScene(new Scene(loginView));
        stage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
