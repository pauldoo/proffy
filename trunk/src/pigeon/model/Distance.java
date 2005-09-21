/*
 * Distance.java
 *
 * Created on September 21, 2005, 7:57 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pigeon.model;

import java.io.Serializable;

/**
 *
 * @author pauldoo
 */
public class Distance implements Serializable, Comparable<Distance> {
    
    private static final long serialVersionUID = 42L;
    
    private static double METRES_PER_YARD = 0.9144;
    private static int YARDS_PER_MILE = 1760;
    
    private double distanceInMetres;
    
    /** Creates a new instance of Distance */
    private Distance(double metres) {
        this.distanceInMetres = metres;
    }
    
    public static Distance createFromMetric(double metres) {
        return new Distance(metres);
    }
    
    public static Distance createFromImperial(int miles, int yards) {
        return createFromMetric((miles * YARDS_PER_MILE + yards) * METRES_PER_YARD);
    }

    // Return distance in metres
    public double getMetres() {
        return distanceInMetres;
    }
    
    // Return distance in yards
    public double getYards() {
        return distanceInMetres / METRES_PER_YARD;
    }
    
    public String toString() {
        int yards = (int)(Math.round(getYards())) % YARDS_PER_MILE;
        int miles = (int)(Math.round(getYards())) / YARDS_PER_MILE;
        return miles + "miles " + yards + "yards";
    }    

    public int hashCode() {
        return new Double(distanceInMetres).hashCode();
    }
    
    public int compareTo(Distance other) {
        return Double.compare(this.distanceInMetres, other.distanceInMetres);
    }
    
}
