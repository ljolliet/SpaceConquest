package controllers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import game.Planet;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import utils.Utils;

public class ComputerController extends Controller {

    /**
     * Enum describing the behaviour of the AI. (CLASSIC, SAFE, AGGRESSIVE).
     */
    private TypeAI type;

    /**
     * Integer used to make the AI send ships every 60 frames.
     */
    int currentTime = 60;

    /**
     * Constructor. Used to set the color and AI type of the controller.
     * @param color Color of the controller.
     * @param type AI type of the controller. (CLASSIC, SAFE, AGGRESSIVE).
     */
    public ComputerController(Color color, TypeAI type) {
        super(color);
        this.type = type;
    }

    /**
     * Function called every frame. Check if the AI has to make a decision, and call the decision function if so.
     * @param all_planets All planets of the current game.
     * @param map Accessibility map of the current game.
     */
    public void process(ArrayList<Planet> all_planets, HashMap<Point2D, Boolean> map) {
        if (currentTime == Utils.AI_ACTION_TIME) {
            shipSendingDecision(all_planets, map);
            currentTime = 0;
        } else {
            currentTime++;
        }
    }

    /**
     * Choose which percentage of ships will be send given the AI type then send them to the nearest ennemy target.
     * @param all_planets All planets of the current game.
     * @param map Accessibility map of the current game.
     */
    public void shipSendingDecision(ArrayList<Planet> all_planets, HashMap<Point2D, Boolean> map) {
        int ratio;
        switch (type) {
            case CLASSIC:
                ratio = Utils.CLASSIC_AI_PERCENTAGE;
                break;
            case AGGRESSIVE:
                ratio = Utils.AGGRESSIVE_AI_PERCENTAGE;
                break;
            case SAFE:
                ratio = Utils.SAFE_AI_PERCENTAGE;
                break;
            default:
                ratio = Utils.SAFE_AI_PERCENTAGE;
                break;
        }

        for (Planet p : planets) {
            Planet target = p.nearestEnnemyPlanet(all_planets);
            p.addWaitingShips(ratio * p.getAvailable_ships() / 100, target, map);
        }
    }
}
