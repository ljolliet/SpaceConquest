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

    public Point2D getPos() {
        return pos;
    }

    public void setPos(Point2D pos) { this.pos = pos;  }

    public String toString(){
        return "X : " + pos.getX() + ", Y : " + pos.getY();
    }

    public abstract Spaceship getInstance();

    public abstract void draw(Group root);

}
