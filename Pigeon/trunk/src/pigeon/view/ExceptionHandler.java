/*
    Copyright (c) 2005-2007, Paul Richards
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

        * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.

        * Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.

        * Neither the name of Paul Richards nor the names of contributors may be
        used to endorse or promote products derived from this software without
        specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.
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
