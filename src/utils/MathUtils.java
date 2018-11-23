package utils;

import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;
import sun.reflect.generics.tree.Tree;

import java.util.*;

public class MathUtils {

    

    public static ArrayList<double[]> dotAroundACircle(Point2D center, int radius, int nb_dots){
        ArrayList<double[]> pos = new ArrayList<>();
        double multiplier = nb_dots%360;
        int newRadius = radius + Utils.DISTANCE_PLANET_SHIPS;

        for(int i = 0; i < nb_dots; i ++){
            double[] newPos = new double[]{center.getX() + newRadius * Math.cos(i * multiplier),center.getY() + newRadius * Math.sin(i * multiplier)};
            pos.add(newPos);
        }

        return pos;
    }

    //inspired by the A* (A star) algorithm -> linkedlist works like a FIFO queue
    public static LinkedList<Point2D> pathfinder(Point2D start, Point2D destination, HashMap<Point2D, Boolean> map){
        LinkedList<Point2D> steps = new LinkedList<>();

        steps.add(firstPoint(start, destination,map));

        Point2D nearDestination = destinationOnGrid(start,destination,map);

        while(!steps.contains(nearDestination)){
            addNext(steps, steps.get(steps.size() - 1), nearDestination, map);
        }
        steps.add(destination);

        return steps;
    }

    public static void  addNext(LinkedList<Point2D> steps,Point2D current, Point2D destination, HashMap<Point2D,Boolean> map){
        TreeSet<Point2D> neighbours = new TreeSet<>((o1, o2) -> {
            double disto1Dest = Math.sqrt(Math.pow(o1.getX() - destination.getX(), 2) + Math.pow(o1.getY() - destination.getY(), 2));
            double disto2Dest = Math.sqrt(Math.pow(o2.getX() - destination.getX(), 2) + Math.pow(o2.getY() - destination.getY(), 2));

            if(disto1Dest < disto2Dest)
                return -1; //d1 is closer to the destination
            else if(disto2Dest < disto1Dest)
                return 1;
            else return 0;
        });

        for(Point2D p : getNeighbours(current,map)){
            if(map.get(p))
            	if(!steps.contains(p))	//otherwise two points can be added indefinitely
            		neighbours.add(p);
        }

        steps.add(neighbours.first());

    }

    public static ArrayList<Point2D> getNeighbours(Point2D p, HashMap<Point2D, Boolean> map){
        ArrayList<Point2D> tmp = new ArrayList<Point2D>(Arrays.asList(
        		new Point2D(p.getX() - Utils.COLUMN_SIZE, p.getY() - Utils.COLUMN_SIZE),	//topleft
        		new Point2D(p.getX() - Utils.COLUMN_SIZE, p.getY()),						//left
        		new Point2D(p.getX(), p.getY() - Utils.COLUMN_SIZE),						//top
        		new Point2D(p.getX() - Utils.COLUMN_SIZE, p.getY() + Utils.COLUMN_SIZE),	//bottomleft
        		new Point2D(p.getX() + Utils.COLUMN_SIZE, p.getY() - Utils.COLUMN_SIZE),	//topright
        		new Point2D(p.getX() + Utils.COLUMN_SIZE, p.getY() + Utils.COLUMN_SIZE),	//bottomright
        		new Point2D(p.getX(), p.getY() + Utils.COLUMN_SIZE),						//bottom
        		new Point2D(p.getX() + Utils.COLUMN_SIZE, p.getY())							//right
        		));
        
        ArrayList<Point2D> neighbours = new ArrayList<>();
        for(Point2D point : tmp) {
        	if(map.containsKey(point))
        		neighbours.add(point);
        }
        
        return neighbours;
    }

    public static Point2D destinationOnGrid(Point2D start, Point2D destination, HashMap<Point2D, Boolean> map){
        int x = (int)(Math.floor(destination.getX()/Utils.COLUMN_SIZE)*Utils.COLUMN_SIZE);
        int y = (int)(Math.floor(destination.getY()/Utils.COLUMN_SIZE)*Utils.COLUMN_SIZE);
        //don't need to test if the points are in the map because the destination is the center of a planet
        Point2D topLeft = new Point2D(x,y);
        Point2D topRight = new Point2D(x + Utils.COLUMN_SIZE, y);
        Point2D bottomLeft = new Point2D(x, y + Utils.COLUMN_SIZE);
        Point2D bottomRight = new Point2D(x + Utils.COLUMN_SIZE, y + Utils.COLUMN_SIZE);

        TreeSet<Point2D> points = new TreeSet<>(new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                double disto1Dest = Math.sqrt(Math.pow(o1.getX() - start.getX(), 2) + Math.pow(o1.getY() - start.getY(), 2));
                double disto2Dest = Math.sqrt(Math.pow(o2.getX() - start.getX(), 2) + Math.pow(o2.getY() - start.getY(), 2));

                if(disto1Dest < disto2Dest)
                    return -1; //d1 is closer to the starting point
                else if(disto2Dest < disto1Dest)
                    return 1;
                else return 0;
            }
        });

        points.add(topLeft);
        points.add(topRight);
        points.add(bottomLeft);
        points.add(bottomRight);

        return points.first();
    }

    //need to be improved, must take in account the distance towards destination
    public static Point2D firstPoint(Point2D start,Point2D destination, HashMap<Point2D, Boolean> map){
        int x = (int)(Math.floor(start.getX()/Utils.COLUMN_SIZE)*Utils.COLUMN_SIZE);
        int y = (int)(Math.floor(start.getY()/Utils.COLUMN_SIZE)*Utils.COLUMN_SIZE);
        Point2D topLeft = null;
        Point2D topRight = null;
        Point2D bottomLeft = null;
        Point2D bottomRight = null;

        //need to verify if the point is on the map in case that the ship is on the border of the screen
        if(map.containsKey(new Point2D(x,y)))
            topLeft = new Point2D(x,y);
        else if(map.containsKey(new Point2D(x+Utils.COLUMN_SIZE,y)))
             topRight = new Point2D(x + Utils.COLUMN_SIZE,y);
        else if(map.containsKey(new Point2D(x, y + Utils.COLUMN_SIZE)))
            bottomLeft = new Point2D(x,y + Utils.COLUMN_SIZE);
        else if(map.containsKey(new Point2D(x + Utils.COLUMN_SIZE, y + Utils.COLUMN_SIZE)))
            bottomRight = new Point2D(x + Utils.COLUMN_SIZE, y + Utils.COLUMN_SIZE);

        TreeSet<Point2D> points = new TreeSet<>(new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                double disto1Dest = Math.sqrt(Math.pow(o1.getX() - destination.getX(), 2) + Math.pow(o1.getY() - destination.getY(), 2));
                double disto2Dest = Math.sqrt(Math.pow(o2.getX() - destination.getX(), 2) + Math.pow(o2.getY() - destination.getY(), 2));

                if(disto1Dest < disto2Dest)
                    return -1; //d1 is closer to the starting point
                else if(disto2Dest < disto1Dest)
                    return 1;
                else return 0;            }
        });

        if(topLeft != null){points.add(topLeft);}
        if(topRight != null){points.add(topRight);}
        if(bottomLeft != null){points.add(bottomLeft);}
        if(bottomRight != null){points.add(bottomRight);}

        return points.first();
    }

    public static double pointOnOffPlanet(Point2D center, Point2D spaceshipPos, Point2D spaceshipDirection, boolean on) {
        Point2D vectorPlanet;
        if(on)
            vectorPlanet = center.subtract(spaceshipPos).normalize();
        else
            vectorPlanet = spaceshipPos.subtract(center).normalize();
        Point2D vectorSpaceShip = spaceshipDirection;
        double uv = vectorPlanet.dotProduct(vectorSpaceShip);
        double u = Math.sqrt(Math.pow(vectorPlanet.getX(),2)+ Math.pow(vectorPlanet.getY(),2));
        double v = Math.sqrt(Math.pow(vectorSpaceShip.getX(),2)+ Math.pow(vectorSpaceShip.getY(),2));
        double teta = Math.acos(uv/(u*v));
        if(vectorPlanet.getX() > 0)
            return -teta;

        return teta;

    }


    public static Point2D getRotatedVector(Point2D direction, double angle) {
        Rotate r = new Rotate();
        r.setAngle(angle); // degrees
        return r.deltaTransform(direction.getX(),direction.getY());
    }
}
