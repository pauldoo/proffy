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

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import pigeon.model.Clock;
import pigeon.model.Member;

/**
 * Represents a list of clocks for placing into a JTable.
 *
 * Displays the associated member and the number of rings entered.
 */
public class ClocksTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 42L;

    private final Vector<Clock> clocks;

    public ClocksTableModel(Vector<Clock> clocks)
    {
        this.clocks = clocks;
    }

    public int getRowCount() {
        return clocks.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return Member.class;
            case 1:
                return Integer.class;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Object getValueAt(int row, int column) {
        Clock clock = clocks.get(row);
        switch (column) {
            case 0:
                return clock.getMember();
            case 1:
                return new Integer(clock.getTimes().size());
            default:
                throw new IllegalArgumentException();
        }
    }

    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Member";
            case 1:
                return "Rings entered";
            default:
                throw new IllegalArgumentException();
        }
    }
}
