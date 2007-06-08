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

package pigeon.view.laf;

import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
    Provides a Large Font theme.
*/
public final class BigFontTheme extends DefaultMetalTheme
{
    private final float scaling;
    
    public BigFontTheme(float scaling) {
        this.scaling = scaling;
    }
    
    public FontUIResource getControlTextFont()
    {
        FontUIResource old = super.getControlTextFont();
        return new FontUIResource(old.deriveFont(old.getSize2D() * scaling));
    }
    
    public FontUIResource getMenuTextFont()
    {
        FontUIResource old = super.getMenuTextFont();
        return new FontUIResource(old.deriveFont(old.getSize2D() * scaling));
    }
    
    public String getName()
    {
        return super.getName() + " BigFont";
    }
    
    public FontUIResource getSubTextFont()
    {
        FontUIResource old = super.getSubTextFont();
        return new FontUIResource(old.deriveFont(old.getSize2D() * scaling));
    }
    
    public FontUIResource getSystemTextFont()
    {
        FontUIResource old = super.getSystemTextFont();
        return new FontUIResource(old.deriveFont(old.getSize2D() * scaling));
    }
    
    public FontUIResource getUserTextFont()
    {
        FontUIResource old = super.getUserTextFont();
        return new FontUIResource(old.deriveFont(old.getSize2D() * scaling));
    }

    public FontUIResource getWindowTitleFont()
    {
        FontUIResource old = super.getWindowTitleFont();
        return new FontUIResource(old.deriveFont(old.getSize2D() * scaling));
    }
}
