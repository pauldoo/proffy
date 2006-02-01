/*
 * ValidationException.java
 *
 * Created on 19 January 2006, 19:51
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pigeon.model;
import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Paul
 */
public class ValidationException extends Exception {
    
    private static final long serialVersionUID = 42L;
    
    private final String cause;
    
    /** Creates a new instance of ValidationException */
    public ValidationException(String cause) {
        this.cause = cause;
    }
    
    public String toString() {
        return cause;
    }
    
    public void showWarning(Component parent) {
        JOptionPane.showMessageDialog(parent, cause, "Invalid information", JOptionPane.WARNING_MESSAGE);
    }
}
