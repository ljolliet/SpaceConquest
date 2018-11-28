package utils;

import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

import java.util.*;

public class MathUtils {

    /**
     * Process position around a circle.
     *
     * @param center  Center of the target circle.
     * @param radius  Radius of the target circle.
     * @param nb_dots Size of the returned list.
     * @return a list of position around a circle.
     */
    public static ArrayList<double[]> dotAroundACircle(Point2D center, int radius, int nb_dots) {
        ArrayList<double[]> pos = new ArrayList<>();
        double multiplier = nb_dots % 360;
        int newRadius = radius + Utils.DISTANCE_PLANET_SHIPS;

        for (int i = 0; i < nb_dots; i++) {
            double[] newPos = new double[]{center.getX() + newRadius * Math.cos(i * multiplier), center.getY() + newRadius * Math.sin(i * multiplier)};
            pos.add(newPos);
        }

        return pos;
    }

    /**
     * @param start Starting position.
     * @param destination Position that need to be reached.
     * @param map Map of dots with their accessibility as a boolean.
     * @return a list of position that is a path without obstacles from start to destination.
     */
    public static LinkedList<Point2D> pathfinder(Point2D start, Point2D destination, HashMap<Point2D, Boolean> map) {
        LinkedList<Point2D> steps = new LinkedList<>();

        steps.add(firstPoint(start, destination, map));

        Point2D nearDestination = destinationOnGrid(start, destination);

        while (!steps.contains(nearDestination)) {
            addNext(steps, nearDestination, map);
        }
        steps.add(destination);

        return steps;
    }

    /**
     * Process and add to the steps list the next position of the path.
     * @param steps Actual path.
     * @param destination Position that need to be reached.
     * @param map Map of dots with their accessibility as a boolean.
     */
    private static void addNext(LinkedList<Point2D> steps, Point2D destination, HashMap<Point2D, Boolean> map) {
        TreeSet<Point2D> neighbours = new TreeSet<>((o1, o2) -> {
            double disto1Dest = Math.sqrt(Math.pow(o1.getX() - destination.getX(), 2) + Math.pow(o1.getY() - destination.getY(), 2));
            double disto2Dest = Math.sqrt(Math.pow(o2.getX() - destination.getX(), 2) + Math.pow(o2.getY() - destination.getY(), 2));

            //d1 is closer to the destination
            return Double.compare(disto1Dest, disto2Dest); //d1 is closer to the destination
        });

        for (Point2D p : getNeighbours(steps.getLast(), map)) {
            if (map.get(p))
                if (!steps.contains(p))    //otherwise two points can be added indefinitely
                    neighbours.add(p);
        }

        steps.add(neighbours.first());

    }

    /**
     * @param p Target point.
     * @param map Map of dots with their accessibility as a boolean.
     * @return A list of position that are neighbours of the point p in the map and that are accessible.
     */
    private static ArrayList<Point2D> getNeighbours(Point2D p, HashMap<Point2D, Boolean> map) {
        ArrayList<Point2D> tmp = new ArrayList<>(Arrays.asList(
                new Point2D(p.getX() - Utils.COLUMN_SIZE, p.getY() - Utils.COLUMN_SIZE),    //topleft
                new Point2D(p.getX() - Utils.COLUMN_SIZE, p.getY()),                        //left
                new Point2D(p.getX(), p.getY() - Utils.COLUMN_SIZE),                        //top
                new Point2D(p.getX() - Utils.COLUMN_SIZE, p.getY() + Utils.COLUMN_SIZE),    //bottomleft
                new Point2D(p.getX() + Utils.COLUMN_SIZE, p.getY() - Utils.COLUMN_SIZE),    //topright
                new Point2D(p.getX() + Utils.COLUMN_SIZE, p.getY() + Utils.COLUMN_SIZE),    //bottomright
                new Point2D(p.getX(), p.getY() + Utils.COLUMN_SIZE),                        //bottom
                new Point2D(p.getX() + Utils.COLUMN_SIZE, p.getY())                            //right
        ));

        ArrayList<Point2D> neighbours = new ArrayList<>();
        for (Point2D point : tmp) {
            if (map.containsKey(point))
                neighbours.add(point);
        }

        return neighbours;
    }

    /**
     * @param start Starting position.
     * @param destination Position that need to be reached.
     * @return The neighbour of destination on the grid that is less distant of start.
     */
    private static Point2D destinationOnGrid(Point2D start, Point2D destination) {
        int x = (int) (Math.floor(destination.getX() / Utils.COLUMN_SIZE) * Utils.COLUMN_SIZE);
        int y = (int) (Math.floor(destination.getY() / Utils.COLUMN_SIZE) * Utils.COLUMN_SIZE);
        //don't need to test if the points are in the map because the destination is the center of a planet
        Point2D topLeft = new Point2D(x, y);
        Point2D topRight = new Point2D(x + Utils.COLUMN_SIZE, y);
        Point2D bottomLeft = new Point2D(x, y + Utils.COLUMN_SIZE);
        Point2D bottomRight = new Point2D(x + Utils.COLUMN_SIZE, y + Utils.COLUMN_SIZE);

        TreeSet<Point2D> points = new TreeSet<>((o1, o2) -> {
            double disto1Dest = Math.sqrt(Math.pow(o1.getX() - start.getX(), 2) + Math.pow(o1.getY() - start.getY(), 2));
            double disto2Dest = Math.sqrt(Math.pow(o2.getX() - start.getX(), 2) + Math.pow(o2.getY() - start.getY(), 2));

            //d1 is closer to the starting point
            return Double.compare(disto1Dest, disto2Dest); //d1 is closer to the starting point
        });

        points.add(topLeft);
        points.add(topRight);
        points.add(bottomLeft);
        points.add(bottomRight);

        return points.first();
    }

    /**
     *
     * @param start Starting position.
     * @param destination Position that need to be reached.
     * @param map Map of dots with their accessibility as a boolean.
     * @return The neighbour of start on the grid that is less distant of destination.
     */
    private static Point2D firstPoint(Point2D start, Point2D destination, HashMap<Point2D, Boolean> map) {
        int x = (int) (Math.floor(start.getX() / Utils.COLUMN_SIZE) * Utils.COLUMN_SIZE);
        int y = (int) (Math.floor(start.getY() / Utils.COLUMN_SIZE) * Utils.COLUMN_SIZE);
        Point2D topLeft = null;
        Point2D topRight = null;
        Point2D bottomLeft = null;
        Point2D bottomRight = null;

        //need to verify if the point is on the map in case that the ship is on the border of the screen
        if (map.containsKey(new Point2D(x, y)))
            topLeft = new Point2D(x, y);
        else if (map.containsKey(new Point2D(x + Utils.COLUMN_SIZE, y)))
            topRight = new Point2D(x + Utils.COLUMN_SIZE, y);
        else if (map.containsKey(new Point2D(x, y + Utils.COLUMN_SIZE)))
            bottomLeft = new Point2D(x, y + Utils.COLUMN_SIZE);
        else if (map.containsKey(new Point2D(x + Utils.COLUMN_SIZE, y + Utils.COLUMN_SIZE)))
            bottomRight = new Point2D(x + Utils.COLUMN_SIZE, y + Utils.COLUMN_SIZE);

        TreeSet<Point2D> points = new TreeSet<>((o1, o2) -> {
            double disto1Dest = Math.sqrt(Math.pow(o1.getX() - destination.getX(), 2) + Math.pow(o1.getY() - destination.getY(), 2));
            double disto2Dest = Math.sqrt(Math.pow(o2.getX() - destination.getX(), 2) + Math.pow(o2.getY() - destination.getY(), 2));

            if (disto1Dest < disto2Dest)
                return -1; //d1 is closer to the starting point
            else if (disto2Dest < disto1Dest)
                return 1;
            else return 0;
        });

        if (topLeft != null) {
            points.add(topLeft);
        }
        if (topRight != null) {
            points.add(topRight);
        }
        if (bottomLeft != null) {
            points.add(bottomLeft);
        }
        if (bottomRight != null) {
            points.add(bottomRight);
        }

        return points.first();
    }

    /**
     *
     * @param center
     * @param spaceshipPos
     * @param spaceshipDirection
     * @param on
     * @return
     */
    public static double pointOnOffPlanet(Point2D center, Point2D spaceshipPos, Point2D spaceshipDirection, boolean on) {
        Point2D vectorPlanet;
        if (on)
            vectorPlanet = center.subtract(spaceshipPos).normalize();
        else
            vectorPlanet = spaceshipPos.subtract(center).normalize();
        double uv = vectorPlanet.dotProduct(spaceshipDirection);
        double u = Math.sqrt(Math.pow(vectorPlanet.getX(), 2) + Math.pow(vectorPlanet.getY(), 2));
        double v = Math.sqrt(Math.pow(spaceshipDirection.getX(), 2) + Math.pow(spaceshipDirection.getY(), 2));
        double theta = Math.acos(uv / (u * v));
        if (vectorPlanet.getX() > 0)
            return -theta;

        return theta;

    }

    /**
     *
     * @param direction A direction vector
     * @param angle Angle of the rotation in degrees
     * @return The vector rotated with the angle
     */
    public static Point2D getRotatedVector(Point2D direction, double angle) {
        Rotate r = new Rotate();
        r.setAngle(angle); // degrees
        return r.deltaTransform(direction.getX(), direction.getY());
    }
}
