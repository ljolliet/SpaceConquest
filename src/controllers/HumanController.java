package controllers;

import game.Planet;
import javafx.scene.paint.Color;

public class HumanController extends Controller {
    public HumanController(Color color) {
        super(color);
    }
    //events and function used by the human to control his spaceships

	public boolean isOnHumanPlanet(double x, double y) {
		boolean res = false;

		for(Planet p : planets){
			if(Math.sqrt(Math.pow(p.getPosX() - x, 2) + Math.pow(p.getPosY() - y ,2)) < p.getRadius()){
				res = true;
			}
		}

		return res;
	}

	public Planet getPlanetClic(double x, double y){
		Planet res = null;

		for(Planet p : planets){
			if(Math.sqrt(Math.pow(p.getPosX() - x, 2) + Math.pow(p.getPosY() - y ,2)) < p.getRadius()){
				res = p;
			}
		}

		return res;
	}
	
}
