package tests;

import game.Planet;
import game.spaceships.LittleSpaceship;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import utils.Utils;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;


class PlanetTest {

    @org.junit.jupiter.api.Test
    void createSquadron() {
        fail("Not implemented");
    }

    @org.junit.jupiter.api.Test
    void sendShip() {
        fail("Not implemented");
    }

    @org.junit.jupiter.api.Test
    void addWaitingShips() {
        Planet p = new Planet(new Point2D(0,0),0,false, 5* Utils.LITTLE_SPACESHIP_NEC_PROD, new LittleSpaceship(Color.BLUE));
        Planet target = new Planet(new Point2D(100,100),0,false, 0, new LittleSpaceship(Color.BLUE));
        p.addProduction();

        assertEquals(5,p.getAvailable_ships());

        p.addWaitingShips(3,target,new HashMap<>());

        assertEquals(3, p.getWaiting_for_launch());
        assertEquals(2, p.getAvailable_ships());
        assertNotEquals(null,p.getMap());
        assertEquals(target, p.getTarget());
    }

    @org.junit.jupiter.api.Test
    void addProduction() {
        Planet p = new Planet(new Point2D(0,0),0,false, 9, new LittleSpaceship(Color.BLUE));
        p.addProduction(); //9
        if(p.getAvailable_ships() != 0 ){
            fail("Didn't add 1 ship");
        }
        p.addProduction(); //18
        if(p.getAvailable_ships() != 1 ){
            fail("Didn't add 1 ship");
        }
    }

    @org.junit.jupiter.api.Test
    void addSpaceship() {
        Planet p = new Planet(new Point2D(0,0),0,false, 9, new LittleSpaceship(Color.BLUE));
        p.addSpaceship();

        assertEquals(1, p.getAvailable_ships());

        for(int i = 0; i < 100; i++){
            p.addSpaceship();
        }
        assertEquals(101, p.getAvailable_ships());
    }

    @org.junit.jupiter.api.Test
    void collide() {
        fail("Not implemented");
    }

    @org.junit.jupiter.api.Test
    void contains() {
        fail("Not implemented");
    }

    @org.junit.jupiter.api.Test
    void containsHitbox() {
        fail("Not implemented");
    }

    @org.junit.jupiter.api.Test
    void distantOf() {
        Planet p1 = new Planet(new Point2D(0,0),0,false, 9, new LittleSpaceship(Color.BLUE));
        Planet p2 = new Planet(new Point2D(0,100),0,false, 9, new LittleSpaceship(Color.BLUE));

        assertEquals(100, p1.distantOf(p2.getCenter()));
        assertEquals(150,p1.distantOf(new Point2D(150,0)));
    }

    @org.junit.jupiter.api.Test
    void changeOwner() {
        fail("Not implemented");
    }

    @org.junit.jupiter.api.Test
    void checkCollision() {
        fail("Not implemented");
    }

    @org.junit.jupiter.api.Test
    void isLaunchReady() {
        fail("Not implemented");
    }

    @org.junit.jupiter.api.Test
    void nearestEnnemyPlanet() {
        fail("Not implemented");
    }
}