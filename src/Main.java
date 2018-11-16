import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class Main extends Application {

    public static Gameloop GAMELOOP;
    public static int WINDOW_HEIGHT = 512;
    public static int WINDOW_WIDTH = 512;

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Hello World");
        Group root = new Group();
        Scene theScene = new Scene( root );
        primaryStage.setScene(theScene);

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        root.getChildren().add(canvas);

        GAMELOOP = new Gameloop(canvas);
        GAMELOOP.start();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
