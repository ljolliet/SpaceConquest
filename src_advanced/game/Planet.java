package game;

import controllers.Controller;
import controllers.HumanController;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import utils.MathUtils;
import utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Planet implements Serializable {

    /**
     * Graphics attributes :
     *  The planet is represented by a circle.
     */

    /**
     * Center of planet.
     */
    private Point2D center;
    /**
     * Radius of the planet.
     */
    private int radius;
    /**
     * Color of the planet.
     */
    private Color color;
    /**
     * Slider allowing the player to choose which percentage of his ships he wants to send.
     */
    private transient Slider sending_quantity = new Slider();

    /**
     * Available ships for launch.
     */
    private int available_ships = 0;
    /**
     * Ships which are waiting for launch, they're not anymore available.
     */
    private int waiting_for_launch = 0;
    /**
     * Target of the ships that will be launched.
     */
    private Planet target = null;
    /**
     * Accessibility map of the ships that will be launched.
     */
    private transient HashMap<Point2D, Boolean> map = new HashMap<>();
    /**
     * Ship that is a model for the planet production, each ship will be a clone of this one.
     */
    private Spaceship model;
    /**
     * The amount of production generated each frame.
     */
    private float production_rate;
    /**
     * The total production amount
     */
    private float total_production;
    /**
     * The owner of this planet.
     */
    private Controller owner;

    /**
     * True if this planet is selected by the human player, false otherwise.
     */
    private boolean selected = false;

    /**
     * TODO
     */
    private Map<Point2D, Integer> collisionPoints = new HashMap<Point2D, Integer>();
    private Color collisionColor = Color.RED; //TODO


    /**
     * Constructor.
     *
     * @param center          Center of the planet as a Point2D.
     * @param radius          Radius of the planet as an int.
     * @param production_rate Production generated each frame, as a float.
     * @param model           Ship that will be cloned for future produced ships.
     */
    public Planet(Point2D center, int radius, float production_rate, Spaceship model) {
        this.center = center;
        this.production_rate = production_rate;
        this.model = model;
        model.setPos(new Point2D(-1, -1)); //so that serialization won't load a null object
        this.radius = radius;
        this.color = model.getColor();

        initSlider();
    }

    /**
     * Initialize Slider.
     */
    public void initSlider() {
        sending_quantity.setPrefWidth(Utils.PLAYER_PLANET_RADIUS);
        sending_quantity.setLayoutX(center.getX() - sending_quantity.getPrefWidth() / 2);
        sending_quantity.setLayoutY(center.getY() + radius);
        sending_quantity.setMin(0);
        sending_quantity.setMax(100);
        sending_quantity.setShowTickLabels(true);
        sending_quantity.setShowTickMarks(true);
        sending_quantity.setMajorTickUnit(25);
        sending_quantity.setMinorTickCount(0);
        sending_quantity.setSnapToTicks(true);
    }

    /**
     * Create a squadron object with size given.
     *
     * @param size Number of ships contained in the squadron.
     * @return An object squadron with as much ships as given size.
     */
    public Squadron createSquadron(int size) {
        ArrayList<Spaceship> spaceships = new ArrayList<>();
        if (size > Utils.WAVE_SIZE_MAX)
            size = Utils.WAVE_SIZE_MAX;

        if (size > waiting_for_launch)
            size = waiting_for_launch;

        waiting_for_launch -= size;

        for (int i = 0; i < size; i++) {
            spaceships.add(model.getInstance());
        }
        return new Squadron(spaceships, this.owner);
    }

    /**
     * Add a squadron to the owner of the planet and set the target for this squadron.
     *
     * @param amount Amount of ship in the squadron.
     * @return The created Squadron.
     */
    public Squadron sendShip(int amount) {
        Squadron squad = createSquadron(amount);

        //withdraw spaceship in the squadron from the on ground spaceships && assign pos
        ArrayList<double[]> pos = MathUtils.dotAroundACircle(center, radius, squad.getSpaceships().size());

        for (int i = 0; i < squad.getSpaceships().size(); i++) {
            squad.getSpaceships().get(i).setPos(new Point2D(pos.get(i)[0], pos.get(i)[1]));
            Spaceship s = squad.getSpaceships().get(i);
            double angle = MathUtils.pointOnOffPlanet(center, s.getPos(), s.getDirection(), false);
            s.rotate(angle);
        }

        if (map == null) {
            System.out.println("MAP NULLE");
        } else {
            squad.setTarget(target, map);

        }
        owner.getSquadrons().add(squad);

        return squad;
    }

    /**
     * Withdraw ships from the available ships and add them to the waiting for launch ships. Actualize the accessibility map and target planet which will be needed when sending those ships.
     *
     * @param amount Amount of ships added to the waiting queue.
     * @param target Planet target of the ships.
     * @param map    Accessibility map of the ships.
     */
    public void addWaitingShips(int amount, Planet target, HashMap<Point2D, Boolean> map) {
        this.target = target;
        this.map = map;
        waiting_for_launch += amount;
        available_ships -= amount;
    }


    /**
     * Add production rate to the total production, and if there is enough production produce a ship and withdraw the production used.
     */
    public void addProduction() {
        total_production += production_rate;
        if (total_production >= model.necessary_production) {
            float producted_ships = total_production / model.necessary_production;
            available_ships += producted_ships;
            total_production -= producted_ships * model.necessary_production;
        }
    }

    /**
     * Add 1 spaceship to the available ships.
     */
    public void addSpaceship() {
        available_ships++;
    }

    /**
     * Check if a position is contained inside the planet's circle.
     *
     * @param p Position that will be checked.
     * @return True if p is contained in the planet's circle, false otherwise.
     */
    public boolean contains(Point2D p) {
        double dist = Math.sqrt(Math.pow(center.getX() - p.getX(), 2) + Math.pow(center.getY() - p.getY(), 2));
        return dist <= this.radius;
    }

    /**
     * Process the distance between a position and the planet's center.
     *
     * @param pos Position which we want to know the distance from the planet's center.
     * @return The distance between pos and the planet's center.
     */
    public double distantOf(Point2D pos) {
        return Math.sqrt(Math.pow(center.getX() - pos.getX(), 2) + Math.pow(center.getY() - pos.getY(), 2));
    }

    /**
     * Change the owner of this planet.
     *
     * @param owner     New owner of the planet.
     * @param spaceship New spaceship used as model for the planet.
     */
    public void changeOwner(Controller owner, Spaceship spaceship) {
        this.waiting_for_launch = 0;
        if (this.owner != null)
            this.owner.getPlanets().remove(this);
        setOwner(owner);
        this.color = owner.getColor();
        this.model = spaceship;
        this.production_rate = Utils.PLAYER_PRODUCTION_RATE;
        owner.addPlanet(this);
    }

    /**
     * Check if there is a collision between a spaceship and the planet. If there is one and that the spaceship is an ennemy, the planet looses available ships, if the spaceship is an allied the planet gains available ships.
     *
     * @param spaceship      The ship colliding with the planet.
     * @param spaceShipOwner The owner of the spaceship.
     * @return null if there is a collision, spaceship otherwise.
     */
    public Spaceship checkCollision(Spaceship spaceship, Controller spaceShipOwner) {
        if (contains(spaceship.getPos())) {
            if (this.getOwner() != spaceShipOwner) {
                this.setHit(spaceship.getDamage());
                if (this.available_ships <= 0)
                    this.changeOwner(spaceShipOwner, spaceship);
            } else
                this.addSpaceship();
            this.collisionPoints.put(spaceship.getPos(), Utils.COLLISION_WAVE_START);
            return spaceship;
        }
        return null;
    }

    /**
     * Check if there isn't a spaceship too close to the planet for the launch of the next wave of spaceship.
     *
     * @return true if there isn't any spaceship too close, false otherwise.
     */
    public boolean isLaunchReady() {
        boolean res = true;

        for (Squadron s : owner.getSquadrons()) {
            for (Spaceship sp : s.getSpaceships()) {
                if (this.distantOf(sp.pos) < radius + Utils.ALLOWED_RANGE_TAKE_OFF)
                    res = false;
            }
        }

        return res;
    }

    /**
     * Get the nearest ennemy planet around this one.
     *
     * @param all_planets A list containing all the planets in the game.
     * @return the nearest ennemy planet.
     */
    public Planet nearestEnnemyPlanet(ArrayList<Planet> all_planets) {
        Planet res = null;

        double minDist = Utils.WINDOW_WIDTH;

        for (Planet p : all_planets) {
            if (p.getOwner() != this.getOwner() && this.distantOf(p.center) < minDist) {
                minDist = this.distantOf(p.center);
                res = p;
            }
        }

        return res;
    }

    @Override
    public String toString() {
        return "[Planet] position : " + center.getX() + ";" + center.getY() + ", production :" + total_production + ", nb ships : " + available_ships;
    }

    //---------------------DRAW-------------------//

    /**
     * Drawing of the planet.
     *
     * @param root Group on which the planet is drawn.
     */
    public void draw(Group root) {
         Map<Point2D, Integer> toDelete = new HashMap<Point2D, Integer>();
        if (selected) {
            Circle c = new Circle(center.getX(), center.getY(), radius + Utils.SELECTED_HALO_SIZE);
            Point2D pScreen = new Point2D(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
            Point2D p2 = root.screenToLocal(pScreen);
            Line l = new Line(center.getX(), center.getY(), p2.getX(), p2.getY());

            l.setStrokeWidth(5);
            l.setStroke(Utils.HALO_COLOR);

            c.setFill(Utils.HALO_COLOR);
            root.getChildren().addAll(c, l);
        }
        for(Map.Entry<Point2D, Integer>  entry : collisionPoints.entrySet())
        {
            Circle arc = new Circle(entry.getKey().getX(), entry.getKey().getY(), entry.getValue());
            arc.setFill(Color.TRANSPARENT);
            arc.setStroke(collisionColor);
            root.getChildren().add(arc);
            entry.setValue(entry.getValue() + Utils.COLLISION_WAVE_INC);
            if(entry.getValue() >= Utils.COLLISION_WAVE_LIMIT)
            toDelete.put(entry.getKey(),entry.getValue());
        }
        for(Point2D p  : toDelete.keySet()) {
            collisionPoints.remove(p);
        }

        Circle c = new Circle(0, 0, radius);
        c.setFill(color);

        //create a text inside a circle
        final Text text = new Text(String.valueOf(available_ships));
        text.setStroke(Color.BLACK);


        //create a layout for circle with text inside
        StackPane stack = new StackPane();

        stack.getChildren().addAll(c, text);
        stack.setLayoutX(center.getX() - radius);
        stack.setLayoutY(center.getY() - radius);

        root.getChildren().add(stack);
        if (owner != null && owner.getClass() == HumanController.class)
            root.getChildren().add(sending_quantity);
    }

    //---------------------SERIALIZATION-------------------//

    /**
     * Write the object in "save.ser"
     *
     * @param oos The stream on which the object is written.
     */
    private void writeObject(ObjectOutputStream oos) {
        try {
            oos.writeObject(center.getX());
            oos.writeObject(center.getY());
            oos.writeObject(color.getRed());
            oos.writeObject(color.getGreen());
            oos.writeObject(color.getBlue());
            oos.writeObject(color.getOpacity());
            oos.writeObject(available_ships);
            oos.writeObject(waiting_for_launch);
            oos.writeObject(total_production);
            oos.writeObject(production_rate);
            oos.writeObject(radius);
            oos.writeObject(model);
            oos.writeObject(owner);
            oos.writeObject(target);
            if (map == null) {
                System.out.println("SAVING NULL MAP");
            }
            oos.writeObject(map.size());
            for (Point2D p : map.keySet()) {
                oos.writeObject(p.getX());
                oos.writeObject(p.getY());
                oos.writeObject(map.get(p));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the planet from "save.ser"
     *
     * @param ois The stream from which the object is read.
     */
    private void readObject(ObjectInputStream ois) {

        try {
            double x = (double) ois.readObject();
            double y = (double) ois.readObject();
            double r = (double) ois.readObject();
            double g = (double) ois.readObject();
            double b = (double) ois.readObject();
            double opacity = (double) ois.readObject();
            available_ships = (int) ois.readObject();
            waiting_for_launch = (int) ois.readObject();
            total_production = (float) ois.readObject();
            production_rate = (float) ois.readObject();
            radius = (int) ois.readObject();
            model = (Spaceship) ois.readObject();
            owner = (Controller) ois.readObject();
            target = (Planet) ois.readObject();
            int mapSize = (int) ois.readObject();
            map = new HashMap<>();
            for (int i = 0; i < mapSize; i++) {
                map.put(new Point2D((double) ois.readObject(), (double) ois.readObject()), (Boolean) ois.readObject());
            }
            center = new Point2D(x, y);
            color = new Color(r, g, b, opacity);
            sending_quantity = new Slider();
            initSlider();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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

    public Point2D getCenter() {
        return center;
    }

    public int getAvailable_ships() {
        return available_ships;
    }

    public void setAvailable_ships(int available_ships) {
        this.available_ships = available_ships;
    }

    public void setHit(int damage) {
        this.available_ships -= damage;
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

    public Slider getSending_quantity() {
        return sending_quantity;
    }

}
