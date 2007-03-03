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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import javax.swing.table.AbstractTableModel;
import pigeon.model.Distance;

/**
 * Generic class to edit distances associated with a list of objects (members or racepoints).
 *
 * Used by the DistanceEditor.
 */
class DistancesTableModel<Target> extends AbstractTableModel {

    private static final long serialVersionUID = 42L;

    private final String targetTitle;
    private final SortedMap<Target, Distance> distances;
    private final boolean editable;

    /** Creates a new instance of DistancesTableModel */
    public DistancesTableModel(String targetTitle, SortedMap<Target, Distance> distances, boolean editable) {
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
                return entry.getKey();
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
