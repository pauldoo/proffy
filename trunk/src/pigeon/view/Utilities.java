/*
 * Utilities.java
 *
 * Created on October 23, 2005, 5:55 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pigeon.view;

import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;

/**
 *
 * @author pauldoo
 */
public class Utilities {
    
    public static <T extends Comparable<T>> Vector<T> sortCollection(Collection<T> collection) {
        return new Vector<T>(new TreeSet<T>(collection));
    }
    
}
