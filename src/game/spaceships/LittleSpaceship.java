package game.spaceships;

import game.Spaceship;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class LittleSpaceship extends Spaceship {


    public LittleSpaceship() {
        necessary_production = 15;
        length = 50;

    }

    @Override
    public Spaceship getInstance() {
        return new LittleSpaceship();
    }

    @Override
    public void draw(Group root) {

        Polygon polygon = new Polygon();

        polygon.getPoints().addAll(pos.getX(), pos.getY()+(2./3.*length),
                pos.getX()-(1./3.*length), pos.getY()-(1./3.*length),
                pos.getX(), pos.getY(),
                pos.getX()+(1./3.*length), pos.getY()-(1./3.*length));

        polygon.setRotationAxis(new Point3D(pos.getX(), pos.getY(), 0));

        polygon.setRotate(angle);
        root.getChildren().add(polygon);
    }
}