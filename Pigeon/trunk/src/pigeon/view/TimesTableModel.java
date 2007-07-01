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

import java.util.Date;
import javax.swing.table.AbstractTableModel;
import pigeon.model.Clock;
import pigeon.model.Constants;
import pigeon.model.Sex;
import pigeon.model.Time;

/**
 * Shows the times entered for a clock by listing the ring numbers and times currently entered.
 */
final class TimesTableModel extends AbstractTableModel
{
    private static final long serialVersionUID = 2820658767004438666L;

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
        return 5;
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
            case 3:
                return String.class;
            case 4:
                return Sex.class;
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
            case 3:
                return entry.getColor();
            case 4:
                return entry.getSex();
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
            case 3:
                return "Bird Color";
            case 4:
                return "Bird Sex";
            default:
                throw new IllegalArgumentException();
        }
    }
}
