package graphics;

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
import utils.Utils;

import java.util.ArrayList;
import java.util.Random;

public class UIController {

    public static Stage mainStage = null;

    public static ArrayList<Image> assets = new ArrayList<>();

    public static ArrayList<ImageView> decoratives = new ArrayList<>();

    public static Image title = new Image("file:res/images/title.png");
    public static ImageView titleView = new ImageView(title);

    public static Button start = new Button("Start");
    public static Button option = new Button("Options");
    public static Button quit = new Button("Quit");
    private static VBox vboxMenu = new VBox();


    public static Rectangle buttonArea = new Rectangle();

    //Options
    public static boolean OPTION_DISPLAYED = false;

    public static Rectangle optionArea = new Rectangle();
    public static Line delimitation = new Line();
    public static Button apply = new Button("Apply");
    public static Text optimization = new Text("Optimization : ");
    public static Text playerNumber = new Text("Number of players : ");
    public static Text neutralNumber = new Text("Number of neutral planets : ");
    public static Text screenSize = new Text("Screen size : ");
    public static CheckBox optimizationController = new CheckBox();
    public static Slider playerNumberController = new Slider();
    public static Slider neutralNumberController = new Slider();
    public static ChoiceBox screenSizeController = new ChoiceBox();

    public static void loadAssets(){
        assets.add(new Image("file:res/images/planet1.png"));
        assets.add(new Image("file:res/images/planet2.png"));
        assets.add(new Image("file:res/images/blackhole1.png"));
        assets.add(new Image("file:res/images/star1.png"));
        assets.add(new Image("file:res/images/star2.png"));
        assets.add(new Image("file:res/images/star3.png"));
        assets.add(new Image("file:res/images/star4.png"));
    }

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
        }
    }

    public static void drawOption(Group group, boolean closeOption){
        if(closeOption && OPTION_DISPLAYED){
            group.getChildren().remove(optionArea);
            group.getChildren().remove(delimitation);
            group.getChildren().remove(optimization);
            group.getChildren().remove(playerNumber);
            group.getChildren().remove(neutralNumber);
            group.getChildren().remove(screenSize);
            group.getChildren().remove(optimizationController);
            group.getChildren().remove(playerNumberController);
            group.getChildren().remove(neutralNumberController);
            group.getChildren().remove(screenSizeController);
            group.getChildren().remove(apply);
            OPTION_DISPLAYED = false;
        }else if(!OPTION_DISPLAYED){
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

                drawOption(group, true);
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
        }
    }

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

    public static void generateMenuBar(){

        Menu menu = new Menu("Menu");
        MenuItem saveItem = new MenuItem("Save");
              // , new ImageView(new Image("menusample/new.png")));
        menu.getItems().add(saveItem);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu);
        vboxMenu.getChildren().addAll(menuBar);

    }

    public static void displayMenuBar(Group group) {
        group.getChildren().add(vboxMenu);

    }
}
