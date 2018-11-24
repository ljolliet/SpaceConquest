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
        necessary_production = 5;
        length = 25;
        this.direction = new Point2D(0, 1); // find a better way
        damage = Utils.LITTLE_SPACESHIP_DAMAGE;
    }

    /**
     * @return a new instance of this Spaceship.
     */
    @Override
    public Spaceship getInstance() {
        return new LittleSpaceship(color);
    }

    /**
     * Draw a spaceship and adds it to root.
     * @param root
     * @param selected
     */
    @Override
    public void draw(Group root, boolean selected) {

        Polygon polygon = initPolygon();
        polygon.setFill(color);

        if(selected)
        {
            DropShadow borderGlow = new DropShadow();
            borderGlow.setColor(color);
            borderGlow.setOffsetX(0f);
            borderGlow.setOffsetY(0f);
            polygon.setEffect(borderGlow);
        }
        root.getChildren().add(polygon);
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