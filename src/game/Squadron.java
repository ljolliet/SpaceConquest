package game;

import controllers.Controller;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import utils.MathUtils;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Squadron {

    private ArrayList<Spaceship> spaceships;
    private Planet target;
    private final Controller owner;
    private boolean selected = false;


    public Squadron(ArrayList<Spaceship> spaceships, Controller owner) {
        this.spaceships = spaceships;
        this.owner = owner;
    }


    private void processSpaceShipPos() {
        for (Spaceship s : spaceships) {
            if (!s.getSteps().isEmpty() && Math.sqrt(Math.pow(s.getSteps().getFirst().getX() - s.getPos().getX(), 2) + Math.pow(s.getSteps().getFirst().getY() - s.getPos().getY(), 2)) < 2) {
                s.getSteps().removeFirst();
            }

            if (!s.getSteps().isEmpty()) {
                double angle = MathUtils.pointOnOffPlanet(s.getSteps().getFirst(), s.getPos(), s.getDirection(), true);
                s.rotate(angle);
            }
        }
    }

    public void setTarget(Planet newTarget, HashMap<Point2D, Boolean> accessibilityMap) {
        target = newTarget;

        //------Work in progress------//
        HashMap<Point2D, Boolean> squadAccessibilityMap = getSquadAccessibilityMap(target, accessibilityMap);
        for (Spaceship sp : spaceships) {
            sp.setSteps(MathUtils.pathfinder(sp.pos, target.getCenter(), squadAccessibilityMap));
        }
    }

    public void sendToTarget() {
        ArrayList<Spaceship> spaceShipToRemove = new ArrayList();
        Spaceship tmpShip;
        if (this.target != null) {
            processSpaceShipPos();
            for (Spaceship s : spaceships) {
                s.moveForward();
                if ((tmpShip = this.target.checkCollision(s, this.owner)) != null)
                    spaceShipToRemove.add(tmpShip);
            }
            for (Spaceship s : spaceShipToRemove)
                spaceships.remove(s);
        }

    }

    private HashMap<Point2D, Boolean> getSquadAccessibilityMap(Planet target, HashMap<Point2D, Boolean> globalAccessibleMap) {
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

    public void draw(Group root) {
        if (!Utils.OPTIMIZED) {
            //long t1 = System.currentTimeMillis();
            for (Spaceship sp : spaceships) {
                sp.draw(root, selected);
                if (sp.getSteps() != null) {
                /*for(Point2D p : sp.getSteps()){
                    //System.out.println(" X : " + p.getX() + ", Y : " + p.getY());
                    Circle c = new Circle(p.getX(),p.getY(),4);
                    c.setFill(Color.BLACK);

                    root.getChildren().add(c);
                }*/
                }
            }
            //System.out.println("Non optimized draw squad time : " + (System.currentTimeMillis() - t1));
        } else {
            //long t1 = System.currentTimeMillis();
            LinkedList<Point2D> pointsDrawn = new LinkedList<>();
            for (Spaceship sp : spaceships) {
                if (!pointsDrawn.contains(sp.getPos())) {
                    sp.draw(root, selected);
                    pointsDrawn.add(sp.getPos());
                }
            }
            //System.out.println("Optimized draw squad time : " + (System.currentTimeMillis() - t1));
        }

    }

    public boolean contains(Point2D pos) {
        for (Spaceship s : spaceships)
            if (s.contains(pos))
                return true;
        return false;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Controller getOwner() { //ONly for test -> should we keep it ?
        return owner;
    }
}

