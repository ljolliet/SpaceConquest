package graphics;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
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
    public static Rectangle optionArea = new Rectangle();
    public static Line delimitation = new Line();
    public static Button apply = new Button("Apply");

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
        if(closeOption){
            group.getChildren().remove(optionArea);
            group.getChildren().remove(delimitation);
            group.getChildren().remove(apply);
        }else{
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

            apply.setOnMouseClicked(event -> {
                drawOption(group, true);
            });

            group.getChildren().add(optionArea);
            group.getChildren().add(delimitation);
            group.getChildren().add(apply);

        }
    }
}
