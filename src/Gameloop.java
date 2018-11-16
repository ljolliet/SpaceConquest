import game.Planet;
import game.Squadron;
import game.spaceships.LittleSpaceship;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import utils.Utils;

import java.util.ArrayList;
import java.util.Random;

import controllers.ComputerController;
import controllers.Controller;
import controllers.HumanController;

import javax.rmi.CORBA.Util;

public class Gameloop extends AnimationTimer{
    //this class will update all graphics change
    //ex : ships' deplacement

    private Group root;


    private ArrayList<Controller> controllers = new ArrayList<>();
    private ArrayList<Planet> planets = new ArrayList<>();

    public Gameloop(Group root){
        this.root = root;
        init();
    }

    @Override
    public void handle(long now) {
        //code here is repeated
        draw();
        actualizeProduction();
    }

    public void actualizeProduction(){
        for(Controller c : controllers){
            for(Planet p : c.getPlanets()){
                p.addProduction();
            }
        }
    }

    public void init(){
        initPlayers();
        initPlanets();
    }

    public void initPlayers(){
        controllers.add(new HumanController(Utils.PLANET_COLOR.get(0)));
        //start at 1 because 0 is the human
        for(int i = 1; i < Utils.NB_PLAYER; i++){
            controllers.add(new ComputerController(Utils.PLANET_COLOR.get(i)));
        }
    }

    public void initPlanets(){
        initPlayerPlanets();
        initNeutralPlanets();
    }

    //give random position at a good distance
    public void initPlayerPlanets(){
        for(int i = 0; i < controllers.size(); i ++){
        	double[] pos = getPlayerPos();
            Planet p = new Planet(pos[0],pos[1], Utils.PLAYER_PLANET_RADIUS, false, 1, new LittleSpaceship(), controllers.get(i).getColor()); // add color
            controllers.get(i).getPlanets().add(p);
            p.setOwner(controllers.get(i));
            planets.add(p);
        }
    }
    
    public double[] getPlayerPos(){
    	double[] pos = new double[2];
    	int maxX = Utils.WINDOW_WIDTH - Utils.PLAYER_PLANET_RADIUS;
    	int maxY = Utils.WINDOW_HEIGHT - Utils.PLAYER_PLANET_RADIUS;
    	int min = Utils.PLAYER_PLANET_RADIUS;
    	
    	pos[0] = new Random().nextInt(maxX + 1 - min) + min; 
    	pos[1] = new Random().nextInt(maxY + 1 - min) + min; 		
    	
    	boolean isFree = true;
    	
    	for(Planet p : planets) {
    		if(p.distantOf(pos) < Utils.DISTANCE_BETWEEN_PLAYERS) {
    			isFree = false;
    		}
    	}
    	
    	if(isFree) {
    		return pos;
    	}else {
    		return getPlayerPos();
    	}
       }

    //give totally random position but not on another planet
    public void initNeutralPlanets(){
    	for(int i = 0; i < Utils.NB_NEUTRAL_PLANET; i++) {
    		addRandomNeutralPlanet();    		
    	}
    }
    
    public void addRandomNeutralPlanet() {
    	int maxX = Utils.WINDOW_WIDTH - Utils.NEUTRAL_PLANET_RADIUS;
    	int maxY = Utils.WINDOW_HEIGHT - Utils.NEUTRAL_PLANET_RADIUS;
    	int min = Utils.NEUTRAL_PLANET_RADIUS;
    	
    	double randX = new Random().nextInt(maxX + 1 - min) + min; 
    	double randY = new Random().nextInt(maxY + 1 - min) + min; 			
		
		boolean isPosFree = true;
		Planet tmp = new Planet(randX, randY, Utils.NEUTRAL_PLANET_RADIUS, true, 0, new LittleSpaceship(), Utils.NEUTRAL_PLANET_COLOR);
		for(Planet p : planets) {
			if(tmp.collide(p)) {
				isPosFree = false;
			}
		}
		
		if(isPosFree) {
			planets.add(tmp);
		}else {
			addRandomNeutralPlanet();
		}
    }
    public void draw(){
        root.getChildren().removeAll(root.getChildren()); // clear root

        for(Planet p : planets) // draw all planets
            p.draw(root);

        for(Controller c : controllers)
            for(Squadron s : c.getSquadrons()) // draw all squadrons
                s.draw(root);
    }



}
