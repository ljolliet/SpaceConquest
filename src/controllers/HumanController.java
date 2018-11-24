package controllers;

import game.Planet;
import game.Squadron;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class HumanController extends Controller {
    private Squadron selectedSquadron = null;

    public HumanController(Color color) {
        super(color);
    }
    //events and function used by the human to control his spaceships

    public boolean isOnHumanPlanet(double x, double y) {
        boolean res = false;
        for (Planet p : planets) {
            if (Math.sqrt(Math.pow(p.getCenter().getX() - x, 2) + Math.pow(p.getCenter().getY() - y, 2)) < p.getRadius()) {
                res = true;
            }
        }

        return res;
    }

    public Planet getHumanPlanetClic(double x, double y) {
        Planet res = null;

        for (Planet p : planets) {
            if (Math.sqrt(Math.pow(p.getCenter().getX() - x, 2) + Math.pow(p.getCenter().getY() - y, 2)) < p.getRadius()) {
                res = p;
            }
        }

        return res;
    }

    public boolean isOnPlanet(double x, double y, ArrayList<Planet> planets) {
        boolean res = false;
        for (Planet p : planets) {
            if (Math.sqrt(Math.pow(p.getCenter().getX() - x, 2) + Math.pow(p.getCenter().getY() - y, 2)) < p.getRadius() && (p.getOwner() == null || p.getOwner() != this)) {
                res = true;
            }
        }
        return res;
    }

    public Planet getPlanetClic(double x, double y, ArrayList<Planet> planets) {
        Planet res = null;

        for (Planet p : planets) {
            if (Math.sqrt(Math.pow(p.getCenter().getX() - x, 2) + Math.pow(p.getCenter().getY() - y, 2)) < p.getRadius() && (p.getOwner() == null || p.getOwner() != this)) {
                res = p;
            }
        }

        return res;
    }

    public void launchShip(Planet p) {
        p.sendShip(p.getAvailable_ships());
    }

    public void setTarget(Planet p, HashMap<Point2D, Boolean> accessibilityMap) {
        if (this.selectedSquadron != null)
            selectedSquadron.setTarget(p, accessibilityMap); // BAD

    }

    public void setSelectedSquadron(Squadron selectedSquadron) {
        this.selectedSquadron = selectedSquadron;
    }
}
