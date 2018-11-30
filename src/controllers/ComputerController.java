package controllers;

import java.util.ArrayList;
import java.util.HashMap;

import game.Planet;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import utils.Utils;

public class ComputerController extends Controller {
	
	private TypeAI type = TypeAI.CLASSIC;
	
	int currentTime = 60;
	
    public ComputerController(Color color) {
        super(color);
    }
    //AI function which will control the spaceships and planet production
    //maybe thread ?
    
    public void process(ArrayList<Planet> all_planets, HashMap<Point2D, Boolean> map){
    	if(currentTime == Utils.AI_ACTION_TIME) {
    		shipSendingDecision(all_planets, map);
    		currentTime = 0;	
    	}else {
    		currentTime ++;
    	}
    }
    
    public void shipSendingDecision(ArrayList<Planet> all_planets, HashMap<Point2D, Boolean> map){
    	int ratio;
    	switch(type){
    	case CLASSIC:
    		ratio = 50;
    		break;
    	default:
    		ratio = 100;
    		break;
    	}
    	
    	for(Planet p : planets) {
    		Planet target = p.nearestEnnemyPlanet(all_planets);
    		p.addWaitingShips(ratio * p.getAvailable_ships() / 100, target, map);
    		System.out.println("Waiting ennemy ships : " + p.getWaiting_for_launch());
    		
    	}
    }

    
}
