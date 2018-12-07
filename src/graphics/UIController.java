package graphics;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import utils.Utils;

import java.util.ArrayList;
import java.util.Random;

public class UIController {

    public static ArrayList<Image> assets = new ArrayList<>();

    public static ArrayList<ImageView> decoratives = new ArrayList<>();

    public static Image title = new Image("file:res/images/title.png");
    public static ImageView titleView = new ImageView(title);

    public static Button start = new Button("Start");
    public static Button option = new Button("Options");
    public static Button quit = new Button("Quit");

    public static Rectangle buttonArea = new Rectangle();

    //Options
    public static boolean OPTION_DISPLAYED = false;

    public static Rectangle optionArea = new Rectangle();
    public static Line delimitation = new Line();
    public static Button apply = new Button("Apply");
    public static Text optimization = new Text("Optimization : ");
    public static Text playerNumber = new Text("Number of players : ");
    public static Text neutralNumber = new Text("Number of neutral planets : ");
    public static CheckBox optimizationController = new CheckBox();
    public static Slider playerNumberController = new Slider();
    public static Slider neutralNumberController = new Slider();

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
        titleView.setY(Utils.WINDOW_HEIGHT/10);

        Rectangle buttonRect = new Rectangle(100,50);

        start.setPrefSize(Utils.BUTTON_WIDTH,Utils.BUTTON_HEIGHT);
        option.setPrefSize(Utils.BUTTON_WIDTH,Utils.BUTTON_HEIGHT);
        quit.setPrefSize(Utils.BUTTON_WIDTH,Utils.BUTTON_HEIGHT);

        start.setShape(buttonRect);
        option.setShape(buttonRect);
        quit.setShape(buttonRect);

        start.setLayoutX(Utils.WINDOW_WIDTH/2 - Utils.BUTTON_WIDTH/2);
        start.setLayoutY(3*Utils.WINDOW_HEIGHT/12);

        option.setLayoutX(Utils.WINDOW_WIDTH/2 - Utils.BUTTON_WIDTH/2);
        option.setLayoutY(5*Utils.WINDOW_HEIGHT/12);

        quit.setLayoutX(Utils.WINDOW_WIDTH/2 - Utils.BUTTON_WIDTH/2);
        quit.setLayoutY(7*Utils.WINDOW_HEIGHT/12);

        buttonArea.setX(Utils.WINDOW_WIDTH/2 - Utils.BUTTON_WIDTH/2 - Utils.BUTTON_RECT_PADDING);
        buttonArea.setY(3*Utils.WINDOW_HEIGHT/12 - Utils.BUTTON_RECT_PADDING);

        buttonArea.setHeight(2*Utils.BUTTON_RECT_PADDING + (Utils.WINDOW_HEIGHT - quit.getLayoutY()) );
        buttonArea.setWidth(2*Utils.BUTTON_RECT_PADDING + Utils.BUTTON_WIDTH);

        buttonArea.setFill(Utils.BUTTON_RECT_COLOR);

    }

    public static void drawBackground(Group group, boolean withControl){
        Rectangle space = new Rectangle(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT);
        space.setFill(Utils.BACKGROUND_COLOR);

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
            group.getChildren().remove(optimizationController);
            group.getChildren().remove(playerNumberController);
            group.getChildren().remove(neutralNumberController);
            group.getChildren().remove(apply);
            OPTION_DISPLAYED = false;
        }else if(!OPTION_DISPLAYED){
            apply.setOnMouseClicked(event -> {
                Utils.OPTIMIZED = optimizationController.isSelected();
                Utils.NB_NEUTRAL_PLANET = (int)neutralNumberController.getValue();
                Utils.NB_PLAYER = (int)playerNumberController.getValue();
                drawOption(group, true);
            });
            group.getChildren().add(optionArea);
            group.getChildren().add(delimitation);
            group.getChildren().add(optimization);
            group.getChildren().add(playerNumber);
            group.getChildren().add(neutralNumber);
            group.getChildren().add(optimizationController);
            group.getChildren().add(playerNumberController);
            group.getChildren().add(neutralNumberController);
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

        optimization.setY(optionArea.getY() + optionArea.getHeight()/8);
        playerNumber.setY(optionArea.getY() + 3*optionArea.getHeight()/8);
        neutralNumber.setY(optionArea.getY() + 5*optionArea.getHeight()/8);

        optimization.setFill(Utils.TEXT_COLOR);
        playerNumber.setFill(Utils.TEXT_COLOR);
        neutralNumber.setFill(Utils.TEXT_COLOR);

        optimizationController.setSelected(Utils.OPTIMIZED);
        optimizationController.setLayoutX(optionArea.getX() + optionArea.getWidth()/2);
        optimizationController.setLayoutY(optimization.getY() - 10);

        playerNumberController.setMin(2);
        playerNumberController.setMax(5);
        playerNumberController.setValue(Utils.NB_PLAYER);
        playerNumberController.setShowTickLabels(true);
        playerNumberController.setShowTickMarks(true);
        playerNumberController.setMajorTickUnit(1);
        playerNumberController.setMinorTickCount(0);
        playerNumberController.setSnapToTicks(true);
        playerNumberController.setLayoutX(optionArea.getX() + optionArea.getWidth()/2);
        playerNumberController.setLayoutY(playerNumber.getY() - 10);

        neutralNumberController.setMin(5);
        neutralNumberController.setMax(20);
        neutralNumberController.setValue(Utils.NB_NEUTRAL_PLANET);
        neutralNumberController.setShowTickLabels(true);
        neutralNumberController.setShowTickMarks(true);
        neutralNumberController.setMajorTickUnit(4);
        neutralNumberController.setSnapToTicks(true);
        neutralNumberController.setLayoutX(optionArea.getX() + optionArea.getWidth()/2);
        neutralNumberController.setLayoutY(neutralNumber.getY() - 10);

    }
}
