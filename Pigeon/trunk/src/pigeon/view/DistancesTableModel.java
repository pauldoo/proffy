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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.table.AbstractTableModel;
import pigeon.model.Distance;
import pigeon.model.Member;

/**
 * Generic final class to edit distances associated with a list of objects (members or racepoints).
 *
 * Used by the DistanceEditor.
 */
final class DistancesTableModel<Target> extends AbstractTableModel {

    private static final long serialVersionUID = 3953261892856570881L;

    private final String targetTitle;
    private final Map<Target, Distance> distances;
    private final boolean editable;

    /** Creates a new instance of DistancesTableModel */
    public DistancesTableModel(String targetTitle, Map<Target, Distance> distances, boolean editable) {
        this.targetTitle = targetTitle;
        this.distances = distances;
        this.editable = editable;
    }

    public int getRowCount() {
        return distances.size();
    }

    public int getColumnCount() {
        return 3;
    }

    private Map.Entry<Target, Distance> getEntry(int row) {
        Set<Map.Entry<Target, Distance>> entries = distances.entrySet();
        Iterator<Map.Entry<Target, Distance>> iter = entries.iterator();
        for (int i = 0; i < row; i++) {
           iter.next();
        }
        return iter.next();
    }

    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Object getValueAt(int row, int column) {
        Map.Entry<Target, Distance> entry = getEntry(row);

        switch (column) {
            case 0:
                Target t = entry.getKey();
                if (t instanceof Member) {
                    Member m = (Member)t;
                    StringBuffer result = new StringBuffer(m.getName());
                    if (m.getClub() != null) {
                        result.append(", " + m.getClub());
                    }
                    if (m.getSection() != null) {
                        result.append(", " + m.getSection());
                    }
                    return result.toString();
                } else {
                    return entry.getKey();
                }
            case 1:
                return entry.getValue().getMiles();
            case 2:
                return entry.getValue().getYardsRemainder();
            default:
                throw new IllegalArgumentException();
        }
    }

    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return targetTitle;
            case 1:
                return "Miles";
            case 2:
                return "Yards";
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean isCellEditable(int row, int column) {
        switch (column) {
            case 0:
                return false;
            case 1:
            case 2:
                return true;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setValueAt(Object value, int row, int column) {
        Map.Entry<Target, Distance> entry = getEntry(row);
        switch (column) {
            case 1: {
                int miles = Integer.parseInt(value.toString());
                int yards = entry.getValue().getYardsRemainder();
                Distance newValue = Distance.createFromImperial(miles,  yards);
                entry.setValue( newValue );
                fireTableRowsUpdated(row, row);
                break;
            }
            case 2: {
                int miles = entry.getValue().getMiles();
                int yards = Integer.parseInt(value.toString());
                Distance newValue = Distance.createFromImperial(miles,  yards);
                entry.setValue( newValue );
                fireTableRowsUpdated(row, row);
                break;
            }
            default:
                throw new IllegalArgumentException();
        }
    }

}
