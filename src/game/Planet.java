package game;

import controllers.Controller;

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

    public Planet(double posX, double posY, int radius, boolean neutral, float production_rate, Spaceship model){
        this.posX = posX;
        this.posY = posY;
        this.neutral = neutral;
        this.production_rate = production_rate;
        this.model = model;
    }

    public Squadron createSquadron(int size){
        //put the spaceships of on_ground_spaceships in a squadron
        return null;
    }

    public void sendShip(int amount){
        //add the squadron to the player controller
        //if player maybe visual/sound alert
    }

    public void setPosition(int x, int y){
        posX = x;
        posY = y;
    }

    public void addProduction(){
        total_production += production_rate;
        if(total_production >= model.necessary_production){
            total_production -= model.necessary_production;
            on_ground_spaceships.add(model);
            //System.out.println("Added new spaceship");
        }
    }
    
    public boolean collide(Planet p) {
    	double centerDistance = Math.sqrt(Math.pow(this.posX - p.posX, 2) + Math.pow(this.posY - p.posY, 2));    		
		return centerDistance < this.radius + p.radius;
    }

    public String toString(){
        return "[Planet] position : " + posX + ";" + posY + ", production :" + total_production;
    }

    //---------------------GETTER/SETTER-------------------//
    public Controller getOwner() {
        return owner;
    }

    public void setOwner(Controller owner) {
        this.owner = owner;
    }
}
