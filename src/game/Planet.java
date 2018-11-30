package game;

import com.sun.org.apache.xpath.internal.operations.Bool;
import controllers.Controller;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import utils.MathUtils;
import utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Planet {



    private Point2D center;
    private int radius; //optional
    private boolean neutral;

    //spaceship waiting to be launched
    private int available_ships = 0;

    private int waiting_for_launch = 0;
    private Planet target = null;
    private HashMap<Point2D, Boolean> map = null;

    private Spaceship model;

    //the amount of production generated per [time_interval]
    private float production_rate;
    private float total_production;

    private Controller owner;
    private Color color;

    private boolean selected = false;

    public Planet(Point2D center, int radius, boolean neutral, float production_rate, Spaceship model){
        this.center = center;
        this.neutral = neutral;
        this.production_rate = production_rate;
        this.model = model;
        this.radius = radius;
        this.color = model.getColor();

    }


    public Squadron createSquadron(int size){
        ArrayList<Spaceship> spaceships = new ArrayList<>();
        //use "i" instead of spaceship sp : on_ground_spaceship in case that a spaceship is added during execution ??
        if(size > Utils.WAVE_SIZE_MAX)
            size = Utils.WAVE_SIZE_MAX;

        if(size > waiting_for_launch)
            size = waiting_for_launch;

        waiting_for_launch -= size;

        for(int i = 0; i < size; i++) {
            spaceships.add(model.getInstance());
        }
        return new Squadron(spaceships, this.owner);
    }

    public Squadron sendShip(int amount){
        //add the squadron to the player controller
        //if player maybe visual/sound alert
        Squadron squad = createSquadron(amount);

        //withdraw spaceship in the squadron from the on ground spaceships && assign pos
        ArrayList<double[]> pos = MathUtils.dotAroundACircle(center, radius, squad.getSpaceships().size());

        for(int i = 0; i < squad.getSpaceships().size(); i++){
            squad.getSpaceships().get(i).setPos(new Point2D(pos.get(i)[0], pos.get(i)[1]));
            Spaceship s = squad.getSpaceships().get(i);
            double angle = MathUtils.pointOnOffPlanet(center, s.getPos(), s.getDirection(), false);
            s.rotate(angle);
        }

        squad.setTarget(target, map);
        owner.getSquadrons().add(squad);

		return squad;
    }

    public void addWaitingShips(int amount, Planet target, HashMap<Point2D,Boolean> map)
    {
        this.target = target;
        this.map = map;
        waiting_for_launch += amount;
        available_ships -= amount;
    }


    public void addProduction(){
        total_production += production_rate;
        if(total_production >= model.necessary_production){
            total_production -= model.necessary_production;
            available_ships ++;
        }
    }

    public void addSpaceship(){
        available_ships ++;
    }

    public boolean collide(Planet p) {
        double centerDistance = Math.sqrt(Math.pow(center.getX() - p.center.getX(), 2) + Math.pow(center.getY() - p.center.getY(), 2));
        return centerDistance < this.radius + p.radius;
    }

    public boolean contains(Point2D p){
        double dist = Math.sqrt(Math.pow(center.getX() - p.getX(), 2) + Math.pow(center.getY() - p.getY(), 2));
        return dist <= this.radius;
    }

    public boolean containsHitbox(Point2D p) {
        double dist = Math.sqrt(Math.pow(center.getX() - p.getX(), 2) + Math.pow(center.getY() - p.getY(), 2));
        return dist <= (this.radius + Utils.ADDITIONAL_HITBOX_RANGE);
    }

    public double distantOf(Point2D pos) {
        return Math.sqrt(Math.pow(center.getX() - pos.getX(), 2) + Math.pow(center.getY() - pos.getY(), 2));
    }

    public void changeOwner(Controller owner, Spaceship spaceship) { // add the spaceship as a model
        if(this.owner != null)
            this.owner.getPlanets().remove(this);
        setOwner(owner);
        this.color = owner.getColor();
        this.model = spaceship;
        this.production_rate = Utils.PLAYER_PRODUCTION_RATE;
        owner.addPlanet(this);
    }

    public Spaceship checkCollision(Spaceship spaceship, Controller spaceShipOwner) {
        if(contains(spaceship.getPos()) ) {
            if (this.getOwner() != spaceShipOwner) {
                this.setHit(spaceship.getDamage());
                if (this.available_ships <= 0)
                    this.changeOwner(spaceShipOwner,spaceship);
            }
            else
                this.addSpaceship();
            return spaceship;
        }
        return null;
    }

    public boolean isLaunchReady(){
        boolean res = true;

        for(Squadron s : owner.getSquadrons()){
            for(Spaceship sp : s.getSpaceships()){
                if(this.distantOf(sp.pos) < radius + Utils.ALLOWED_RANGE_TAKE_OFF)
                    res = false;
            }
        }

        return res;
    }

    @Override
    public String toString(){
        return "[Planet] position : " + center.getX() + ";" + center.getY() + ", production :" + total_production + ", nb ships : " + available_ships;
    }
    
    public Planet nearestEnnemyPlanet(ArrayList<Planet> all_planets) {
		Planet res = null;
		
		double minDist = Utils.WINDOW_WIDTH;
		
		for(Planet p : all_planets) {
			if(p.getOwner() != this.getOwner() && this.distantOf(p.center) < minDist) {
				minDist = this.distantOf(p.center);
				res = p;
			}
		}
    	
    	return res;
    }
    
    //---------------------DRAW-------------------//

    public void draw(Group root) {

        if(selected){
            Circle c = new Circle(center.getX(),center.getY(), radius + Utils.SELECTED_HALO_SIZE);
            Point2D pScreen = new Point2D(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
            Point2D p2 = root.screenToLocal(pScreen);
            Line l = new Line(center.getX(), center.getY(),p2.getX(),p2.getY());

            l.setStrokeWidth(5);
            l.setStroke(Utils.HALO_COLOR);

            c.setFill(Utils.HALO_COLOR);
            root.getChildren().addAll(c,l);
        }

        Circle c = new Circle(0,0,radius);
        c.setFill(color);

        //create a text inside a circle
        final Text text = new Text (String.valueOf(available_ships));
        text.setStroke(Color.BLACK);


        //create a layout for circle with text inside
        StackPane stack = new StackPane();

        stack.getChildren().addAll(c, text);
        stack.setLayoutX(center.getX()-radius);
        stack.setLayoutY(center.getY()-radius);

        root.getChildren().add(stack);
    }
    //---------------------GETTER/SETTER-------------------//

    public void setOwner(Controller owner) {
        this.owner = owner;
    }

    public Controller getOwner() {
        return owner;
    }

    public int getRadius() {
        return radius;
    }

    public Point2D getCenter() { return center; }

    public int getAvailable_ships() {
        return available_ships;
    }

    public void setAvailable_ships(int available_ships) {
        this.available_ships = available_ships;
    }

    public void setHit(int damage) {
        this.available_ships-=damage;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getWaiting_for_launch() {
        return waiting_for_launch;
    }

    public void setWaiting_for_launch(int waiting_for_launch) {
        this.waiting_for_launch = waiting_for_launch;
    }


    public Planet getTarget() {
        return target;
    }

    public void setTarget(Planet target) {
        this.target = target;
    }

    public HashMap<Point2D, Boolean> getMap() {
        return map;
    }

    public void setMap(HashMap<Point2D, Boolean> map) {
        this.map = map;
    }


}
