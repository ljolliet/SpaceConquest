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

    private TypeAI type;

    int currentTime = 60;

    public ComputerController(Color color, TypeAI type) {
        super(color);
        this.type = type;
    }
    //AI function which will control the spaceships and planet production
    //maybe thread ?

    public void process(ArrayList<Planet> all_planets, HashMap<Point2D, Boolean> map) {
        if (currentTime == Utils.AI_ACTION_TIME) {
            shipSendingDecision(all_planets, map);
            currentTime = 0;
        } else {
            currentTime++;
        }
    }

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

/*
    @Override
    public void writeObject(ObjectOutputStream oos) {

    }

    @Override
    public void readObject(ObjectInputStream ois) {

    }

*/
    }
