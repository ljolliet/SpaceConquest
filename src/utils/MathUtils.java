package utils;

import java.util.ArrayList;

public class MathUtils {

    private static int DISTANCE_ADDED = 15;

    public static ArrayList<double[]> dotAroundACircle(double x, double y, int radius, int nb_dots){
        ArrayList<double[]> pos = new ArrayList<>();
        double multiplier = 360/nb_dots;
        int newRadius = radius + DISTANCE_ADDED;

        for(int i = 0; i < nb_dots; i ++){
            double[] newPos = new double[]{x + newRadius * Math.cos(i * multiplier),y + newRadius * Math.sin(i * multiplier)};
            pos.add(newPos);
            //System.out.println("X : " + newPos[0] + ", Y : " + newPos[1]);
        }

        return pos;
    }
}
