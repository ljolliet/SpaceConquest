package controllers;

import game.Planet;
import game.Squadron;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public abstract class Controller {

    private ArrayList<Planet> planets = new ArrayList<>();
    private ArrayList<Squadron> squadrons = new ArrayList<>();
    private Color color;

    public Controller(Color color){
        this.color = color;
    }

    public ArrayList<Squadron> getSquadrons() {
        return squadrons;
    }

    public void setSquadrons(ArrayList<Squadron> squadrons) {
        this.squadrons = squadrons;
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
}
