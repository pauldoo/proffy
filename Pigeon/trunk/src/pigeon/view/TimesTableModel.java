/*
 * Pigeon: A pigeon club race result management program.
 * Copyright (C) 2005-2006  Paul Richards
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package pigeon.view;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.table.AbstractTableModel;
import pigeon.model.Clock;
import pigeon.model.Race;
import pigeon.model.Time;

/**
 * Represents the times entered for a clock by listing the ring numbers and the time currently entered.
 * @author pauldoo
 */
public class TimesTableModel extends AbstractTableModel
{
    private static final long serialVersionUID = 42L;
    
    private Clock clock;
    private Race race;
    private boolean editable;

    /** Creates a new instance of TimesTableModel */
    public TimesTableModel(Clock clock/*, Race race*/, boolean editable)
    {
        this.clock = clock;
        //this.race = race;
        this.editable = editable;
    }
    
    public int getRowCount() {
        return clock.getTimes().size();
    }
    
    public int getColumnCount() {
        return 2;
    }
    
    private Time getEntry(int row) {
        return clock.getTimes().get(row);
    }
    
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
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
                return Utilities.TIME_FORMAT.format(new Date(entry.getMemberTime()));
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Ring Number";
            case 1:
                return "Clock Time";
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public boolean isCellEditable(int row, int column) {
        switch (column) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public void setValueAt(Object value, int row, int column) {
        Time entry = getEntry(row);
        switch (column) {
            case 1: {
                try {
                    Date date = Utilities.TIME_FORMAT.parse((String)value);
                    entry.setMemberTime(date.getTime());
                    fireTableRowsUpdated(row, row);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            }
            default:
                throw new IllegalArgumentException();
        }
    }
}
