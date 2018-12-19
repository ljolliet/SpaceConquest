
import controllers.ComputerController;
import controllers.Controller;
import controllers.HumanController;
import controllers.TypeAI;
import game.Planet;
import game.Squadron;
import game.spaceships.LittleSpaceship;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * @author tguesdon, ljolliet
 */
public class GameLoop extends AnimationTimer implements Serializable {

    private Group root;
    private Scene scene;

    /**
     * Boolean representing whether or not the player is doing a drag & drop movement.
     */
    public  boolean dragging = false;

    /**
     * List of all controllers in the game.
     */
    private ArrayList<Controller> controllers = new ArrayList<>();
    /**
     * List of all planets in the game.
     */
    private ArrayList<Planet> planets = new ArrayList<>();

    /**
     * Accessibility map. If accessibilityMap.get(Point p) is true, it means that p is accessible.
     */
    private HashMap<Point2D, Boolean> accessibilityMap = new HashMap<>();

    /**
     * @param root  containing the object that will be drawn
     * @param scene on which root is drawn
     */
    public GameLoop(Group root, Scene scene) {
        this.root = root;
        this.scene = scene;
        init();
    }

    /**
     * @see javafx.animation.AnimationTimer#handle(long)
     * @param now
     */
    @Override
    public void handle(long now) {
        draw();
        actualizeProduction();
        actualizeShipPos();
        actualizeShipSending();
        actualizeActionsAI();
    }

    //--------------------INIT-------------//


    /**
     * Call all initialization function.
     */
    private void init() {
        initPlayers();
        initPlanets();
        initEvents();
        initAccessibilityMap();
        GUIController.generateMenuBar();
    }


    /**
     * Create a human controller for the player 0 and a computer controller for every other player
     */
    private void initPlayers() {
        controllers.add(new HumanController(Utils.PLANET_COLOR.get(0)));
        //start at 1 because 0 is the human
        for (int i = 1; i < Utils.NB_PLAYER; i++) {
            int rand = (int)(Math.random() * 3);
            TypeAI type;
            switch (rand){
                case 0:
                    type = TypeAI.CLASSIC;
                    break;
                case 1:
                    type = TypeAI.SAFE;
                    break;
                case 2:
                    type =TypeAI.AGGRESSIVE;
                    break;
                default:
                    type = TypeAI.CLASSIC;
                    break;
            }

            controllers.add(new ComputerController(Utils.PLANET_COLOR.get(i), type));
        }
    }


    /**
     * Init all planets (player & neutral)
     */
    private void initPlanets() {
        initPlayerPlanets();
        initNeutralPlanets(Utils.NB_NEUTRAL_PLANET);
    }

    /**
     * Create planets for players using getPlayerPlanetPos() to get a free position
     */
    private void initPlayerPlanets() {
        for (Controller c : controllers) {
            Point2D pos = getPlayerPlanetPos();

            double maxProdRate = Utils.PLAYER_PRODUCTION_RATE + Utils.PRODUCTION_VARIATION * Utils.PLAYER_PRODUCTION_RATE / 100;
            double minProdRate = Utils.PLAYER_PRODUCTION_RATE - Utils.PRODUCTION_VARIATION * Utils.PLAYER_PRODUCTION_RATE / 100;
            double randProd = minProdRate + (maxProdRate - minProdRate) * (new Random()).nextDouble();

            Planet p = new Planet(pos, Utils.PLAYER_PLANET_RADIUS, (float)randProd, new LittleSpaceship(c.getColor()));// add color
            c.getPlanets().add(p);
            p.setOwner(c);
            planets.add(p);
        }
    }


    /**
     * @return a free position for player's planet
     */
    private Point2D getPlayerPlanetPos() {
        int maxX = Utils.WINDOW_WIDTH - Utils.PLAYER_PLANET_RADIUS;
        int maxY = Utils.WINDOW_HEIGHT - Utils.PLAYER_PLANET_RADIUS;
        int min = Utils.PLAYER_PLANET_RADIUS;

        int x = new Random().nextInt(maxX + 1 - min) + min;
        int y = new Random().nextInt(maxY + 1 - min) + min;

        Point2D pos = new Point2D(x, y);

        boolean isFree = true;

        if(pos.distance(new Point2D(pos.getX(), 0)) < Utils.DISTANCE_BORDER || pos.distance(new Point2D(0, pos.getY())) < Utils.DISTANCE_BORDER
                || pos.distance(new Point2D(pos.getX(), Utils.WINDOW_HEIGHT)) < Utils.DISTANCE_BORDER || pos.distance(new Point2D(Utils.WINDOW_WIDTH, pos.getY())) < Utils.DISTANCE_BORDER )
            isFree = false;

        if(isFree){
            for (Planet p : planets) {
                if (p.distantOf(pos) < Utils.DISTANCE_BETWEEN_PLAYERS) {
                    isFree = false;
                }
            }
        }

        if (isFree) {
            return pos;
        } else {
            return getPlayerPlanetPos();
        }
    }


    /**
     * Add the neutral planets.
     *
     * @param amount of neutral planets
     */
    private void initNeutralPlanets(int amount) {
        for (int i = 0; i < amount; i++) {
            addRandomNeutralPlanet();
        }
    }

    /**
     * Generate a random position that isn't part of an already existing planet and create a neutral planet with this position.
     */
    private void addRandomNeutralPlanet() {
        int maxX = Utils.WINDOW_WIDTH - Utils.NEUTRAL_PLANET_RADIUS;
        int maxY = Utils.WINDOW_HEIGHT - Utils.NEUTRAL_PLANET_RADIUS;
        int min = Utils.NEUTRAL_PLANET_RADIUS;

        int randX = new Random().nextInt(maxX + 1 - min) + min;
        int randY = new Random().nextInt(maxY + 1 - min) + min;
        boolean isPosFree = true;
        Planet tmp = new Planet(new Point2D(randX, randY), Utils.NEUTRAL_PLANET_RADIUS, 0, new LittleSpaceship(Utils.NEUTRAL_PLANET_COLOR));

        int maxHp = Utils.NEUTRAL_HP_RANGE + Utils.HP_VARIATION * Utils.NEUTRAL_HP_RANGE / 100;
        int minHp = Utils.NEUTRAL_HP_RANGE - Utils.HP_VARIATION * Utils.NEUTRAL_HP_RANGE / 100;
        int hp = new Random().nextInt(maxHp + 1 - minHp) + minHp;

        tmp.setAvailable_ships(hp);

        if(tmp.getCenter().distance(new Point2D(tmp.getCenter().getX(), 0)) < Utils.DISTANCE_BORDER || tmp.getCenter().distance(new Point2D(0, tmp.getCenter().getY())) < Utils.DISTANCE_BORDER
                || tmp.getCenter().distance(new Point2D(tmp.getCenter().getX(), Utils.WINDOW_HEIGHT)) < Utils.DISTANCE_BORDER || tmp.getCenter().distance(new Point2D(Utils.WINDOW_WIDTH, tmp.getCenter().getY())) < Utils.DISTANCE_BORDER )
            isPosFree = false;

        for (Planet p : planets) {
            if (tmp.distantOf(p.getCenter()) < Utils.DISTANCE_BETWEEN_NEUTRAL) {
                isPosFree = false;
            }
        }

        if (isPosFree) {
            planets.add(tmp);
        } else {
            addRandomNeutralPlanet();
        }
    }


    /**
     * Fill the accessibility map by creating a grid of points and checking if they are part of a planet
     */
    private void initAccessibilityMap() {
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < Utils.WINDOW_WIDTH; i += Utils.COLUMN_SIZE) {
            for (int j = 0; j < Utils.WINDOW_HEIGHT; j += Utils.COLUMN_SIZE) {
                Point2D pos = new Point2D(i, j);
                boolean accessible = true;
                for (Planet p : planets) {
                    if (p.contains(pos))  //.containsHitbox(pos)
                        accessible = false;
                }
                accessibilityMap.put(pos, accessible);
            }
        }
        System.out.println("Accessibility Map generated in : " + (System.currentTimeMillis() - t0) + "ms");
    }

    //--------------------------EVENTS-------------------//

    /**
     * Declaration of all events triggered by the human
     */
    private void initEvents() {
        HumanController hc = (HumanController) controllers.get(0);

        scene.setOnMouseClicked(event -> {
                for (Squadron s : hc.getSquadrons())
                    if (s.contains(new Point2D(event.getX(), event.getY()))) {
                        hc.setSelectedSquadron(s);
                    }
        });

        scene.setOnDragDetected(event->{
        	if(hc.isOnHumanPlanet(event.getX(), event.getY())) {
        		dragging = true;
        		Planet p = hc.getHumanPlanetClick(event.getX(), event.getY());
        		p.setSelected(true);
        		hc.setSelectedPlanet(p);
        	}
        	});

        scene.setOnMouseReleased(event->{
        	if(hc.isOnPlanet(event.getX(), event.getY(), planets) || hc.isOnHumanPlanet(event.getX(), event.getY())) {
        	    if (dragging) { //if mouse released during a drag action
                    hc.getSelectedPlanet().addWaitingShips((int)(hc.getSelectedPlanet().getAvailable_ships() * (hc.getSelectedPlanet().getSending_quantity().getValue()/100)), hc.getPlanetClic(event.getX(), event.getY(), planets), accessibilityMap);
                    dragging = false;
                }else{ //if mouse was released not during a drag action
                    if (hc.getSelectedSquadron() != null) {
                        hc.setTarget(hc.getPlanetClic(event.getX(), event.getY(), planets), accessibilityMap);
                    }
                }
            }

        	if(hc.getSelectedPlanet() != null)
                hc.getSelectedPlanet().setSelected(false);

        });

    }

    //-----------------PROCESS---------------------//


    /**
     * Actualize the production of all planets
     */
    private void actualizeProduction() {
        for (Controller c : controllers) {
            for (Planet p : c.getPlanets()) {
                p.addProduction();
            }
        }
    }


    /**
     * Actualize the position of all controller's ships
     */
    private void actualizeShipPos() {
        if (Utils.OPTIMIZED) {
            for (Controller c : controllers) { // refactor in a method
                ArrayList<Squadron> toRemove = new ArrayList<>();
                for (Squadron s : c.getSquadrons()) {
                    s.sendToTarget();
                    if (s.getSpaceships().isEmpty()) {
                        toRemove.add(s);
                    }
                }
                for (Squadron s : toRemove) {
                    c.getSquadrons().remove(s);
                }
            }
        } else {
            for (Controller c : controllers) // refactor in a method
                for (Squadron s : c.getSquadrons())
                    s.sendToTarget();
        }


    }

    //COMPLEXITY TO BAD ?
    private void actualizeShipSending(){
        for(Controller c : controllers){
            for(Planet p : c.getPlanets()){
                if(p.isLaunchReady() && p.getWaiting_for_launch() > 0){
                    if(c.getClass() == HumanController.class)
                        ((HumanController)c).launchShip(p);
                    else {
                    	p.sendShip(Utils.WAVE_SIZE);
                    }
                    	
                }
            }
        }
    }
    
    private void actualizeActionsAI(){
    	for(Controller c : controllers) {
    		if(c.getClass() == ComputerController.class) {
    			((ComputerController)c).process(planets, accessibilityMap);
    		}
    	}
    }


    /**
     * Call every draw function of the game's elements
     */
    private void draw() {
        root.getChildren().removeAll(root.getChildren()); // clear root
        //if(!Utils.OPTIMIZED)
            GUIController.drawBackground(root, false);

        for (Planet p : planets) // draw all planets
            p.draw(root);

        for (Controller c : controllers)
            for (Squadron s : c.getSquadrons()) {
                s.draw(root);
            }
        GUIController.displayMenuBar(root);


    }

    public void writeObject(ObjectOutputStream oos){
        try {
            oos.writeObject(planets);
            oos.writeObject(controllers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readObject(ObjectInputStream ois){
        try {
            planets = (ArrayList<Planet>) ois.readObject();
            controllers = (ArrayList<Controller>)ois.readObject();
            accessibilityMap = new HashMap<>();
            initAccessibilityMap();
            for(Controller c : controllers){
                for(Squadron s : c.getSquadrons()){
                    s.setTarget(s.getTarget(), accessibilityMap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setRoot(Group root) {
        this.root = root;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
