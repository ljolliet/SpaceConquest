package game;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import utils.MathUtils;

import java.util.LinkedList;

public abstract class  Spaceship {

    protected int damage;
    protected int necessary_production;
    protected int angle = 0;
    protected double speed;
    protected double length;
    protected Point2D pos;
    protected Color color;

    protected LinkedList<Point2D> steps; //list of coordinates to get to the target
    protected Point2D direction = Point2D.ZERO;

    public Spaceship(Color color) {
        this.color = color;
    }


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
             polygon.setEffect(borderGlow); // SLOW THE ENTIRE GAME
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
}
