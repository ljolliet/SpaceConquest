package game;

import controllers.Controller;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import utils.MathUtils;
import utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Squadron implements Serializable{

    /**
     * List of all spaceship in this squadron.
     */
    private ArrayList<Spaceship> spaceships;
    /**
     * Squadron's target.
     */
    private Planet target;
    /**
     * Squadron's owner.
     */
    private Controller owner;
    /**
     * Boolean representing whether or not the squadron is selected by the human player.
     */
    private boolean selected = false;

    /**
     * Constructor of the squadron.
     * @param spaceships The list of spaceship that are contained in this squadron.
     * @param owner Squadron's owner.
     */
    public Squadron(ArrayList<Spaceship> spaceships, Controller owner) {
        this.spaceships = spaceships;
        this.owner = owner;
    }

    /**
     * For each spaceships remove the first step if they are close enough. Make the Spaceship point towards the next step.
     */
    private void processSpaceShipNextStep() {
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

    /**
     * Set a new target for this squadron and actualize steps of the spaceships.
     * @param newTarget The new target.
     * @param accessibilityMap Accessibilty map of the spaceships.
     */
    public void setTarget(Planet newTarget, HashMap<Point2D, Boolean> accessibilityMap) {
        target = newTarget;

        HashMap<Point2D, Boolean> squadAccessibilityMap = getSquadAccessibilityMap(target, accessibilityMap);
        for (Spaceship sp : spaceships) {
            sp.setSteps(MathUtils.pathfinder(sp.pos, target.getCenter(), squadAccessibilityMap));
        }
    }

    /**
     * Make the spaceships move towards their targets and check if there is a collision.
     */
    public void sendToTarget() {
        ArrayList<Spaceship> spaceShipToRemove = new ArrayList();
        Spaceship tmpShip;
        if (this.target != null) {
            processSpaceShipNextStep();
            for (Spaceship s : spaceships) {
                s.moveForward();
                if ((tmpShip = this.target.checkCollision(s, this.owner)) != null)
                    spaceShipToRemove.add(tmpShip);
            }
            for (Spaceship s : spaceShipToRemove)
                spaceships.remove(s);
        }

    }

    /**
     * Create an accessibility map where the target planet is accessible.
     * @param target The target of the squadron.
     * @param globalAccessibleMap The global accessible map, where every target is inaccessible.
     * @return
     */
    private HashMap<Point2D, Boolean> getSquadAccessibilityMap(Planet target, HashMap<Point2D, Boolean> globalAccessibleMap) {
        HashMap<Point2D, Boolean> tmp = (HashMap<Point2D, Boolean>) globalAccessibleMap.clone();
        for (Point2D p : tmp.keySet()) {
            if (target.contains(p))
                tmp.put(p, true);   //if p is in the target planet it is accessible
        }
        return tmp;
    }

    public boolean contains(Point2D pos) {
        for (Spaceship s : spaceships)
            if (s.contains(pos))
                return true;
        return false;
    }

    //--------------------------DRAW-----------------//

    /**
     * Drawing of the ships.
     * @param root
     */
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

    //-------------------------------SERIALIZATION----------------------//

    /**
     * Write the squadron on "save.ser"
     * @param oos the stream on which the squadron is written.
     */
    private void writeObject(ObjectOutputStream oos){
        try {
            oos.writeObject(spaceships);
            oos.writeObject(target);
            oos.writeObject(owner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the squadron from "save.ser"
     * @param ois the stream from which the squadron is read.
     */
    private void readObject(ObjectInputStream ois){
        try {
            spaceships = (ArrayList<Spaceship>)ois.readObject();
            target = (Planet) ois.readObject();
            owner = (Controller) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    //-------GETTER SETTER---------//


    public ArrayList<Spaceship> getSpaceships() {
        return spaceships;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Controller getOwner() { //ONly for test -> should we keep it ?
        return owner;
    }



    public Planet getTarget() {
        return target;
    }
}

