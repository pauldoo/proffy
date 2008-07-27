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

package pigeon.view;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import pigeon.About;

/**
 * Handles uncaught exceptions and presents a "friendly" crash dialog.
 */
final class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    static public void register() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());
    }

    public ExceptionHandler() {
    }

    public void handle(Throwable e)
    {
        uncaughtException(null, e);
    }

    public void uncaughtException(Thread t, Throwable e) {
        StringWriter message = new StringWriter();
        PrintWriter writer = new PrintWriter(message);
        writer.println(About.TITLE);
        writer.println();
        e.printStackTrace(writer);
        writer.flush();

        JTextComponent message_text = new JTextArea(message.toString(), 10, 50);
        message_text.setEditable(false);
        JScrollPane message_pane = new JScrollPane(message_text);

        Object[] contents = new Object[]{
            new JLabel("An unexpected error has occured.  Please copy & paste the following and send to Paul <paul.richards@gmail.com>."),
            message_pane,
            new JLabel("The application may be unstable until you restart.")
        };

        JOptionPane.showMessageDialog(null, contents, "Oops!", JOptionPane.ERROR_MESSAGE);
    }
}
