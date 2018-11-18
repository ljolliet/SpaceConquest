package game;

import javafx.scene.Group;

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
    	//set every pos after a change of target
    }
    
    public void changeTarget(Planet newTarget) {
    	//oefnsoenfsef
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
