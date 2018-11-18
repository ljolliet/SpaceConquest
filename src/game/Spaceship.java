package game;

import javafx.scene.Group;

public abstract class  Spaceship {

    private int speed;
    private int damage;
    protected double[] pos;
    protected int necessary_production;

    public double[] getPos() {
        return pos;
    }

    public void setPos(double[] pos) {
        this.pos = pos;
    }

    public String toString(){
        return "X : " + pos[0] + ", Y : " + pos[1];
    }

    public abstract Spaceship getInstance();

    public abstract void draw(Group root);

}
