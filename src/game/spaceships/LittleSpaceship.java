package game.spaceships;

import game.Spaceship;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import utils.Utils;

public class LittleSpaceship extends Spaceship {

    public LittleSpaceship(Color color) {
        super(color);
        necessary_production = Utils.LITTLE_SPACESHIP_NEC_PROD;
        damage = Utils.LITTLE_SPACESHIP_DAMAGE;
        length = Utils.LITTLE_SPACESHIP_LENGTH;
        this.direction = new Point2D(0, 1); // find a better way
    }

    /**
     * @return a new instance of this Spaceship.
     */
    @Override
    public Spaceship getInstance() {
        return new LittleSpaceship(color);
    }

    @Override
    public Polygon initPolygon() {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(pos.getX(), pos.getY()+(2./3.*length),
                pos.getX()-(1./3.*length), pos.getY()-(1./3.*length),
                pos.getX(), pos.getY(),
                pos.getX()+(1./3.*length), pos.getY()-(1./3.*length));
        polygon.getTransforms().add(new Rotate(angle, pos.getX(),pos.getY()));
        return polygon;

    }
}