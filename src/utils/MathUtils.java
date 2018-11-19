package utils;

import game.Spaceship;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public class MathUtils {

    private static int DISTANCE_ADDED = 15;

    public static ArrayList<double[]> dotAroundACircle(Point2D center, int radius, int nb_dots){
        ArrayList<double[]> pos = new ArrayList<>();
        double multiplier = 360/nb_dots;
        int newRadius = radius + DISTANCE_ADDED;

        for(int i = 0; i < nb_dots; i ++){
            double[] newPos = new double[]{center.getX() + newRadius * Math.cos(i * multiplier),center.getY() + newRadius * Math.sin(i * multiplier)};
            pos.add(newPos);
        }

        return pos;
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


    public static Point2D getRotatedVector(Point2D direction, double theta) {


        double px = direction.getX() * Math.cos(theta) - direction.getY() * Math.sin(theta);
        double py = direction.getX() * Math.sin(theta) + direction.getY() * Math.cos(theta);

        return new Point2D(px,py);

    }
}
