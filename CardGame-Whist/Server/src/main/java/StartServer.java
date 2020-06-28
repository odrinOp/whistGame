import com.FX.ServerController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartServer extends Application {
    public static void main(String[] args) {
        //ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-server.xml");
        //System.out.println("Server is running");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/serverView.fxml"));
        Parent parent = loader.load();
        ServerController controller = loader.getController();

        controller.initState();

        stage.setScene(new Scene(parent));
        stage.setOnCloseRequest(windowEvent -> {
            System.exit(0);
        });
        stage.show();

    }
}
