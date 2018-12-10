import graphics.UIController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import utils.Utils;

import java.io.*;

public class Main extends Application {

    /**
     * Instance of the game
     */
    public static Gameloop GAMELOOP;


    @Override
    public void start(Stage stage) {
        stage.setTitle("Space Conquest");
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        //Canvas canvas = new Canvas(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT);  <---- Full useless ???
        //root.getChildren().add(canvas);


        if(Utils.TESTING){
            GAMELOOP = new Gameloop(root, scene);
            GAMELOOP.start();
            addSceneEvents(scene,root);
        }
        else{
            UIController.loadAssets();
            UIController.generateDecoratives();
            UIController.generateControlsAndTitle();
            UIController.generateOptionControls();
            UIController.drawBackground(root, true);
            UIController.mainStage = stage;

            UIController.start.setOnMouseClicked(event -> {
                GAMELOOP = new Gameloop(root, scene);
                GAMELOOP.start();
                addSceneEvents(scene,root);
            });

            UIController.option.setOnMouseClicked(event -> {
                if(UIController.OPTION_DISPLAYED){
                    UIController.drawOption(root, true);
                }else{
                    UIController.drawOption(root, false);
                }

            });

            UIController.quit.setOnMouseClicked(event -> {
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
                    GAMELOOP = (Gameloop) in.readObject();
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
    }


    public static void main(String[] args) {
        launch(args);
    }

}
