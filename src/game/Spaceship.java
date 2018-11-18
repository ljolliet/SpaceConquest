package game;

import javafx.geometry.Point2D;
import javafx.scene.Group;

public abstract class  Spaceship {

    private int speed;
    private int damage;
    protected Point2D pos;
    protected double length;
    protected int necessary_production;
    protected double angle = 0;

    public Point2D getDirection() {
        return direction;
    }

    protected Point2D direction = Point2D.ZERO;

    public Point2D getPos() {
        return pos;
    }

    public void setPos(Point2D pos) { this.pos = pos;  }

    public String toString(){
        return "X : " + pos.getX() + ", Y : " + pos.getY();
    }

    public abstract Spaceship getInstance();

    public abstract void draw(Group root);

    public  void rotate(double teta){ // teta in  radians
        angle = (angle+(teta* 180/Math.PI)) % 360; // angle in degrees ( _*180/pi : radians --> degrees)
    }
}
