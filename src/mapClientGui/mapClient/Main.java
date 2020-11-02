package mapClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class of the software.
 * 
 * @author Fabio
 *
 */
public class Main extends Application {
  /**
   * Ovverrides the start(Stage stageName) method in the Application class.
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
    Parent root = loader.load();
    primaryStage.getIcons().add(new Image("icon.png"));
    primaryStage.setScene(new Scene(root, 520, 300));
    primaryStage.setResizable(true);
    primaryStage.setTitle("Map Client");
    primaryStage.show();
    Controller controller = loader.getController();
    primaryStage.setOnCloseRequest(e -> controller.closeConnection());
  }

  /**
   * Software entry point.
   * 
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    launch(args);
  }
}