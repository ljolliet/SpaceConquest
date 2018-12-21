package controllers;

import game.Planet;
import game.Squadron;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A controller contains the function managing the player's data. Ships, Planets, ships movement etc...
 * There is two types of controller, the HumanController responds to events called by the human player with the mouse, and the ComputerController which function are called automaticaly.
 */
public abstract class Controller implements Serializable{


    /**
     * Planets belonging to the controller.
     */
    protected ArrayList<Planet> planets = new ArrayList<>();

    /**
     * Squadrons belonging to the controller.
     */
    protected ArrayList<Squadron> squadrons = new ArrayList<>();

    /**
     * Color of the controller.
     */
    protected Color color;

    /**
     * Controller constructor.
     * @param color Color of the controller.
     */
    public Controller(Color color){
        this.color = color;
    }

    /**
     * Add a planet to the controller's planets.
     * @param planet The planet to be added.
     */
    public void addPlanet(Planet planet){
        this.planets.add(planet);
    }

    /**
     * Save the controller's data in "save.ser".
     * @param oos The stream on which data will be written.
     */
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

    /**
     * Read the controller from "save.ser".
     * @param ois The stream from which data will be read.
     */
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

}
