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

package pigeon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
    Provides access to the public information about the program.
*/
public final class About
{
    public static final String VERSION = "0.1" + " (build " + getBuildId() + ")";
    public static final String TITLE = "Pigeon v" + VERSION;
    public static final String CREDITS = "Created by Paul Richards <paul.richards@gmail.com>.";
    
    // NonCreatable
    private About()
    {
    }

    /**
        Attempt to read the Subversion ID from the BuildID.txt file inside the Jar file.
    
        Returns "unknown" if this cannot be found.
    */
    public static String getBuildId()
    {
        InputStream in = ClassLoader.getSystemResourceAsStream("BuildID.txt");
        try {
            if (in != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String buildId = reader.readLine();
                if (buildId != null) {
                    return buildId;
                }
            }
        }  catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return "unknown";
    }
    
    /**
        Read the LICENSE.txt file inside the application Jar file.
        
        Returns "Please see LICENSE.txt" if this cannot be found.
    */
    public static String getLicense()
    {
        InputStream in = ClassLoader.getSystemResourceAsStream("LICENSE.txt");
        try {
            if (in != null) {
                Reader reader = new InputStreamReader(in);
                StringBuffer buf = new StringBuffer();
                while (true) {
                    int ch = reader.read();
                    if (ch == -1) {
                        break;
                    }
                    buf.append((char)ch);
                }
                return buf.toString();
            }
        } catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return "Please see LICENSE.txt";
    }    
}
