import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.Utils;

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

        //Canvas canvas = new Canvas(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT);  <---- Full useless ???
        //root.getChildren().add(canvas);


        if (Utils.TESTING) {
            GAMELOOP = new GameLoop();
            GAMELOOP.start();
            //addSceneEvents(scene, root);
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

    /**
     * Add events allowing to save the game with "S" and load with "L".
     * @param
     * @param
     */
    /*
    public void addSceneEvents(Scene scene, Group root){
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.S){

                System.out.println("  Saving ...");
                long t1 = System.currentTimeMillis();
                try {
                    FileOutputStream fileOut =
                            new FileOutputStream("save.ser");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(GAMELOOP);
                    out.close();
                    fileOut.close();
                    System.out.printf("  Saved in save.ser (" + (System.currentTimeMillis() - t1) + ")ms");
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }else if(event.getCode() == KeyCode.L){
                System.out.println("  Loading ...");
                long t1 = System.currentTimeMillis();
                GAMELOOP = null;
                try
                {
                    FileInputStream fileIn = new FileInputStream("save.ser");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    GAMELOOP = (GameLoop) in.readObject();
                    in.close();
                    fileIn.close();
                    GAMELOOP.setScene(scene);
                    GAMELOOP.setRoot(root);
                    GAMELOOP.start();
                    System.out.printf("  Loaded in " + (System.currentTimeMillis() - t1) + " ms");

                } catch(IOException ioe)
                {
                    ioe.printStackTrace();
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    } */


    public static void main(String[] args) {
        launch(args);
    }
}
