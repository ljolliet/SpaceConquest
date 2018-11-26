package utils;

import javafx.scene.paint.Color;
import java.util.Arrays;
import java.util.List;

public class Utils {

	/**
	 * Max size for a wave.
	 */
	public static int WAVE_SIZE = 50;
    
	/**
     * If true the game will run optimized code on some functions.
     */
    public static final boolean OPTIMIZED = true;

    /**
     * Damage made by the little spaceship when they land on an enemy planet.
     */
    public static final int LITTLE_SPACESHIP_DAMAGE = 1;

    /**
     * Production added by frame (=/= number of ship).
     */
    public static final float PLAYER_PRODUCTION_RATE = 1; // Double ?

    /**
     * Necessary production to create a Spaceship.
     */
    public static final int LITTLE_SPACESHIP_NEC_PROD = 60;

    /**
     *  Length of a little spaceship in px.
     */
    public static final int LITTLE_SPACESHIP_LENGTH = 25;

    /**
     * Height of the window.
     */
    public static int WINDOW_HEIGHT = 1080;

    /**
     * Width of the window.
     */
    public static int WINDOW_WIDTH = 1920;

    /**
     * Number of player.
     */
    public static int NB_PLAYER = 5;

    /**
     * Number of neutral planets.
     */
    public static int NB_NEUTRAL_PLANET = 20;

    /**
     * Radius' size for neutral planets.
     */
    public static int NEUTRAL_PLANET_RADIUS = 2*LITTLE_SPACESHIP_LENGTH;

    /**
     * Base HP before variation calculation for neutral planets.
     */
    public static int NEUTRAL_HP_RANGE = 100; //600 ?

    /**
     * Maximum percentage of HP that can be withdrawn or added to neutral planets when they are generated.
     */
    public static int HP_VARIATION = 20; //percentage

    /**
     * Radius' size for player planets.
     */
    public static int PLAYER_PLANET_RADIUS = 3*LITTLE_SPACESHIP_LENGTH;

    /**
     * Minimal distance between two player's planet at initialization.
     */
    public static int DISTANCE_BETWEEN_PLAYERS = 3*PLAYER_PLANET_RADIUS;

    /**
     * Minimal distance between a neutral planet and every other planet at initialization.
     */
    public static int DISTANCE_BETWEEN_NEUTRAL = 2*PLAYER_PLANET_RADIUS;

    /**
     * Distance between the ship and the planet at ship's spawn.
     */
    public static int DISTANCE_PLANET_SHIPS = 15;

    /**
     * Distance around a planet where ships cannot go.
     */
    public static int ADDITIONAL_HITBOX_RANGE = 6;     //must be < DISTANCE_PLANET_SHIP -> bug < 6 ??

    /**
     * Color for neutral planets.
     */
    public static Color NEUTRAL_PLANET_COLOR = Color.GREY;

    /**
     * List of colors for players.
     */
    public static List<Color> PLANET_COLOR =  Arrays.asList(Color.RED,Color.BLUEVIOLET, Color.GOLDENROD, Color.LIGHTSKYBLUE, Color.YELLOWGREEN);

    /**
     * Distance between points used for the path finding algorithm. Smaller number increase time needed for calculation but increase precision.
     */
    public static int COLUMN_SIZE = 5;

}
