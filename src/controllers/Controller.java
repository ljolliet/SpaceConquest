package controllers;

import game.Planet;
import game.Squadron;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public abstract class Controller {


    protected ArrayList<Planet> planets = new ArrayList<>();
    protected ArrayList<Squadron> squadrons = new ArrayList<>();
    
    protected Color color;

    public Controller(Color color){
        this.color = color;
    }


    public ArrayList<Squadron> getSquadrons() {
        return squadrons;
    }

    public ArrayList<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(ArrayList<Planet> planets) {
        this.planets = planets;
    }

    public Color getColor() {
        return color;
    }

    public void addPlanet(Planet planet){
        this.planets.add(planet);
    }

}
