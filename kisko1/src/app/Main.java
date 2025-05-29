package app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml")); 
        
        Scene scene = new Scene(root, 674, 422); 
        
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene); 
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
