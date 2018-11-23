package game;

import controllers.Controller;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import utils.MathUtils;
import java.util.ArrayList;
import java.util.HashMap;

public class Squadron {

    private ArrayList<Spaceship> spaceships;
    private Planet target;
    private Controller owner;
    private boolean selected = false;


    public Squadron(ArrayList<Spaceship> spaceships, Controller owner) {
        this.spaceships = spaceships;
        this.owner = owner;
    }


    public void processSpaceShipPos() {
        for (Spaceship s : spaceships) {
            if(!s.getSteps().isEmpty() && Math.sqrt(Math.pow(s.getSteps().getFirst().getX() - s.getPos().getX(),2) + Math.pow(s.getSteps().getFirst().getY() - s.getPos().getY(),2)) < 2){
                s.getSteps().removeFirst();
            }

            if(!s.getSteps().isEmpty()){
                double angle = MathUtils.pointOnOffPlanet(s.getSteps().getFirst(), s.getPos(), s.getDirection(), true);
                s.rotate(angle);
            }


        }
    }

    public void setTarget(Planet newTarget, HashMap<Point2D, Boolean> accessibilityMap) {
        target = newTarget;

        //------Work in progress------//
        HashMap<Point2D, Boolean> squadAccessibilityMap = getSquadAccessibilityMap(target, accessibilityMap );
        for(Spaceship sp : spaceships){
            sp.setSteps(MathUtils.pathfinder(sp.pos, target.getCenter(), squadAccessibilityMap));

        }
    }

    public void sendToTarget() {
        ArrayList<Spaceship> spaceShipToRemove = new ArrayList();
        Spaceship tmpShip;
        if(this.target != null){
            processSpaceShipPos();
            for (Spaceship s : spaceships) {
                s.moveForward();
                if((tmpShip = this.target.checkCollision(s,this.owner))!=null)
                    spaceShipToRemove.add(tmpShip);
            }
            for(Spaceship s : spaceShipToRemove)
                spaceships.remove(s);
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
            sp.draw(root,selected);

            if(sp.getSteps() != null){
                /*for(Point2D p : sp.getSteps()){
                    //System.out.println(" X : " + p.getX() + ", Y : " + p.getY());
                    Circle c = new Circle(p.getX(),p.getY(),4);
                    c.setFill(Color.BLACK);

                    root.getChildren().add(c);
                }*/
            }

        }

    }

    public boolean contains(Point2D pos) {
        for(Spaceship s : spaceships)
            if(s.contains(pos))
                return true;
        return false;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;

    }
}

