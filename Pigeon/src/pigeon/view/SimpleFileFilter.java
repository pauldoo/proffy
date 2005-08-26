/*
 * SimpleFileFilter.java
 *
 * Created on 26 August 2005, 21:45
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pigeon.view;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Paul
 */
public class SimpleFileFilter extends FileFilter {
    
    private String extensions[];
    private String description;
    
    /** Creates a new instance of SimpleFileFilter */
    public SimpleFileFilter(String[] extensions, String description) {
        this.extensions = extensions;
        this.description = description;
    }
    
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        for (String extension: extensions) {
            if (file.getName().endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static SimpleFileFilter createSeasonFileFilter() {
        return new SimpleFileFilter(new String[]{"pcs"}, "Pigeon Club Season");
    }
    
}
