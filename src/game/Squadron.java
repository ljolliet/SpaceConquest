package game;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import utils.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Squadron {

    private ArrayList<Spaceship> spaceships;
    private Planet target;

    public Squadron(ArrayList<Spaceship> spaceships) {
        this.spaceships = spaceships;
    }


    public void processSpaceShipPos() {
        for (Spaceship s : spaceships) {
            double angle = MathUtils.pointOnOffPlanet(target.getCenter(), s.getPos(), s.getDirection(), true);
            s.rotate(angle);
        }
    }

    public void setTarget(Planet newTarget, HashMap<Point2D, Boolean> accessibilityMap) {
        target = newTarget;
        processSpaceShipPos();

        //------Work in progress------//
        HashMap<Point2D, Boolean> squadAccessibilityMap = getSquadAccessibilityMap(target, accessibilityMap );
        for(Spaceship sp : spaceships){
            sp.setSteps(MathUtils.pathfinder(sp.pos, target.getCenter(), squadAccessibilityMap));
        }
    }

    public void sendToTarget() {
        if(this.target != null)
        for (Spaceship s : spaceships) {
            s.moveForward();
        }
    }

    public HashMap<Point2D, Boolean> getSquadAccessibilityMap(Planet target, HashMap<Point2D, Boolean> globalAccessibleMap) {
        HashMap<Point2D, Boolean> tmp = (HashMap<Point2D, Boolean>) globalAccessibleMap.clone();
        for (Point2D p : tmp.keySet()) {
            if (target.contains(p))
                tmp.put(p, true);   //if p is in the target planet it is accessible
        }
        return tmp;
    }

    //-------GETTER SETTER---------//

    public ArrayList<Spaceship> getSpaceships() {
        return spaceships;
    }

    public void setSpaceships(ArrayList<Spaceship> spaceships) {
        this.spaceships = spaceships;
    }

    public void draw(Group root) {
        for(Spaceship sp : spaceships){
            sp.draw(root);
        }
    }
}
