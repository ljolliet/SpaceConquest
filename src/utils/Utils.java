package utils;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class Utils {

    public static final boolean OPTIMIZED = true;

    public static final int LITTLE_SPACESHIP_DAMAGE = 1;
    public static final float PLAYER_PRODUCTION_RATE = 1;
    public static int WINDOW_HEIGHT = 620;
    public static int WINDOW_WIDTH = 1080;

    public static int NB_PLAYER = 5;
    public static int NB_NEUTRAL_PLANET = 10;
    public static int NEUTRAL_PLANET_RADIUS = 25;
    public static int NEUTRAL_HP_RANGE = 100; //600 ?
    public static int HP_VARIATION = 20; //percentage
    
    public static int PLAYER_PLANET_RADIUS = 50;
    public static int DISTANCE_BETWEEN_PLAYERS = 150;
    public static int DISTANCE_BETWEEN_NEUTRAL = 100;
    public static int DISTANCE_PLANET_SHIPS = 15; //distance between the planet and the ship at ship's spawn
    public static int ADDITIONAL_HITBOX_RANGE = 6;     //must be < DISTANCE_PLANET_SHIP -> bug < 6 ??

    public static Color NEUTRAL_PLANET_COLOR = Color.GREY;
    public static List<Color> PLANET_COLOR =  Arrays.asList(Color.RED,Color.BLUEVIOLET, Color.GOLDENROD, Color.LIGHTSKYBLUE, Color.YELLOWGREEN);

    public static int COLUMN_SIZE = 5;

}
