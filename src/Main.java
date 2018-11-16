import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {

    public static Gameloop GAMELOOP;
    public static int WINDOW_HEIGHT = 512;
    public static int WINDOW_WIDTH = 512;

    @Override
    public void start(Stage stage){
        stage.setTitle("Hello World");
        Group root = new Group();
        Scene scene = new Scene( root );
        stage.setScene(scene);

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        root.getChildren().add(canvas);

        GAMELOOP = new Gameloop(root);
        GAMELOOP.start();

        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
