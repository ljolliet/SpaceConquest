package tests;

import controllers.ComputerController;
import controllers.TypeAI;
import game.Planet;
import game.Spaceship;
import game.Squadron;
import game.spaceships.LittleSpaceship;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


class PlanetTest {

    @org.junit.jupiter.api.Test
    void createSquadron() {
        Planet p = new Planet(new Point2D(0,0),0, 5* Utils.LITTLE_SPACESHIP_NEC_PROD, new LittleSpaceship(Color.BLUE));
        Planet target = new Planet(new Point2D(100,100),0, 0, new LittleSpaceship(Color.BLUE));
        p.addProduction();
        assertEquals(5,p.getAvailable_ships());
        p.addWaitingShips(5,target,new HashMap<>());
        assertEquals(5, p.getWaiting_for_launch());

        p.setOwner(new ComputerController(Color.RED, TypeAI.CLASSIC));

        Squadron s = p.createSquadron(p.getWaiting_for_launch());

        assertEquals(5,s.getSpaceships().size());
        assertNotEquals(null, s.getOwner());

    }

    @org.junit.jupiter.api.Test
    void addWaitingShips() {
        Planet p = new Planet(new Point2D(0,0),0, 5* Utils.LITTLE_SPACESHIP_NEC_PROD, new LittleSpaceship(Color.BLUE));
        Planet target = new Planet(new Point2D(100,100),0, 0, new LittleSpaceship(Color.BLUE));
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
        Planet p = new Planet(new Point2D(0,0),0, 9, new LittleSpaceship(Color.BLUE));
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
        Planet p = new Planet(new Point2D(0,0),0, 9, new LittleSpaceship(Color.BLUE));
        p.addSpaceship();

        assertEquals(1, p.getAvailable_ships());

        for(int i = 0; i < 100; i++){
            p.addSpaceship();
        }
        assertEquals(101, p.getAvailable_ships());
    }

    @org.junit.jupiter.api.Test
    void contains() {
        Planet p1 = new Planet(new Point2D(0,0),50, 9, new LittleSpaceship(Color.BLUE));
        assertEquals(true, p1.contains(new Point2D(0,50)));
        assertEquals(false,p1.contains(new Point2D(0,51)));
    }

    @org.junit.jupiter.api.Test
    void distantOf() {
        Planet p1 = new Planet(new Point2D(0,0),0, 9, new LittleSpaceship(Color.BLUE));
        Planet p2 = new Planet(new Point2D(0,100),0, 9, new LittleSpaceship(Color.BLUE));

        assertEquals(100, p1.distantOf(p2.getCenter()));
        assertEquals(150,p1.distantOf(new Point2D(150,0)));
    }

    @org.junit.jupiter.api.Test
    void changeOwner() {
        Planet p1 = new Planet(new Point2D(0,0),0, 9, new LittleSpaceship(Color.BLUE));

        ComputerController owner1 = new ComputerController(Color.BLUE, TypeAI.CLASSIC);
        ComputerController owner2 = new ComputerController(Color.RED, TypeAI.CLASSIC);

        p1.setOwner(owner1);

        assertEquals(owner1, p1.getOwner());

        p1.changeOwner(owner2, new LittleSpaceship(Color.RED));

        assertEquals(owner2, p1.getOwner());
        assertFalse(owner1.getPlanets().contains(p1));
    }

    @org.junit.jupiter.api.Test
    void isLaunchReady() {
        Planet p1 = new Planet(new Point2D(0,0),0, 9, new LittleSpaceship(Color.BLUE));
        ComputerController owner1 = new ComputerController(Color.BLUE, TypeAI.CLASSIC);
        p1.setOwner(owner1);

        assertEquals(true, p1.isLaunchReady());

        ArrayList<Spaceship> spaceships = new ArrayList<>();
        Spaceship obstacle = new LittleSpaceship(Color.BLUE);
        obstacle.setPos(new Point2D( 0,0));

        spaceships.add(obstacle);

        Squadron squad = new Squadron(spaceships, owner1);
        owner1.getSquadrons().add(squad);

        assertEquals(false, p1.isLaunchReady());
    }

    @org.junit.jupiter.api.Test
    void nearestEnnemyPlanet() {
        Planet p1 = new Planet(new Point2D(0,0),0, 9, new LittleSpaceship(Color.BLUE));
        Planet p2 = new Planet(new Point2D(0,100),0, 9, new LittleSpaceship(Color.BLUE));
        Planet p3 = new Planet(new Point2D(0,200),0, 9, new LittleSpaceship(Color.BLUE));

        ComputerController owner1 = new ComputerController(Color.BLUE, TypeAI.CLASSIC);
        ComputerController owner2 = new ComputerController(Color.RED, TypeAI.CLASSIC);

        p1.setOwner(owner1);
        p2.setOwner(owner2);
        p3.setOwner(owner2);

        ArrayList<Planet> planets = new ArrayList<>();
        planets.add(p1);
        planets.add(p2);
        planets.add(p3);

        assertEquals(p2, p1.nearestEnnemyPlanet(planets));
    }
}