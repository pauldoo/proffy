/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package pigeon.view;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import pigeon.model.Clock;
import pigeon.model.Member;

/**
 * Represents a list of clocks for placing into a JTable.
 *
 * Displays the associated member and the number of rings entered.
 */
final class ClocksTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 6245142229764744394L;

    private final List<Clock> clocks;

    public ClocksTableModel(List<Clock> clocks)
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
