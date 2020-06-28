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

import java.io.FileWriter;
import java.io.IOException;

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
        //writeToXmlFile();
    }


    private static void writeToXmlFile(){
        String ipAddress = "192.0.0.1";
        String port = "1234";
        String value = ipAddress + ":" + port;
        String propertyURL = "<property name=\"serviceUrl\" value=\"rmi://" + value + "/WhistGame\"/>";


        String finalString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "\n" +
                "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd\">\n" +
                "\n" +
                "    <bean id=\"appServer\" class=\"org.springframework.remoting.rmi.RmiProxyFactoryBean\">";

        finalString += propertyURL;

        finalString += "<property name=\"serviceInterface\" value=\"com.IServer\"/>\n" +
                "    </bean>\n" +
                "\n" +
                "</beans>";


        try {
            FileWriter writer = new FileWriter("C:\\GitProjects\\whistGame\\CardGame-Whist\\ClientFX\\src\\main\\resources\\spring-client.xml");
            writer.write(finalString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
