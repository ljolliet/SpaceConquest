
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class GUIController {

    /**
     * Stage of the application. Will be set in main function of Main.class
     */
    private static Stage mainStage = null;

    /**
     * All assets used in the game.
     */
    private static ArrayList<Image> assets = new ArrayList<>();
    /**
     * List of assets used to decorates the background.
     */
    private static ArrayList<ImageView> decoratives = new ArrayList<>();
    /**
     * Stylized title of the game.
     */
    private static Image title = new Image("file:resources/images/title.png");
    private static ImageView titleView = new ImageView(title);

    /**
     * Button used to start the game.
     */
    private static Button start = new Button("Start");
    /**
     * Button used to view and edit options.
     */
    private static Button option = new Button("Options");
    /**
     * Button used to quit the game.
     */
    private static Button quit = new Button("Quit");

    /**
     * Menu with button allowing to save and load a game while playing.
     */
    private static VBox vboxMenu = new VBox();

    /**
     * Rectangle surrounding the buttons start, option and quit.
     */
    private static Rectangle buttonArea = new Rectangle();

    /**
     * Boolean representing whether or not the option are displayed.
     */
    public static boolean OPTION_DISPLAYED = false;

    /**
     * Rectangle surrounding the options' controllers.
     */
    private static Rectangle optionArea = new Rectangle();
    /**
     * Line between the start, option, quit buttons and the options' controllers.
     */
    private static Line delimitation = new Line();
    /**
     * Button allowing to apply changes made to the options.
     */
    private static Button apply = new Button("Apply");

    /**
     * Text aside of the optimization checkbox.
     */
    private static Text optimization = new Text("Optimization : ");
    /**
     * Text aside of the slider on number of players.
     */
    private static Text playerNumber = new Text("Number of players : ");
    /**
     * Text aside of the slider on number of neutral planets.
     */
    private static Text neutralNumber = new Text("Number of neutral planets : ");
    /**
     * Text aside of the choice of screen size.
     */
    private static Text screenSize = new Text("Screen size : ");

    /**
     * Checkbox allowing to choose if the game will be optimized or not.
     */
    private static CheckBox optimizationController = new CheckBox();

    /**
     * Slider controlling the number of players.
     */
    private static Slider playerNumberController = new Slider();
    /**
     * Slider controlling the number of neutral planets.
     */
    private static Slider neutralNumberController = new Slider();
    /**
     * ChoiceBox controlling the screen size.
     */
    private static ChoiceBox screenSizeController = new ChoiceBox();

    /**
     * Alert sent after saving.
     */
    private static Alert saveAlert = new Alert(Alert.AlertType.INFORMATION, "Game saved in \"save.ser\"");

    /**
     * Alert sent after loading.
     */
    private static Alert loadAlert = new Alert(Alert.AlertType.INFORMATION, "Game loaded successfuly !");

    /**
     * Load every Image into assets.
     */
    public static void loadAssets(){
        assets.add(new Image("file:resources/images/planet1.png"));
        assets.add(new Image("file:resources/images/planet2.png"));
        assets.add(new Image("file:resources/images/blackhole1.png"));
        assets.add(new Image("file:resources/images/star1.png"));
        assets.add(new Image("file:resources/images/star2.png"));
        assets.add(new Image("file:resources/images/star3.png"));
        assets.add(new Image("file:resources/images/star4.png"));
    }

    /**
     * Generate a random list of images from the assets.
     */
    public static void generateDecoratives(){
        for(int i = 0; i < Utils.DECORATIVE_NUMBER; i++){
            int x = new Random().nextInt(Utils.WINDOW_WIDTH);
            int y = new Random().nextInt(Utils.WINDOW_HEIGHT);
            int decorativeIndex = new Random().nextInt(assets.size());

            int maxSize = Utils.DECORATIVE_SIZE + Utils.DECORATIVE_SIZE_VARIATION * Utils.DECORATIVE_SIZE / 100;
            int minSize = Utils.DECORATIVE_SIZE - Utils.DECORATIVE_SIZE_VARIATION * Utils.DECORATIVE_SIZE / 100;


            int width = new Random().nextInt(maxSize + 1 - minSize) + minSize;

            ImageView iv = new ImageView(assets.get(decorativeIndex));
            iv.setX(x);
            iv.setY(y);
            iv.setFitWidth(width);
            iv.setFitHeight(width);

            decoratives.add(iv);
        }
    }

    /**
     * Process position and size of every control. Process position and size of the title.
     */
    public static void generateControlsAndTitle(){
        titleView.setX(Utils.WINDOW_WIDTH/2 - title.getWidth()/2);
        titleView.setY(0);

        Rectangle buttonRect = new Rectangle(100,50);

        start.setPrefSize(Utils.BUTTON_WIDTH,Utils.BUTTON_HEIGHT);
        option.setPrefSize(Utils.BUTTON_WIDTH,Utils.BUTTON_HEIGHT);
        quit.setPrefSize(Utils.BUTTON_WIDTH,Utils.BUTTON_HEIGHT);

        start.setShape(buttonRect);
        option.setShape(buttonRect);
        quit.setShape(buttonRect);

        start.setLayoutX(Utils.WINDOW_WIDTH/2 - Utils.BUTTON_WIDTH/2);
        start.setLayoutY(4*Utils.WINDOW_HEIGHT/12);

        option.setLayoutX(Utils.WINDOW_WIDTH/2 - Utils.BUTTON_WIDTH/2);
        option.setLayoutY(6*Utils.WINDOW_HEIGHT/12);

        quit.setLayoutX(Utils.WINDOW_WIDTH/2 - Utils.BUTTON_WIDTH/2);
        quit.setLayoutY(8*Utils.WINDOW_HEIGHT/12);

        buttonArea.setX(Utils.WINDOW_WIDTH/2 - Utils.BUTTON_WIDTH/2 - Utils.BUTTON_RECT_PADDING);
        buttonArea.setY(4*Utils.WINDOW_HEIGHT/12 - Utils.BUTTON_RECT_PADDING);

        buttonArea.setHeight(4*Utils.BUTTON_RECT_PADDING + (quit.getLayoutY() - start.getLayoutY()));
        buttonArea.setWidth(2*Utils.BUTTON_RECT_PADDING + Utils.BUTTON_WIDTH);

        buttonArea.setFill(Utils.BUTTON_RECT_COLOR);

    }

    /**
     * Draw the background on a Group.
     * @param group Group on which the background will be drawn.
     * @param withControl True if the controls are drawn (main menu), false if not (during game).
     */
    public static void drawBackground(Group group, boolean withControl){
        Rectangle space = new Rectangle(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT);
        Stop[] stops = new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Utils.BACKGROUND_COLOR)};
        LinearGradient lg1 = new LinearGradient(0, 0, 0.75, 1, true, CycleMethod.NO_CYCLE, stops);

        space.setFill(lg1);

        group.getChildren().add(space);

        for(ImageView iv : decoratives){
            group.getChildren().add(iv);
        }

        if(withControl){
            group.getChildren().add(buttonArea);
            group.getChildren().add(titleView);
            group.getChildren().add(start);
            group.getChildren().add(option);
            group.getChildren().add(quit);
            
            apply.setOnMouseClicked(event -> {
                Utils.OPTIMIZED = optimizationController.isSelected();
                Utils.NB_NEUTRAL_PLANET = (int)neutralNumberController.getValue();
                Utils.NB_PLAYER = (int)playerNumberController.getValue();
                int oldWidth = Utils.WINDOW_WIDTH;
                switch (screenSizeController.getSelectionModel().getSelectedItem().toString()){
                    case "1920 x 1080":
                        Utils.WINDOW_WIDTH = 1920;
                        Utils.WINDOW_HEIGHT = 1080;
                        Utils.MAX_NB_PLAYER = 5;
                        Utils.MAX_NB_NEUTRAL_PLANET = 20;
                        break;
                    case "1600 x 900":
                        Utils.WINDOW_WIDTH = 1600;
                        Utils.WINDOW_HEIGHT = 900;
                        Utils.MAX_NB_PLAYER = 5;
                        Utils.MAX_NB_NEUTRAL_PLANET = 15;
                        if(Utils.NB_NEUTRAL_PLANET > Utils.MAX_NB_NEUTRAL_PLANET){
                            Utils.NB_NEUTRAL_PLANET = Utils.MAX_NB_NEUTRAL_PLANET;
                        }
                        break;
                    case "1280 x 720":
                        Utils.WINDOW_WIDTH = 1280;
                        Utils.WINDOW_HEIGHT = 720;
                        Utils.MAX_NB_PLAYER = 4;
                        Utils.MAX_NB_NEUTRAL_PLANET = 10;
                        if(Utils.NB_PLAYER > Utils.MAX_NB_PLAYER){
                            Utils.NB_PLAYER = Utils.MAX_NB_PLAYER;
                        }
                        if(Utils.NB_NEUTRAL_PLANET > Utils.MAX_NB_NEUTRAL_PLANET){
                            Utils.NB_NEUTRAL_PLANET = Utils.MAX_NB_NEUTRAL_PLANET;
                        }
                        break;
                    default:
                        break;
                }
                if(Utils.WINDOW_WIDTH != oldWidth){
                    ArrayList<Node> toDelete = new ArrayList<>();
                    for(Node n : group.getChildren()){
                        toDelete.add(n);
                    }
                    group.getChildren().removeAll(toDelete);
                    decoratives = new ArrayList<>();
                    generateDecoratives();
                    generateControlsAndTitle();
                    generateOptionControls();
                    drawBackground(group, true);
                    mainStage.setWidth(Utils.WINDOW_WIDTH);
                    mainStage.setHeight(Utils.WINDOW_HEIGHT);
                }
                drawOption( true);
            });
            
            group.getChildren().add(optionArea);
            group.getChildren().add(delimitation);
            group.getChildren().add(optimization);
            group.getChildren().add(playerNumber);
            group.getChildren().add(neutralNumber);
            group.getChildren().add(screenSize);
            group.getChildren().add(optimizationController);
            group.getChildren().add(playerNumberController);
            group.getChildren().add(neutralNumberController);
            group.getChildren().add(screenSizeController);
            group.getChildren().add(apply);
            OPTION_DISPLAYED = true;
            drawOption( true);
            
        }
    }

    /**
     * Make the option panel visible or not depending on closeOption.
     * @param closeOption If true makes the option panel visible, if not makes it invisible.
     */
    public static void drawOption(boolean closeOption){
        if(closeOption && OPTION_DISPLAYED){
        	optionArea.setVisible(false);
        	delimitation.setVisible(false);
        	optimization.setVisible(false);
        	playerNumber.setVisible(false);
        	neutralNumber.setVisible(false);
        	screenSize.setVisible(false);
        	optimizationController.setVisible(false);
            playerNumberController.setVisible(false);
            neutralNumberController.setVisible(false);
            screenSizeController.setVisible(false);
            apply.setVisible(false);
            
            OPTION_DISPLAYED = false;
        }else if(!closeOption && !OPTION_DISPLAYED){
        	
        	optionArea.setVisible(true);
        	delimitation.setVisible(true);
        	optimization.setVisible(true);
        	playerNumber.setVisible(true);
        	neutralNumber.setVisible(true);
        	screenSize.setVisible(true);
        	optimizationController.setVisible(true);
            playerNumberController.setVisible(true);
            neutralNumberController.setVisible(true);
            screenSizeController.setVisible(true);
            apply.setVisible(true);
            OPTION_DISPLAYED = true;
        }
    }

    /**
     * Process position of every controls of the options panel.
     */
    public static void generateOptionControls(){
        optionArea.setHeight(buttonArea.getHeight());
        optionArea.setWidth(Utils.OPTION_AREA_WIDTH);

        optionArea.setX(buttonArea.getX() + buttonArea.getWidth());
        optionArea.setY(buttonArea.getY());

        optionArea.setFill(Utils.BUTTON_RECT_COLOR);

        delimitation.setStartX(optionArea.getX());
        delimitation.setStartY(optionArea.getY());

        delimitation.setEndX(optionArea.getX());
        delimitation.setEndY(optionArea.getY() + optionArea.getHeight());

        delimitation.setFill(Color.BLACK);

        Rectangle buttonRect = new Rectangle(Utils.BUTTON_WIDTH,Utils.BUTTON_HEIGHT);
        apply.setPrefSize(Utils.BUTTON_WIDTH,Utils.BUTTON_HEIGHT);
        apply.setShape(buttonRect);
        apply.setLayoutX(optionArea.getX() + optionArea.getWidth()/2 - Utils.BUTTON_WIDTH/2);
        apply.setLayoutY(quit.getLayoutY());

        optimization.setX(optionArea.getX() + 10);
        playerNumber.setX(optionArea.getX() + 10);
        neutralNumber.setX(optionArea.getX() + 10);
        screenSize.setX(optionArea.getX() + 10);

        optimization.setY(optionArea.getY() + optionArea.getHeight()/12);
        playerNumber.setY(optionArea.getY() + 3*optionArea.getHeight()/12);
        neutralNumber.setY(optionArea.getY() + 5*optionArea.getHeight()/12);
        screenSize.setY(optionArea.getY() + 7 * optionArea.getHeight()/12);

        optimization.setFill(Utils.TEXT_COLOR);
        playerNumber.setFill(Utils.TEXT_COLOR);
        neutralNumber.setFill(Utils.TEXT_COLOR);
        screenSize.setFill(Utils.TEXT_COLOR);

        optimizationController.setSelected(Utils.OPTIMIZED);
        optimizationController.setLayoutX(optionArea.getX() + optionArea.getWidth()/2);
        optimizationController.setLayoutY(optimization.getY() - 10);

        playerNumberController.setMin(2);
        playerNumberController.setMax(Utils.MAX_NB_PLAYER);
        playerNumberController.setValue(Utils.NB_PLAYER);
        playerNumberController.setShowTickLabels(true);
        playerNumberController.setShowTickMarks(true);
        playerNumberController.setMajorTickUnit(1);
        playerNumberController.setMinorTickCount(0);
        playerNumberController.setSnapToTicks(true);
        playerNumberController.setLayoutX(optionArea.getX() + optionArea.getWidth()/2);
        playerNumberController.setLayoutY(playerNumber.getY() - 10);

        neutralNumberController.setMin(5);
        neutralNumberController.setMax(Utils.MAX_NB_NEUTRAL_PLANET);
        neutralNumberController.setValue(Utils.NB_NEUTRAL_PLANET);
        neutralNumberController.setShowTickLabels(true);
        neutralNumberController.setShowTickMarks(true);
        neutralNumberController.setMajorTickUnit(4);
        neutralNumberController.setSnapToTicks(true);
        neutralNumberController.setLayoutX(optionArea.getX() + optionArea.getWidth()/2);
        neutralNumberController.setLayoutY(neutralNumber.getY() - 10);

        screenSizeController.setLayoutX(optionArea.getX() + optionArea.getWidth()/2);
        screenSizeController.setLayoutY(screenSize.getY() - 10);
        screenSizeController.setItems(FXCollections.observableArrayList("1920 x 1080", "1600 x 900", "1280 x 720"));
        screenSizeController.getSelectionModel().selectFirst();
    }



    /**
     * Generate a menu with two buttons. One saving the game and one loading it.
     */
    public static void generateMenuBar(){
        if(vboxMenu.getChildren().isEmpty()) {
            Menu menu = new Menu("Menu");
            MenuItem saveItem = new MenuItem("Save", new ImageView(new Image("file:resources/images/planet1.png")));
            saveItem.setOnAction(e -> {
                vboxMenu.setVisible(true);
                System.out.println("  Saving ...");
                long t1 = System.currentTimeMillis();
                try {
                    FileOutputStream fileOut = new FileOutputStream("save.ser");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(Main.GAMELOOP);
                    out.close();
                    fileOut.close();
                    System.out.println("  Saved in save.ser (" + (System.currentTimeMillis() - t1) + ")ms");
                    saveAlert.show();
                    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2500), ae -> saveAlert.close()));
                    timeline.play();
                } catch (IOException i) {
                    i.printStackTrace();
                }
            });
            MenuItem loadItem = new MenuItem("Load", new ImageView(new Image("file:resources/images/planet2.png")));
            loadItem.setOnAction(e -> {
                vboxMenu.setVisible(true);
                System.out.println("  Loading ...");
                long t1 = System.currentTimeMillis();

                FileInputStream fileIn = null;
                try {
                    fileIn = new FileInputStream("save.ser");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    Main.GAMELOOP.stop();
                    Main.GAMELOOP = (GameLoop) in.readObject();
                    in.close();
                    fileIn.close();
                    Main.GAMELOOP.start();
                    System.out.println("  Loaded in " + (System.currentTimeMillis() - t1) + " ms");
                    loadAlert.show();
                    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2500), ae -> loadAlert.close()));
                    timeline.play();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }


            });
            menu.getItems().add(saveItem);
            menu.getItems().add(loadItem);

            MenuBar menuBar = new MenuBar();
            menuBar.getMenus().addAll(menu);
            vboxMenu.getChildren().addAll(menuBar);
        }
    }

    /**
     * Add the menu to the group given in parameter.
     * @param group Group on which the menu will be added.
     */
    public static void displayMenuBar(Group group) {
        group.getChildren().add(vboxMenu);

    }

    public static Button getQuit() {
        return quit;
    }

    public static Button getOption() {
        return option;
    }

    public static Button getStart() {
        return start;
    }

    public static void setMainStage(Stage mainStage) {
        GUIController.mainStage = mainStage;
    }

}
