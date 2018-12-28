package controllers;

import game.Planet;
import game.Squadron;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class HumanController extends Controller {

    /**
     * Currently selected squadrons.
     */
    private transient ArrayList<Squadron> selectedSquadrons = new ArrayList<>();

    /**
     * Currently selected planet.
     */
    private Planet selectedPlanet = null;

    /**
     * Controller. Set the color of the controller.
     *
     * @param color Color of the controller.
     * */
    public HumanController(Color color) {
        super(color);
    }

    /**
     * Check if a position(x,y) is on a planet belonging to the human player.
     *
     * @param x X position to be checked.
     * @param y Y position to be checked.
     * @return True if the position is contained in a planet belonging to the human player, false otherwise.
     */
    public boolean isOnHumanPlanet(double x, double y) {
        boolean res = false;
        for (Planet p : planets) {
            if (Math.sqrt(Math.pow(p.getCenter().getX() - x, 2) + Math.pow(p.getCenter().getY() - y, 2)) < p.getRadius()) {
                res = true;
            }
        }
        return res;
    }

    /**
     * Get the human planet containing the position(x,y).
     *
     * @param x X position.
     * @param y Y position.
     * @return The planet containing the position(x,y).
     */
    public Planet getHumanPlanetClick(double x, double y) {
        Planet res = null;

        for (Planet p : planets) {
            if (Math.sqrt(Math.pow(p.getCenter().getX() - x, 2) + Math.pow(p.getCenter().getY() - y, 2)) < p.getRadius()) {
                res = p;
            }
        }

        return res;
    }

    /**
     * Check if the position(x,y) is on a planet which doesn't belong to this controller.
     *
     * @param x       X position to be checked.
     * @param y       Y position to be checked.
     * @param planets List of every planets in the current game.
     * @return True if the position(x,y) is contained in a planet.
     */
    public boolean isOnPlanet(double x, double y, ArrayList<Planet> planets) {
        boolean res = false;
        for (Planet p : planets) {
            if (Math.sqrt(Math.pow(p.getCenter().getX() - x, 2) + Math.pow(p.getCenter().getY() - y, 2)) < p.getRadius() && (p.getOwner() == null || p.getOwner() != this)) {
                res = true;
            }
        }
        return res;
    }

    /**
     * Get the planet containing the position(x,y). There is no need to check owner because this function is called in a if on isOnPlanet().
     *
     * @param x       X position.
     * @param y       Y position.
     * @param planets List of every planets in the current game.
     * @return The planet containing the position(x,y).
     */
    public Planet getPlanetClic(double x, double y, ArrayList<Planet> planets) {
        Planet res = null;

        for (Planet p : planets) {
            if (Math.sqrt(Math.pow(p.getCenter().getX() - x, 2) + Math.pow(p.getCenter().getY() - y, 2)) < p.getRadius()) {
                res = p;
            }
        }

        return res;
    }

    /**
     * Call sending ships function of a planet.
     *
     * @param p The planet that will send its ships.
     */
    public void launchShip(Planet p) {
        p.sendShip(Utils.WAVE_SIZE);
    }

    /**
     * Set the target of the selected squadron.
     *
     * @param p                The new target.
     * @param accessibilityMap Accessibility map of the squadron.
     */
    public void setTarget(Planet p, HashMap<Point2D, Boolean> accessibilityMap) {
        for(Squadron s : selectedSquadrons)
            s.setTarget(p, accessibilityMap);

    }

    /**
     * Change the selected squadrons. Change the selected boolean of this squadron.
     *
     * @param squadrons
     */
    public void changeSelectedSquadron(ArrayList<Squadron> squadrons){
        for(Squadron s : selectedSquadrons)
            s.setSelected(false);

        selectedSquadrons = squadrons;

        for(Squadron s : selectedSquadrons){
            s.setSelected(true);
        }
    }

    public Planet getSelectedPlanet() {
        return selectedPlanet;
    }

    public void setSelectedPlanet(Planet selectedPlanet) {
        this.selectedPlanet = selectedPlanet;
    }
}
