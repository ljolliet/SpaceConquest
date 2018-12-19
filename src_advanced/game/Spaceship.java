package game;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import utils.MathUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

public abstract class  Spaceship implements Serializable{

    /**
     * Damage made by this spaceship when hitting an ennemy planet.
     */
    protected int damage;
    /**
     * Amount of production to create this spaceship.
     */
    protected int necessary_production;
    /**
     * Speed of this spaceship.
     */
    protected double speed;
    /**
     * Angle of the spaceship, relative to direction's vector.
     */
    protected int angle = 0;
    /**
     * Lenght of the spaceship.
     */
    protected double length;
    /**
     * Position of the spaceship.
     */
    protected Point2D pos;
    /**
     * Color of the spaceship.
     */
    protected Color color;

    /**
     * List of every steps (position) between this spaceship and his target.
     */
    protected LinkedList<Point2D> steps; //list of coordinates to get to the target
    /**
     * Vector corresponding to the direction of the spaceship, initialized at (0,0)
     */
    protected Point2D direction = Point2D.ZERO;

    /**
     * Constructor of the spaceship.
     * @param color
     */
    public Spaceship(Color color) {
        this.color = color;
    }

    /**
     * Get a clone of this spaceship.
     * @return a Spaceship with exactly the same caracteristics.
     */
    public abstract Spaceship getInstance();


    /**
     * Draw a spaceship and adds it to root.
     * @param root
     * @param selected
     */
    public void draw(Group root, boolean selected) {

        Polygon polygon = initPolygon();
        polygon.setFill(color);

        if(selected)
        {
            DropShadow borderGlow = new DropShadow();
            borderGlow.setColor(color);
            borderGlow.setOffsetX(0f);
            borderGlow.setOffsetY(0f);
            // polygon.setEffect(borderGlow); // SLOW THE ENTIRE GAME
        }
        root.getChildren().add(polygon);
    }

    /**
     * Rotate the spaceship.
     * @param theta Rotation's angle in radians.
     */
    public void rotate(double theta){ // theta in  radians
        this.angle = Math.floorMod((int) Math.toDegrees(theta), 360); // angle in degrees this mod is better than %
    }

    /**
     * Move the spaceship towards its target.
     */
    public void moveForward(){
        this.setPos(this.getPos().add(MathUtils.getRotatedVector(this.direction.multiply(speed), this.angle)));
    }

    /**
     * Ensure that the Spaceship contains the point.
     * @param point A Point in space.
     * @return True if the spaceship contains the point.
     */
    public boolean contains(Point2D point){
        Polygon polygon = initPolygon();
        return  polygon.contains(point);
    }


    @Override
    public String toString(){
        return "X : " + pos.getX() + ", Y : " + pos.getY();
    }

    //----------------GETTERS/SETTERS--------------//

    public Point2D getDirection() {
        return direction;
    }

    public Point2D getPos() {
        return pos;
    }

    public void setPos(Point2D pos) { this.pos = pos;  }

    public LinkedList<Point2D> getSteps() {
        return steps;
    }

    public void setSteps(LinkedList<Point2D> steps) {
        this.steps = steps;
    }

    public Color getColor() {
        return color;
    }
    public int getDamage() {
        return damage;
    }

    public abstract Polygon initPolygon();

    public void setColor(Color color) {
        this.color = color;
    }

    private void writeObject(ObjectOutputStream oos){
        try {
            oos.writeObject(damage);
            oos.writeObject(necessary_production);
            oos.writeObject(angle);
            oos.writeObject(speed);
            oos.writeObject(length);
            oos.writeObject(pos.getX());
            oos.writeObject(pos.getY());
            oos.writeObject(color.getRed());
            oos.writeObject(color.getGreen());
            oos.writeObject(color.getBlue());
            oos.writeObject(color.getOpacity());
            oos.writeObject(direction.getX());
            oos.writeObject(direction.getY());
            if(steps != null){
                oos.writeObject(true);
                oos.writeObject(steps.size());
                for(Point2D p : steps){
                    oos.writeObject(p.getX());
                    oos.writeObject(p.getY());
                }
            }else{
                oos.writeObject(false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream ois){
        try {
            damage = (int)ois.readObject();
            necessary_production = (int)ois.readObject();
            angle = (int)ois.readObject();
            speed = (double)ois.readObject();
            length = (double)ois.readObject();
            double x = (double)ois.readObject();
            double y = (double)ois.readObject();
            double r = (double)ois.readObject();
            double g = (double)ois.readObject();
            double b = (double)ois.readObject();
            double opacity = (double)ois.readObject();
            double xDirection = (double)ois.readObject();
            double yDirection = (double)ois.readObject();
            boolean stepsWritten = (boolean)ois.readObject();
            if(stepsWritten){
                int sizeSteps = (int)ois.readObject();
                steps = new LinkedList<>();
                for(int i = 0; i < sizeSteps; i++){
                    steps.add(new Point2D((double)ois.readObject(), (double)ois.readObject()));
                }
            }

            pos = new Point2D(x,y);
            direction = new Point2D(xDirection, yDirection);
            color = new Color(r,g,b,opacity);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
