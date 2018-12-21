package utils;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Arrays;
import java.util.List;

public class Utils {


    //------------GAME-------------//

    /**
     * Height of the window.
     */
    public static int WINDOW_HEIGHT = 1080;

    /**
     * Width of the window.
     */
    public static int WINDOW_WIDTH = 1920;

    /**
     * If true the game will run optimized code on some functions.
     */
    public static boolean OPTIMIZED = true;


    //------------SPACESHIPS-------------//

    /**
     * Spaceship default direction vector
     */
    public static final Point2D SPACESHIP_DIRECTION = new Point2D(0, 1);

    /**
     * Necessary production to create a little spaceship.
     */
    public static final int LITTLE_SPACESHIP_NEC_PROD = 10;

    /**
     * Necessary production to create a big spaceship.
     */
    public static final int BIG_SPACESHIP_NEC_PROD = 15;

    /**
     * Length of a little spaceship in px.
     */
    public static final int LITTLE_SPACESHIP_LENGTH = 25;

    /**
     * Length of a big spaceship in px.
     */
    public static final double BIG_SPACESHIP_LENGTH = 35;

    /**
     * Damage made by a little spaceship when it crashes on an enemy planet.
     */
    public static final int LITTLE_SPACESHIP_DAMAGE = 1;

    /**
     * Damage made by a big spaceship when it crashes on an enemy planet.
     */
    public static final int BIG_SPACESHIP_DAMAGE = 2;

    /**
     * Speed of a little spaceship in px/frame.
     */
    public static final double LITTLE_SPACESHIP_SPEED = 2.; // Not more than 3

    /**
     * Speed of a big spaceship in px/frame.
     */
    public static final double BIG_SPACESHIP_SPEED = 1;

//-----------------------------//

    /**
     * Production added by frame (=/= number of ship).
     */
    public static final float PLAYER_PRODUCTION_RATE = 1;

    /**
     * Maximal percentage of production that can be withdrawn or added to the production
     */
    public static final int PRODUCTION_VARIATION = 20;

    /**
     * Number of player.
     */
    public static int NB_PLAYER = 5;

    /**
     * Max number of player, will be changed depending on the resolution
     */
    public static int MAX_NB_PLAYER = 5;

    /**
     * Number of neutral planets.
     */
    public static int NB_NEUTRAL_PLANET = 20;

    /**
     * Max number of neutral planets, will be changed depending on the resolution
     */
    public static int MAX_NB_NEUTRAL_PLANET = 20;

    /**
     * Radius' size for neutral planets.
     */
    public static final int NEUTRAL_PLANET_RADIUS = 2 * LITTLE_SPACESHIP_LENGTH;

    /**
     * Base HP before variation calculation for neutral planets.
     */
    public static final int NEUTRAL_HP_RANGE = 100;

    /**
     * Maximum percentage of HP that can be withdrawn or added to neutral planets when they are generated.
     */
    public static final int HP_VARIATION = 20;

    /**
     * Radius' size for player planets.
     */
    public static int PLAYER_PLANET_RADIUS = 3 * LITTLE_SPACESHIP_LENGTH;

    /**
     * Maximum percentage that can be withdrawn or added to the radius of a planet.
     */
    public static int RADIUS_VARIATION = 20;

    /**
     * Minimal distance between a planet and the border of the screen at initialization
     */
    public static int DISTANCE_BORDER = 2 * PLAYER_PLANET_RADIUS;

    /**
     * Minimal distance between two player's planet at initialization.
     */
    public static int DISTANCE_BETWEEN_PLAYERS = 3 * PLAYER_PLANET_RADIUS;

    /**
     * Minimal distance between a neutral planet and every other planet at initialization.
     */
    public static int DISTANCE_BETWEEN_NEUTRAL = 2 * PLAYER_PLANET_RADIUS;

    /**
     * Distance between the ship and the planet at ship's spawn.
     */
    public static int DISTANCE_PLANET_SHIPS = 15;

    /**
     * Color for neutral planets.
     */
    public static Color NEUTRAL_PLANET_COLOR = Color.GREY;

    /**
     * List of colors for players.
     */
    public static List<Color> PLANET_COLOR = Arrays.asList(Color.RED, Color.BLUEVIOLET, Color.GOLDENROD, Color.LIGHTSKYBLUE, Color.YELLOWGREEN);

    /**
     * Generic name of the colors.
     */
    public static List<String> COLOR_STRING = Arrays.asList("RED", "PURPLE", "YELLOW", "BLUE", "GREEN");

    /**
     * Color of the halo when a planet is selected.
     */
    public static Paint HALO_COLOR = PLANET_COLOR.get(0).darker();

    /**
     * Distance between points used for the path finding algorithm. Smaller number increase time needed for calculation but increase precision.
     */
    public static int COLUMN_SIZE = 5;

    /**
     * Size added to the radius of a planet when selected.
     */
    public static int SELECTED_HALO_SIZE = 5;

    /**
     * Max size for a wave.
     */
    public static int WAVE_SIZE_MAX = 50;

    /**
     * Number of ships per wave (for the player).
     */
    public static int WAVE_SIZE = 25;

    /**
     * Distance around a planet that needs to be empty for spaceships to take off.
     */
    public static int ALLOWED_RANGE_TAKE_OFF = 25;

    //--------------------------------------AI-------------------

    /**
     * Number of frames between each AI action.
     */
    public static int AI_ACTION_TIME = 60;

    /**
     * Percentage of ships sent at every action for each type of AI
     */
    public static int CLASSIC_AI_PERCENTAGE = 30;
    public static int AGGRESSIVE_AI_PERCENTAGE = 50;
    public static int SAFE_AI_PERCENTAGE = 10;

    /**
     * Start the game without start screen
     */
    public static boolean TESTING = false;

    //-----------------------USER INTERFACE-------------------------

    /**
     * Number of decorative sprites in the background
     */
    public static int DECORATIVE_NUMBER = 40;

    /**
     * Average size of sprites in the background
     */
    public static int DECORATIVE_SIZE = 30;

    /**
     * Percentage of variation around DECORATIVE_SIZE for the size of decorative sprites in the background
     */
    public static int DECORATIVE_SIZE_VARIATION = 33;

    /**
     * Color of the background
     */
    public static Color BACKGROUND_COLOR = Color.rgb(0, 0, 51); //Nightblue

    /**
     * Width of buttons in the user interface
     */
    public static int BUTTON_WIDTH = 100;

    /**
     * Height of buttons in the user interface
     */
    public static int BUTTON_HEIGHT = 50;

    /**
     * Size of padding around buttons
     */
    public static int BUTTON_RECT_PADDING = BUTTON_WIDTH / 4;

    /**
     * Color of the rectangle surrounding buttons
     */
    public static Color BUTTON_RECT_COLOR = Color.rgb(77, 0, 77, 0.75);

    /**
     * Width of the rectangle around options
     */
    public static int OPTION_AREA_WIDTH = Utils.WINDOW_WIDTH / 5;

    /**
     * Option text color
     */
    public static Color TEXT_COLOR = Color.WHITE;

}
