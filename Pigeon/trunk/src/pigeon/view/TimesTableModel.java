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

import java.text.ParseException;
import java.util.Date;
import javax.swing.table.AbstractTableModel;
import pigeon.model.Clock;
import pigeon.model.Constants;
import pigeon.model.Time;
import pigeon.model.ValidationException;

/**
 * Shows the times entered for a clock by listing the ring numbers and times currently entered.
 */
public final class TimesTableModel extends AbstractTableModel
{
    private static final long serialVersionUID = 42L;

    private final Clock clock;
    private final int daysInRace;
    private final boolean editable;

    public TimesTableModel(Clock clock, int daysInRace, boolean editable)
    {
        this.clock = clock;
        this.daysInRace = daysInRace;
        this.editable = editable;
    }

    public int getRowCount() {
        return clock.getTimes().size();
    }

    public int getColumnCount() {
        return 3;
    }

    private Time getEntry(int row) {
        return clock.getTimes().get(row);
    }

    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return Integer.class;
            case 2:
                return String.class;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Object getValueAt(int row, int column) {
        Time entry = getEntry(row);

        switch (column) {
            case 0:
                return entry.getRingNumber();
            case 1:
                return (entry.getMemberTime() / Constants.MILLISECONDS_PER_DAY) + 1;
            case 2:
                return Utilities.TIME_FORMAT_WITHOUT_LOCALE.format(new Date(entry.getMemberTime() % Constants.MILLISECONDS_PER_DAY));
            default:
                throw new IllegalArgumentException();
        }
    }

    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Ring Number";
            case 1:
                return "Day";
            case 2:
                return "Clock Time";
            default:
                throw new IllegalArgumentException();
        }
    }
}
