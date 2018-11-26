package game;

import controllers.Controller;
import game.spaceships.LittleSpaceship;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import utils.MathUtils;
import utils.Utils;

import java.util.ArrayList;

public class Planet {



    private Point2D center;
    private int radius; //optional
    private boolean neutral;

    //spaceship waiting to be launched
    private int available_ships = 0;
    private Spaceship model;

    //the amount of production generated per [time_interval]
    private float production_rate;
    private float total_production;

    private Controller owner;
    private Color color;

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
        for(int i = 0; i < available_ships; i++) {
            spaceships.add(model.getInstance());
        }
        return new Squadron(spaceships, this.owner);
    }

    public void sendShip(int amount){
        //add the squadron to the player controller
        //if player maybe visual/sound alert
        Squadron squad = createSquadron(amount);
        owner.getSquadrons().add(squad);
        //withdraw spaceship in the squadron from the on ground spaceships && assign pos
        ArrayList<double[]> pos = MathUtils.dotAroundACircle(center, radius, squad.getSpaceships().size());
    	/*for(Spaceship sp : squad.getSpaceships()) {
    		on_ground_spaceships.remove(sp);
    	}*/
        for(int i = 0; i < squad.getSpaceships().size(); i++){
            available_ships --;
            squad.getSpaceships().get(i).setPos(new Point2D(pos.get(i)[0], pos.get(i)[1]));
            Spaceship s = squad.getSpaceships().get(i);
            double angle = MathUtils.pointOnOffPlanet(center, s.getPos(), s.getDirection(), false);
            s.rotate(angle);

        }
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
    public void changeOwner(Controller owner) { // add the spaceship as a model
        setOwner(owner);
        this.color = owner.getColor();
        owner.addPlanet(this);
        model.setColor(color);
        this.production_rate = Utils.PLAYER_PRODUCTION_RATE;
    }

    public Spaceship checkCollision(Spaceship spaceship, Controller spaceShipOwner) {
        if(contains(spaceship.getPos()) ) {
            if (this.getOwner() != spaceShipOwner) {
                this.setHit(spaceship.getDamage());
                if (this.available_ships <= 0)
                    this.changeOwner(spaceShipOwner);
            }
            else
                this.addSpaceship();
            return spaceship;
        }
        return null;
    }

    @Override
    public String toString(){
        return "[Planet] position : " + center.getX() + ";" + center.getY() + ", production :" + total_production + ", nb ships : " + available_ships;
    }
    //---------------------DRAW-------------------//

    public void draw(Group root) {
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

}
