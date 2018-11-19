package game;

import javafx.scene.Group;
import utils.MathUtils;

import java.util.ArrayList;

public class Squadron {

    private ArrayList<Spaceship> spaceships;
    private Planet target;

    public Squadron(ArrayList<Spaceship> spaceships) {
        this.spaceships = spaceships;
    }

    public void initSpaceShipPos() {
        //set every ship pos around the planet
    }

    public void processSpaceShipPos() {
        for (Spaceship s : spaceships) {
            double angle = MathUtils.pointOnOffPlanet(target.getCenter(), s.getPos(), s.getDirection(), true);
            s.rotate(angle);
        }
    }

    public void setTarget(Planet newTarget) {
        target = newTarget;
        processSpaceShipPos();
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
