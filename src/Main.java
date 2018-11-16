import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import utils.Utils;

public class Main extends Application {

    public static Gameloop GAMELOOP;

    @Override
    public void start(Stage stage){
        stage.setTitle("Hello World");
        Group root = new Group();
        Scene scene = new Scene( root );
        stage.setScene(scene);

        Canvas canvas = new Canvas(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT);
        root.getChildren().add(canvas);

        GAMELOOP = new Gameloop(root);
        GAMELOOP.start();

        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
