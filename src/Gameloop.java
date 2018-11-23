import game.Planet;
import game.Squadron;
import game.spaceships.LittleSpaceship;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import controllers.ComputerController;
import controllers.Controller;
import controllers.HumanController;


/**
 * @author tguesdon, ljolliet
 * 
 */
public class Gameloop extends AnimationTimer{
    //this class will update all graphics change
    //ex : ships' deplacement


    private Group root;
    private Scene scene;

    private ArrayList<Controller> controllers = new ArrayList<>();
    private ArrayList<Planet> planets = new ArrayList<>();

    private HashMap<Point2D, Boolean> accessibilityMap = new HashMap<>();

    /**
     * @param root
     * @param scene
     */
    public Gameloop(Group root, Scene scene){
        this.root = root;
        this.scene = scene;
        init();
    }

    /* (non-Javadoc)
     * @see javafx.animation.AnimationTimer#handle(long)
     */
    @Override
    public void handle(long now) {
        draw();
        actualizeProduction();
        actualizeShipPos();
    }

    //--------------------INIT-------------//

    
    /**
     * Call all initialization function.
     */
    public void init(){
        initPlayers();
        initPlanets();
        initEvents();
        initAccessibilityMap();
    }

    
    /**
     * Create a human controller for the player 0 and a computer controller for every other player
     */
    public void initPlayers(){
        controllers.add(new HumanController(Utils.PLANET_COLOR.get(0)));
        //start at 1 because 0 is the human
        for(int i = 1; i < Utils.NB_PLAYER; i++){
            controllers.add(new ComputerController(Utils.PLANET_COLOR.get(i)));
        }
    }

    
    /**
     * Init all planets (player & neutral)
     */
    public void initPlanets(){
        initPlayerPlanets();
        initNeutralPlanets(Utils.NB_NEUTRAL_PLANET);
    }

    /**
     * Create planets for players using getPlayerPlanetPos() to get a free position
     */
    public void initPlayerPlanets(){
        for(int i = 0; i < controllers.size(); i ++){
        	Point2D pos = getPlayerPlanetPos();
            Planet p = new Planet(pos, Utils.PLAYER_PLANET_RADIUS, false, 1, new LittleSpaceship(controllers.get(i).getColor()));// add color
            controllers.get(i).getPlanets().add(p);
            p.setOwner(controllers.get(i));
            planets.add(p);
        }
    }
    
    
    /**
     * @return a free position for player's planet
     */
    public Point2D getPlayerPlanetPos(){
    	int maxX = Utils.WINDOW_WIDTH - Utils.PLAYER_PLANET_RADIUS;
    	int maxY = Utils.WINDOW_HEIGHT - Utils.PLAYER_PLANET_RADIUS;
    	int min = Utils.PLAYER_PLANET_RADIUS;
    	
    	int x = new Random().nextInt(maxX + 1 - min) + min; 
    	int y = new Random().nextInt(maxY + 1 - min) + min; 		
    	
    	Point2D pos = new Point2D(x,y);
    	
    	boolean isFree = true;
    	
    	for(Planet p : planets) {
    		if(p.distantOf(pos) < Utils.DISTANCE_BETWEEN_PLAYERS) {
    			isFree = false;
    		}
    	}
    	
    	if(isFree) {
    		return pos;
    	}else {
    		return getPlayerPlanetPos();
    	}
       }

  
    /**
     * Add the neutral planets. 
     * @param number of neutral planets
     */
    public void initNeutralPlanets(int nb){
    	for(int i = 0; i < nb; i++) {
    		addRandomNeutralPlanet();    		
    	}
    }
    
    /**
     * Generate a random position that isn't part of an already existing planet and create a neutral planet with this position.
     */
    public void addRandomNeutralPlanet() {
    	int maxX = Utils.WINDOW_WIDTH - Utils.NEUTRAL_PLANET_RADIUS;
    	int maxY = Utils.WINDOW_HEIGHT - Utils.NEUTRAL_PLANET_RADIUS;
    	int min = Utils.NEUTRAL_PLANET_RADIUS;
    	
    	int randX = new Random().nextInt(maxX + 1 - min) + min; 
    	int randY = new Random().nextInt(maxY + 1 - min) + min; 			
		
		boolean isPosFree = true;
		Planet tmp = new Planet(new Point2D(randX, randY), Utils.NEUTRAL_PLANET_RADIUS, true, 0, new LittleSpaceship(Utils.NEUTRAL_PLANET_COLOR));
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

    
    /**
     * Fill the accessibility map by creating a grid of points and checking if they are part of a planet
     */
    public void initAccessibilityMap(){
        long t0 = System.currentTimeMillis();
        for(int i = 0; i < Utils.WINDOW_WIDTH; i += Utils.COLUMN_SIZE){
            for(int j = 0; j < Utils.WINDOW_HEIGHT; j += Utils.COLUMN_SIZE){
                Point2D realPos = new Point2D(i,j);
                boolean accessible = true;
                for(Planet p : planets){
                    if(p.contains(realPos))
                        accessible = false;
                }
                accessibilityMap.put(realPos,accessible);
            }
        }
        System.out.println("Accessibility Map generated in : " + (System.currentTimeMillis() - t0) + "ms");
    }

    //--------------------------EVENTS-------------------//

    /**
     * Declaration of all events triggered by the human
     */
    public void initEvents(){
        HumanController hc = (HumanController)controllers.get(0);

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(hc.isOnHumanPlanet(event.getX(), event.getY())){
                    Planet selected = hc.getHumanPlanetClic(event.getX(), event.getY());
                    hc.launchShip(selected);

                }
                else if(hc.isOnPlanet(event.getX(), event.getY(), planets)){
                    Planet selected = hc.getPlanetClic(event.getX(), event.getY(), planets);
                    hc.setTarget(selected, accessibilityMap);
                }
            }
        });
    }

    //-------------PROCESS---------------------//

    
    /**
     * Actualize the production of all planets 
     */
    public void actualizeProduction(){
        for(Controller c : controllers){
            for(Planet p : c.getPlanets()){
                p.addProduction();
            }
        }
    }

    
    /**
     * Actualize the position of all controller's ships
     */
    public void actualizeShipPos(){
        for(Controller c : controllers) // refactor in a method
            for( Squadron s : c.getSquadrons())
                s.sendToTarget();
    }



    /**
     * Call every draw function of the game's elements
     */
    public void draw(){
        root.getChildren().removeAll(root.getChildren()); // clear root

        for(Planet p : planets) // draw all planets
            p.draw(root);

        for(Controller c : controllers)
            for(Squadron s : c.getSquadrons()){
                s.draw(root);
            }
    }



}
