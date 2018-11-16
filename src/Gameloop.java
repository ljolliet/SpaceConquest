import game.Planet;
import game.spaceships.LittleSpaceship;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import utils.Utils;

import java.util.ArrayList;

import controllers.ComputerController;
import controllers.Controller;
import controllers.HumanController;

public class Gameloop extends AnimationTimer{
    //this class will update all graphics change
    //ex : ships' deplacement

	public static int NB_NEUTRAL_PLANET = 5;
	public static int NEUTRAL_PLANET_RADIUS = 25;
	
    private Canvas canvas;

    private ArrayList<Controller> controllers = new ArrayList<>();
    private ArrayList<Planet> planets = new ArrayList<>();

    public Gameloop(Canvas canvas){
        this.canvas = canvas;
        init();
    }

    @Override
    public void handle(long now) {
        //code here is repeated
        actualizeProduction();
    }

    public void actualizeProduction(){
        for(Controller c : controllers){
            for(Planet p : c.getPlanets()){
                p.addProduction();
            }
        }
        System.out.println(planets);
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
            Planet p = new Planet(i*50,100, 50, false, 1, new LittleSpaceship());
            controllers.get(i).getPlanets().add(p);
            p.setOwner(controllers.get(i));
            planets.add(p);
        }
    }

    //give totally random position but not on another planet
    public void initNeutralPlanets(){
    	for(int i = 0; i < NB_NEUTRAL_PLANET; i++) {
    		addRandomNeutralPlanet();    		
    	}
    }
    
    public void addRandomNeutralPlanet() {
    	double randX = (Math.random() * Main.WINDOW_WIDTH);
		double randY = (Math.random() * Main.WINDOW_HEIGHT);
		if(randX - NEUTRAL_PLANET_RADIUS < 0) {
			randX += NEUTRAL_PLANET_RADIUS;
		}
		if(randY - NEUTRAL_PLANET_RADIUS < 0) {
			randY += NEUTRAL_PLANET_RADIUS;
		}
		
		boolean isPosFree = true;
		Planet tmp = new Planet(randX, randY, NEUTRAL_PLANET_RADIUS, true, 0, new LittleSpaceship());
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



}
