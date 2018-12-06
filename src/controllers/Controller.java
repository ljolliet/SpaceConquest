package controllers;

import game.Planet;
import game.Squadron;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public abstract class Controller implements Serializable{


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

    private void writeObject(ObjectOutputStream oos){
        try {
            oos.writeObject(this.planets);
            oos.writeObject(this.squadrons);
            oos.writeObject(color.getRed());
            oos.writeObject(color.getGreen());
            oos.writeObject(color.getBlue());
            oos.writeObject(color.getOpacity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream ois){
        try {
            planets = (ArrayList<Planet>)ois.readObject();
            squadrons = (ArrayList<Squadron>)ois.readObject();
            double r = (double)ois.readObject();
            double g = (double)ois.readObject();
            double b = (double)ois.readObject();
            double opacity = (double)ois.readObject();
            color = new Color(r,g,b,opacity);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
