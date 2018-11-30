package controllers;

import game.Planet;
import game.Squadron;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class HumanController extends Controller {
    private Squadron selectedSquadron = null;
    private Planet selectedPlanet = null;

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

    public Planet getHumanPlanetClick(double x, double y) {
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
            if (Math.sqrt(Math.pow(p.getCenter().getX() - x, 2) + Math.pow(p.getCenter().getY() - y, 2)) < p.getRadius()) { //&& (p.getOwner() == null || p.getOwner() != this)
                res = p;
            }
        }

        return res;
    }

    public void launchShip(Planet p) {
        Squadron squad = p.sendShip(Utils.WAVE_SIZE);
        this.setSelectedSquadron(squad);
    }

    public void setTarget(Planet p, HashMap<Point2D, Boolean> accessibilityMap) {
        if (this.selectedSquadron != null)
            selectedSquadron.setTarget(p, accessibilityMap);

    }

    public void setSelectedSquadron(Squadron selectedSquadron) {
        if(this.selectedSquadron != null)
        	this.selectedSquadron.setSelected(false);
        
        this.selectedSquadron = selectedSquadron;
        selectedSquadron.setSelected(true);
    }

	public Planet getSelectedPlanet() {
		return selectedPlanet;
	}

	public void setSelectedPlanet(Planet selectedPlanet) {
		this.selectedPlanet = selectedPlanet;
	}

	public Squadron getSelectedSquadron() {
		return selectedSquadron;
	}
}
