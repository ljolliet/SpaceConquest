package gameloop;

import game.Spaceship;
import game.Squadron;
import controllers.ComputerController;
import controllers.Controller;
import controllers.HumanController;
import controllers.TypeAI;
import game.Planet;
import game.spaceships.BigSpaceship;
import game.spaceships.LittleSpaceship;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * @author tguesdon, ljolliet
 */
public class GameLoop extends AnimationTimer implements Serializable {

    /**
     * Boolean representing whether or not the player is doing a drag & drop movement.
     */
    public transient boolean dragging = false;

    /**
     * List of all controllers in the game.
     */
    private ArrayList<Controller> controllers = new ArrayList<>();
    /**
     * List of all planets in the game.
     */
    private ArrayList<Planet> planets = new ArrayList<>();
    /**
     * List of all pirate squadron in the game.
     */
    private ArrayList<Squadron> pirateSquadrons = new ArrayList<>();

    /**
     * Accessibility map. If accessibilityMap.get(Point p) is true, it means that p is accessible.
     */
    private transient HashMap<Point2D, Boolean> accessibilityMap = new HashMap<>();

    /**
     * Rectangle used to select squadron.
     */
    private transient Rectangle selectionRect = new Rectangle();

    /**
     * First point of the selection rectangle, will get a value when the user click on the screen.
     */
    private transient Point2D startSelection = null;

    //---------------------VICTORY SCREEN-----------------------------//
    /**
     * Boolean representing whether or not a player won the game.
     */
    private boolean gameWon = false;

    /**
     * The winner of the game.
     */
    private transient Controller winner;

    /**
     * Text displayed when a player win.
     */
    private transient Text winText = new Text("");

    private transient Button menuButton = new Button("Back to main menu");
    //----------------------------------------------------------------//

    /**
     * Constructor. Call initialization function.
     */
    public GameLoop() {
        init();
    }

    /**
     * @param now
     * @see javafx.animation.AnimationTimer#handle(long)
     */
    @Override
    public void handle(long now) {
        if (!gameWon) {
            draw();
            actualizeProduction();
            actualizeShipPos();
            actualizeShipSending();
            actualizeActionsAI();
            pirateSpawn();
            pirateActualize();
            gameWon = checkWin();
        } else {
            drawWin();
        }

    }

    //--------------------INIT-------------//


    /**
     * Call all initialization function.
     */
    private void init() {
        initPlayers();
        initPlanets();
        initEvents();
        initAccessibilityMap();
        GUIController.generateMenuBar();
    }


    /**
     * Create a human controller for the player 0 and a computer controller for every other player
     */
    private void initPlayers() {
        controllers.add(new HumanController(Utils.PLANET_COLOR.get(0)));
        //start at 1 because 0 is the human
        for (int i = 1; i < Utils.NB_PLAYER; i++) {
            int rand = (int) (Math.random() * 3);
            TypeAI type;
            switch (rand) {
                case 0:
                    type = TypeAI.CLASSIC;
                    break;
                case 1:
                    type = TypeAI.SAFE;
                    break;
                case 2:
                    type = TypeAI.AGGRESSIVE;
                    break;
                default:
                    type = TypeAI.CLASSIC;
                    break;
            }
            controllers.add(new ComputerController(Utils.PLANET_COLOR.get(i), type));
        }
    }


    /**
     * Init all planets (player & neutral)
     */
    private void initPlanets() {
        initPlayerPlanets();
        initNeutralPlanets(Utils.NB_NEUTRAL_PLANET);
    }

    /**
     * Create planets for players using getPlayerPlanetPos() to get a free position
     */
    private void initPlayerPlanets() {
        for (Controller c : controllers) {
            Point2D pos = getPlayerPlanetPos();

            double maxProdRate = Utils.PLAYER_PRODUCTION_RATE + Utils.PRODUCTION_VARIATION * Utils.PLAYER_PRODUCTION_RATE / 100;
            double minProdRate = Utils.PLAYER_PRODUCTION_RATE - Utils.PRODUCTION_VARIATION * Utils.PLAYER_PRODUCTION_RATE / 100;
            double randProd = minProdRate + (maxProdRate - minProdRate) * (new Random()).nextDouble();

            double maxRadius = Utils.PLAYER_PLANET_RADIUS + Utils.RADIUS_VARIATION * Utils.PLAYER_PLANET_RADIUS / 100;
            double minRadius = Utils.PLAYER_PLANET_RADIUS - Utils.RADIUS_VARIATION * Utils.PLAYER_PLANET_RADIUS / 100;
            double randRadius = minRadius + (maxRadius - minRadius) * (new Random()).nextDouble();

            int randShip = (new Random()).nextInt(2);
            Planet p = null;
            if (randShip == 0) {
                p = new Planet(pos, (int) randRadius, (float) randProd, new LittleSpaceship(c.getColor()));// add color
            } else if (randShip == 1) {
                p = new Planet(pos, (int) randRadius, (float) randProd, new BigSpaceship(c.getColor()));// add color
            }

            c.getPlanets().add(p);
            p.setOwner(c);
            planets.add(p);
        }
    }


    /**
     * @return a free position for player's planet
     */
    private Point2D getPlayerPlanetPos() {
        int maxX = Utils.WINDOW_WIDTH - Utils.PLAYER_PLANET_RADIUS;
        int maxY = Utils.WINDOW_HEIGHT - Utils.PLAYER_PLANET_RADIUS;
        int min = Utils.PLAYER_PLANET_RADIUS;

        int x = new Random().nextInt(maxX + 1 - min) + min;
        int y = new Random().nextInt(maxY + 1 - min) + min;

        Point2D pos = new Point2D(x, y);

        boolean isFree = true;

        if (pos.distance(new Point2D(pos.getX(), 0)) < Utils.DISTANCE_BORDER || pos.distance(new Point2D(0, pos.getY())) < Utils.DISTANCE_BORDER
                || pos.distance(new Point2D(pos.getX(), Utils.WINDOW_HEIGHT)) < Utils.DISTANCE_BORDER || pos.distance(new Point2D(Utils.WINDOW_WIDTH, pos.getY())) < Utils.DISTANCE_BORDER)
            isFree = false;

        if (isFree) {
            for (Planet p : planets) {
                if (p.distantOf(pos) < Utils.DISTANCE_BETWEEN_PLAYERS) {
                    isFree = false;
                }
            }
        }

        if (isFree) {
            return pos;
        } else {
            return getPlayerPlanetPos();
        }
    }


    /**
     * Add the neutral planets.
     *
     * @param amount of neutral planets
     */
    private void initNeutralPlanets(int amount) {
        for (int i = 0; i < amount; i++) {
            addRandomNeutralPlanet();
        }
    }

    /**
     * Generate a random position that isn't part of an already existing planet and create a neutral planet with this position.
     */
    private void addRandomNeutralPlanet() {
        int maxX = Utils.WINDOW_WIDTH - Utils.NEUTRAL_PLANET_RADIUS;
        int maxY = Utils.WINDOW_HEIGHT - Utils.NEUTRAL_PLANET_RADIUS;
        int min = Utils.NEUTRAL_PLANET_RADIUS;

        int randX = new Random().nextInt(maxX + 1 - min) + min;
        int randY = new Random().nextInt(maxY + 1 - min) + min;

        double maxRadius = Utils.NEUTRAL_PLANET_RADIUS + Utils.RADIUS_VARIATION * Utils.NEUTRAL_PLANET_RADIUS / 100;
        double minRadius = Utils.NEUTRAL_PLANET_RADIUS - Utils.RADIUS_VARIATION * Utils.NEUTRAL_PLANET_RADIUS / 100;
        double randRadius = minRadius + (maxRadius - minRadius) * (new Random()).nextDouble();

        boolean isPosFree = true;
        Planet tmp = new Planet(new Point2D(randX, randY), (int) randRadius, 0, new LittleSpaceship(Utils.NEUTRAL_PLANET_COLOR));

        int maxHp = Utils.NEUTRAL_HP_RANGE + Utils.HP_VARIATION * Utils.NEUTRAL_HP_RANGE / 100;
        int minHp = Utils.NEUTRAL_HP_RANGE - Utils.HP_VARIATION * Utils.NEUTRAL_HP_RANGE / 100;
        int hp = new Random().nextInt(maxHp + 1 - minHp) + minHp;

        tmp.setAvailable_ships(hp);

        if (tmp.getCenter().distance(new Point2D(tmp.getCenter().getX(), 0)) < Utils.DISTANCE_BORDER || tmp.getCenter().distance(new Point2D(0, tmp.getCenter().getY())) < Utils.DISTANCE_BORDER
                || tmp.getCenter().distance(new Point2D(tmp.getCenter().getX(), Utils.WINDOW_HEIGHT)) < Utils.DISTANCE_BORDER || tmp.getCenter().distance(new Point2D(Utils.WINDOW_WIDTH, tmp.getCenter().getY())) < Utils.DISTANCE_BORDER)
            isPosFree = false;

        for (Planet p : planets) {
            if (tmp.distantOf(p.getCenter()) < Utils.DISTANCE_BETWEEN_NEUTRAL) {
                isPosFree = false;
            }
        }

        if (isPosFree) {
            planets.add(tmp);
        } else {
            addRandomNeutralPlanet();
        }
    }


    /**
     * Fill the accessibility map by creating a grid of points and checking if they are part of a planet
     */
    private void initAccessibilityMap() {
        accessibilityMap = new HashMap<>();
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < Utils.WINDOW_WIDTH; i += Utils.COLUMN_SIZE) {
            for (int j = 0; j < Utils.WINDOW_HEIGHT; j += Utils.COLUMN_SIZE) {
                Point2D pos = new Point2D(i, j);
                boolean accessible = true;
                for (Planet p : planets) {
                    if (p.contains(pos))
                        accessible = false;
                }
                accessibilityMap.put(pos, accessible);
            }
        }
        System.out.println("Accessibility Map generated in : " + (System.currentTimeMillis() - t0) + "ms");
    }

    //--------------------------EVENTS-------------------//

    /**
     * Declaration of all events triggered by the human
     */
    private void initEvents() {
        HumanController hc = (HumanController) controllers.get(0);

        Main.SCENE.setOnMouseClicked(event -> {
            if (!gameWon) {
                for (Squadron s : hc.getSquadrons()) {
                    if (s.contains(new Point2D(event.getX(), event.getY()))) {
                        hc.setSelectedSquadron(s);
                    }

                }
            } else {
                if (event.getX() > menuButton.getLayoutX() && event.getX() < menuButton.getLayoutX() + menuButton.getWidth()
                        && event.getY() > menuButton.getLayoutY() && event.getY() < menuButton.getLayoutY() + menuButton.getHeight()) {
                    Main.GROUP.getChildren().removeAll(Main.GROUP.getChildren());
                    GUIController.drawBackground(Main.GROUP, true);
                    this.stop();
                }
            }


        });

        Main.SCENE.setOnDragDetected(event -> {
            if (!gameWon) {
                if (hc.isOnHumanPlanet(event.getX(), event.getY())) {
                    dragging = true;
                    Planet p = hc.getHumanPlanetClick(event.getX(), event.getY());
                    p.setSelected(true);
                    hc.setSelectedPlanet(p);
                } else {
                    startSelection = new Point2D(event.getX(), event.getY());
                }
            }
        });

        Main.SCENE.setOnMouseReleased(event -> {
            if (!gameWon) {
                if (hc.isOnPlanet(event.getX(), event.getY(), planets) || hc.isOnHumanPlanet(event.getX(), event.getY())) {
                    if (dragging) { //if mouse released during a drag action
                        hc.getSelectedPlanet().addWaitingShips((int) (hc.getSelectedPlanet().getAvailable_ships() * (hc.getSelectedPlanet().getSending_quantity().getValue() / 100)), hc.getPlanetClic(event.getX(), event.getY(), planets), accessibilityMap);
                        dragging = false;
                    } else { //if mouse was released not during a drag action
                        if (hc.getSelectedSquadron() != null) {
                            hc.setTarget(hc.getPlanetClic(event.getX(), event.getY(), planets), accessibilityMap);
                        }
                    }
                }

                if (hc.getSelectedPlanet() != null)
                    hc.getSelectedPlanet().setSelected(false);

                if (startSelection != null) {
                    int actual = 0;
                    for (Squadron s : controllers.get(0).getSquadrons()) {
                        if (s.shipsInRectangle(selectionRect) > actual) {
                            ((HumanController) controllers.get(0)).setSelectedSquadron(s);
                            s.setSelected(true);
                        }
                    }

                    startSelection = null;
                }
            }
        });


    }

    //-----------------PROCESS---------------------//

    /**
     * Check if the game is won by a controller.
     *
     * @return true if the game is won, false otherwise.
     */
    private boolean checkWin() {
        int nbPlayerAlive = 0;
        for (Controller c : controllers) {
            if (c.getPlanets().size() > 0) {
                nbPlayerAlive++;
                winner = c;
            }
        }
        if (nbPlayerAlive == 1) {
            System.out.println("WIN");
            return true;
        } else {
            winner = null;
            return false;
        }


    }

    /**
     * Actualize the production of all planets
     */
    private void actualizeProduction() {
        for (Controller c : controllers) {
            for (Planet p : c.getPlanets()) {
                p.addProduction();
            }
        }
    }


    /**
     * Actualize the position of all controller's ships
     */
    private void actualizeShipPos() {
        if (Utils.OPTIMIZED) {
            for (Controller c : controllers) {
                ArrayList<Squadron> toRemove = new ArrayList<>();
                for (Squadron s : c.getSquadrons()) {
                    s.sendToTarget();
                    if (s.getSpaceships().isEmpty()) {
                        toRemove.add(s);
                    }
                }
                for (Squadron s : toRemove) {
                    c.getSquadrons().remove(s);
                }
            }
        } else {
            for (Controller c : controllers) // refactor in a method
                for (Squadron s : c.getSquadrons())
                    s.sendToTarget();
        }


    }

    /**
     * Launch the currently waiting ships if there is no other ships around the planet.
     */
    private void actualizeShipSending() {
        for (Controller c : controllers) {
            for (Planet p : c.getPlanets()) {
                if (p.isLaunchReady() && p.getWaiting_for_launch() > 0) {
                    if (c.getClass() == HumanController.class)
                        ((HumanController) c).launchShip(p);
                    else {
                        p.sendShip(Utils.WAVE_SIZE);
                    }

                }
            }
        }
    }

    /**
     * Call the decision making function of each AI.
     */
    private void actualizeActionsAI() {
        for (Controller c : controllers) {
            if (c.getClass() == ComputerController.class) {
                ((ComputerController) c).process(planets, accessibilityMap);
            }
        }
    }


    /**
     * Randomly add pirate squadron.
     */
    private void pirateSpawn() {
        int rand = (int) (Math.random() * Utils.MAX_RAND_PIRATE);

        if (rand == 0) {
            int randTarget = (int) (Math.random() * planets.size());
            int randSP = (int) (Math.random() * 2);
            ArrayList<Spaceship> pirates = new ArrayList<>();
            switch (randSP) {
                case 0:
                    pirates.add(new LittleSpaceship(Utils.NEUTRAL_PLANET_COLOR));
                    break;
                case 1:
                    pirates.add(new BigSpaceship(Utils.NEUTRAL_PLANET_COLOR));
                    break;
                default:
                    pirates.add(new LittleSpaceship(Utils.NEUTRAL_PLANET_COLOR));
                    break;
            }

            Point2D pos;
            switch ((int) (Math.random() * 4)) {
                case 0:
                    pos = new Point2D(0, (Math.random() * Utils.WINDOW_HEIGHT));
                    break;
                case 1:
                    pos = new Point2D(Utils.WINDOW_WIDTH - 1, (Math.random() * Utils.WINDOW_HEIGHT));
                    break;
                case 2:
                    pos = new Point2D((Math.random() * Utils.WINDOW_WIDTH), 0);
                    break;
                case 3:
                    pos = new Point2D((Math.random() * Utils.WINDOW_WIDTH), Utils.WINDOW_HEIGHT - 1);
                    break;
                default:
                    pos = new Point2D(0,0);
                    break;
            }

            pirates.get(0).setPos(pos);
            Squadron squad = new Squadron(pirates, null);
            if(squad.getTarget() == null)
                squad.setTarget(planets.get(randTarget), accessibilityMap);

            pirateSquadrons.add(squad);
        }
    }

    /**
     * Actualize pirate squadron position.
     */
    private void pirateActualize() {
        for (Squadron s : pirateSquadrons) {
                s.sendToTarget();
        }
    }

    //--------------------------------------DRAWING-----------------------//

    /**
     * Call every draw function of the game's elements
     */
    private void draw() {
        Main.GROUP.getChildren().removeAll(Main.GROUP.getChildren()); // clear root

        if (!Utils.OPTIMIZED)
            GUIController.drawBackground(Main.GROUP, false);

        for (Planet p : planets) // draw all planets
            p.draw(Main.GROUP);

        for (Controller c : controllers)
            for (Squadron s : c.getSquadrons()) {
                s.draw(Main.GROUP);
            }

        for (Squadron s : pirateSquadrons) {
            s.draw(Main.GROUP);
        }

        GUIController.displayMenuBar(Main.GROUP);

        if (startSelection != null) {
            selectionRect.setX(startSelection.getX());
            selectionRect.setY(startSelection.getY());
            selectionRect.setWidth(Math.sqrt(Math.pow(MouseInfo.getPointerInfo().getLocation().getX() - startSelection.getX(), 2)));
            selectionRect.setHeight(Math.sqrt(Math.pow(MouseInfo.getPointerInfo().getLocation().getY() - startSelection.getY(), 2)));
            if (MouseInfo.getPointerInfo().getLocation().getX() - startSelection.getX() < 0) {
                selectionRect.setX(MouseInfo.getPointerInfo().getLocation().getX());
            }
            if (MouseInfo.getPointerInfo().getLocation().getY() - startSelection.getY() < 0) {
                selectionRect.setY(MouseInfo.getPointerInfo().getLocation().getY());
            }
            selectionRect.setStrokeWidth(2.0);
            selectionRect.setStroke(Color.WHITE);
            selectionRect.setFill(Color.TRANSPARENT);
            Main.GROUP.getChildren().add(selectionRect);
        }


    }

    /**
     * Draw the win screen, with winner color.
     */
    private void drawWin() {
        Main.GROUP.getChildren().removeAll(Main.GROUP.getChildren());

        winText.setText("The winner is the " + Utils.COLOR_STRING.get(Utils.PLANET_COLOR.indexOf(winner.getColor())) + " player ! ");
        winText.setFill(winner.getColor());
        winText.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        winText.setTextAlignment(TextAlignment.CENTER);
        winText.setX(Utils.WINDOW_WIDTH / 2 - winText.getLayoutBounds().getWidth() / 2);
        winText.setY(Utils.WINDOW_HEIGHT / 3 - winText.getLayoutBounds().getHeight() / 2);
        winText.setStroke(winner.getColor().darker());
        winText.setStrokeWidth(2.);

        menuButton.setLayoutX(Utils.WINDOW_WIDTH / 2 - menuButton.getWidth() / 2);
        menuButton.setLayoutY(2 * Utils.WINDOW_HEIGHT / 3 - menuButton.getHeight() / 2);

        GUIController.drawBackground(Main.GROUP, false);

        Main.GROUP.getChildren().add(winText);
        Main.GROUP.getChildren().add(menuButton);
    }

    /**
     * Save the gameloop's data in "save.ser".
     *
     * @param oos The stream used to write data.
     */
    private void writeObject(ObjectOutputStream oos) {
        try {
            oos.writeObject(planets);
            oos.writeObject(controllers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the gameloop from "save.ser".
     *
     * @param ois The stream used to read gameloop.
     */
    private void readObject(ObjectInputStream ois) {
        try {
            planets = (ArrayList<Planet>) ois.readObject();
            controllers = (ArrayList<Controller>) ois.readObject();
            accessibilityMap = new HashMap<>();
            initAccessibilityMap();
            for (Controller c : controllers) {
                for (Squadron s : c.getSquadrons()) {
                    s.setTarget(s.getTarget(), accessibilityMap);
                }
            }
            selectionRect = new Rectangle();
            dragging = false;
            winText = new Text("");
            menuButton = new Button("Back to main menu");
            initEvents();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
