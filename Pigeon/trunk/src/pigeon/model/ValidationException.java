/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package pigeon.model;
import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * Exception thrown when model data is inconsistent.
 */
public final class ValidationException extends Exception {

    private static final long serialVersionUID = 1525226081739583319L;

    private final String message;
    private final Throwable cause;

    public ValidationException(String message) {
        this.message = message;
        this.cause = null;
    }

    public ValidationException(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    public String toString() {
        return message;
    }

    public void displayErrorDialog(Component parent) {
        JOptionPane.showMessageDialog(parent, message, "Invalid information", JOptionPane.WARNING_MESSAGE);
    }
}
