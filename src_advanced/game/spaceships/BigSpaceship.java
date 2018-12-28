package game.spaceships;

import game.Spaceship;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import utils.Utils;

public class BigSpaceship extends Spaceship {

    public BigSpaceship(Color color) {
        super(color);
        necessary_production = Utils.BIG_SPACESHIP_NEC_PROD;
        damage = Utils.BIG_SPACESHIP_DAMAGE;
        length = Utils.BIG_SPACESHIP_LENGTH;
        speed = Utils.BIG_SPACESHIP_SPEED;
        this.direction = Utils.SPACESHIP_DIRECTION;
    }

    @Override
    public Spaceship getInstance() {
        return new BigSpaceship(color);
    }

    @Override
    public Polygon initPolygon(){
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(pos.getX(), pos.getY() + (1. / 2. * length),
                pos.getX() - (1. / 2. * length), pos.getY() - (1. / 4. * length),
                pos.getX() - (1. / 5. * length), pos.getY(),
                pos.getX() - (1. / 3. * length), pos.getY() - (1. / 2. * length),
                pos.getX() + (1. / 3. * length), pos.getY() - (1. / 2. * length),
                pos.getX() + (1. / 5. * length), pos.getY(),
                pos.getX() + (1. / 2. * length), pos.getY() - (1. / 4. * length));
        polygon.getTransforms().add(new Rotate(angle, pos.getX(), pos.getY()));
        return polygon;
    }
}
