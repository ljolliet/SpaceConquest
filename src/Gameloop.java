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

import controllers.ComputerController;
import controllers.Controller;
import controllers.HumanController;

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
        controllers.add(new HumanController());
        //start at 1 because 0 is the human
        for(int i = 1; i < Utils.NB_PLAYER; i++){
            controllers.add(new ComputerController());
        }
    }

    public void initPlanets(){
        initPlayerPlanets();
        initNeutralPlanets();
    }

    //give random position at a good distance
    public void initPlayerPlanets(){
        for(int i = 0; i < controllers.size(); i ++){
            Planet p = new Planet(i*50,100, 50, false, 1, new LittleSpaceship()); // add color
            controllers.get(i).getPlanets().add(p);
            p.setOwner(controllers.get(i));
            planets.add(p);
        }
    }

    //give totally random position but not on another planet
    public void initNeutralPlanets(){
    	for(int i = 0; i < Utils.NB_NEUTRAL_PLANET; i++) {
    		addRandomNeutralPlanet();    		
    	}
    }
    
    public void addRandomNeutralPlanet() {
    	double randX = (Math.random() * Main.WINDOW_WIDTH);
		double randY = (Math.random() * Main.WINDOW_HEIGHT);
		if(randX - Utils.NEUTRAL_PLANET_RADIUS < 0) {
			randX += Utils.NEUTRAL_PLANET_RADIUS;
		}
		if(randY - Utils.NEUTRAL_PLANET_RADIUS < 0) {
			randY += Utils.NEUTRAL_PLANET_RADIUS;
		}
		
		boolean isPosFree = true;
		Planet tmp = new Planet(randX, randY, Utils.NEUTRAL_PLANET_RADIUS, true, 0, new LittleSpaceship());
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
