package game;

import controllers.Controller;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Planet {

    private double posX;
    private double posY;
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
        this.posX = posX;
        this.posY = posY;
        this.neutral = neutral;
        this.production_rate = production_rate;
        this.model = model;
        this.radius = radius;
        this.color = color;

    }

    //----------------NOT TESTED-------------------------------//
    public Squadron createSquadron(int size){
        ArrayList<Spaceship> spaceships = new ArrayList<>();
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
    	//withdraw spaceship in the squadron from the on ground spaceships
    	for(Spaceship sp : squad.getSpaceships()) {
    		on_ground_spaceships.remove(sp);
    	}
    }


    public void addProduction(){
        total_production += production_rate;
        if(total_production >= model.necessary_production){
            total_production -= model.necessary_production;
            on_ground_spaceships.add(model);
            System.out.println("ADDED NEW SPACESHIP");
        }
    }
    
    public boolean collide(Planet p) {
    	double centerDistance = Math.sqrt(Math.pow(this.posX - p.posX, 2) + Math.pow(this.posY - p.posY, 2));    		
		return centerDistance < this.radius + p.radius;
    }
    
    public double distantOf(double[] pos) {
    	return Math.sqrt(Math.pow(this.posX - pos[0], 2) + Math.pow(this.posY - pos[1], 2));    		
    }

    public String toString(){
        return "[Planet] position : " + posX + ";" + posY + ", production :" + total_production;
    }
    //---------------------DRAW-------------------//

    public void draw(Group root) {
        Circle c = new Circle(posX,posY,radius);
        c.setFill(color);
        root.getChildren().add(c);

    }
    //---------------------GETTER/SETTER-------------------//

    public Controller getOwner() {
        return owner;
    }

    public void setOwner(Controller owner) {
        this.owner = owner;
    }
}
