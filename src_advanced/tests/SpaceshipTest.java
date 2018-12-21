package tests;

import game.Spaceship;
import game.spaceships.LittleSpaceship;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpaceshipTest {

    @Test
    void getInstance() {
        LittleSpaceship spaceship = new LittleSpaceship(Color.BLUE);
        Spaceship instance = spaceship.getInstance();

        assertEquals(LittleSpaceship.class, instance.getClass());
        assertEquals(spaceship.getColor(), instance.getColor());
    }

    @Test
    void rotate() {
        fail("not implemented");
    }

    @Test
    void moveForward() {
        fail("not implemented");
    }

    @Test
    void contains() {
        LittleSpaceship spaceship = new LittleSpaceship(Color.BLUE);
        spaceship.setPos(new Point2D(25, 25));
        spaceship.initPolygon();

        assertEquals(false, spaceship.contains(new Point2D(150, 150)));
        assertEquals(true, spaceship.contains(new Point2D(25, 25)));
    }
}