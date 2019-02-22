package CGProject;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, (primaryScreenBounds.getWidth() / 1.4), (primaryScreenBounds.getHeight() / 1.4));
        Controller controller = loader.getController();
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case Q: controller.setTool(Tools.SELECT); break;
                    case W: controller.setTool(Tools.MOVE); break;
                    case E: controller.setTool(Tools.ROTATE); break;
                    case R: controller.setTool(Tools.SCALE); break;
                }
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("Vector Graphics Editor");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
