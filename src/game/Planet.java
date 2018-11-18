package game;

import controllers.Controller;
import game.spaceships.LittleSpaceship;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import utils.MathUtils;

import java.util.ArrayList;

public class Planet {



    private Point2D center;
    private int radius; //optional
    private boolean neutral;

    //spaceship waiting to be launched
    private ArrayList<Spaceship> on_ground_spaceships = new ArrayList<>();
    private Spaceship model;

    //the amount of production generated per [time_interval]
    private float production_rate;
    private float total_production;

    private Controller owner;
    private Color color;

    public Planet(double posX, double posY, int radius, boolean neutral, float production_rate, Spaceship model, Color color){
        center = new Point2D(posX,posY);
        this.neutral = neutral;
        this.production_rate = production_rate;
        this.model = model;
        this.radius = radius;
        this.color = color;

    }

    //----------------NOT TESTED-------------------------------//
    public Squadron createSquadron(int size){
        ArrayList<Spaceship> spaceships = new ArrayList<>();
        //use "i" instead of spaceship sp : on_ground_spaceship in case that a spaceship is added during execution ??
    	for(Spaceship sp : on_ground_spaceships) {
    		spaceships.add(sp);
    	}
        return new Squadron(spaceships);
    }

    //---------------NOT TESTED---------------------------//
    public void sendShip(int amount){
        //add the squadron to the player controller
        //if player maybe visual/sound alert
    	Squadron squad = createSquadron(amount);
    	owner.getSquadrons().add(squad);
    	//withdraw spaceship in the squadron from the on ground spaceships && assign pos
        ArrayList<double[]> pos = MathUtils.dotAroundACircle(center.getX(), center.getY(), radius, squad.getSpaceships().size());
    	/*for(Spaceship sp : squad.getSpaceships()) {
    		on_ground_spaceships.remove(sp);
    	}*/
    	for(int i = 0; i < squad.getSpaceships().size(); i++){
    	    on_ground_spaceships.remove(squad.getSpaceships().get(i));
    	    squad.getSpaceships().get(i).setPos(new Point2D(pos.get(i)[0], pos.get(i)[1]));
        }
    }


    public void addProduction(){
        total_production += production_rate;
        if(total_production >= model.necessary_production){
            total_production -= model.necessary_production;
            on_ground_spaceships.add(model.getInstance());
            System.out.println("ADDED NEW SPACESHIP");
        }
    }
    
    public boolean collide(Planet p) {
    	double centerDistance = Math.sqrt(Math.pow(center.getX() - p.center.getX(), 2) + Math.pow(center.getY() - center.getY(), 2));
		return centerDistance < this.radius + p.radius;
    }
    
    public double distantOf(double[] pos) {
    	return Math.sqrt(Math.pow(center.getX() - pos[0], 2) + Math.pow(center.getY() - pos[1], 2));
    }

    public String toString(){
        return "[Planet] position : " + center.getX() + ";" + center.getY() + ", production :" + total_production + ", nb ships : " + on_ground_spaceships.size();
    }
    //---------------------DRAW-------------------//

    public void draw(Group root) {
        Circle c = new Circle(0,0,radius);
        c.setFill(color);

        //create a text inside a circle
        final Text text = new Text (String.valueOf(this.on_ground_spaceships.size()));
        text.setStroke(Color.BLACK);

        //create a layout for circle with text inside
         StackPane stack = new StackPane();

        stack.getChildren().addAll(c, text);
        stack.setLayoutX(center.getX()-radius);
        stack.setLayoutY(center.getY()-radius);
        root.getChildren().add(stack);


    }
    //---------------------GETTER/SETTER-------------------//

    public Controller getOwner() {
        return owner;
    }

    public void setOwner(Controller owner) {
        this.owner = owner;
    }

    public int getRadius() {
        return radius;
    }

    public Point2D getCenter() { return center; }

    public void setCenter(Point2D center) { this.center = center; }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public ArrayList<Spaceship> getOn_ground_spaceships() {
        return on_ground_spaceships;
    }

    public void setOn_ground_spaceships(ArrayList<Spaceship> on_ground_spaceships) {
        this.on_ground_spaceships = on_ground_spaceships;
    }

}
