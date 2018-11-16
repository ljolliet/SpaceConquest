package utils;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class Utils {
	
	
    public static int WINDOW_HEIGHT = 620;
    public static int WINDOW_WIDTH = 1080;

    public static int NB_PLAYER = 5;
    public static int NB_NEUTRAL_PLANET = 5;
    public static int NEUTRAL_PLANET_RADIUS = 25;
    public static int PLAYER_PLANET_RADIUS = 50;
    public static int DISTANCE_BETWEEN_PLAYERS = 150;

    public static Color NEUTRAL_PLANET_COLOR = Color.GREY;
    public static List<Color> PLANET_COLOR =  Arrays.asList(Color.RED,Color.BLUEVIOLET, Color.GOLDENROD, Color.LIGHTSKYBLUE, Color.YELLOWGREEN);

    //Refresh the amount of production of planets every TIME_INTERVAL, 1s ? 100ms?
    public static int TIME_INTERVAL = 1;
}
