import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.Utils;


/**
 * @author tguesdon, ljolliet
 */
public class Main extends Application {

    /**
     * Instance of the game
     */
    public static GameLoop GAMELOOP;

    /**
     * Group on which everything will be drawn.
     */
    public static Group GROUP;

    /**
     * Scene of the javaFX application.
     */
    public static Scene SCENE;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Space Conquest");
        GROUP = new Group();
        SCENE = new Scene(GROUP);
        GROUP.getStylesheets().add("file:css/stylesheet.css");
        stage.setScene(SCENE);

        if (Utils.TESTING) {
            GAMELOOP = new GameLoop();
            GAMELOOP.start();
        } else {
            GUIController.loadAssets();
            GUIController.generateDecoratives();
            GUIController.generateControlsAndTitle();
            GUIController.generateOptionControls();
            GUIController.drawBackground(GROUP, true);
            GUIController.setMainStage(stage);

            GUIController.getStart().setOnMouseClicked(event -> {
                GAMELOOP = new GameLoop();
                GAMELOOP.start();
                //addSceneEvents(scene, root);
            });

            GUIController.getOption().setOnMouseClicked(event -> {
                if (GUIController.OPTION_DISPLAYED) {
                    GUIController.drawOption(true);
                } else {
                    GUIController.drawOption(false);
                }

            });

            GUIController.getQuit().setOnMouseClicked(event -> {
                Platform.exit();
                try {
                    stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
