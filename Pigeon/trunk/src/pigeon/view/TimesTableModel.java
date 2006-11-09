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
import javax.swing.table.AbstractTableModel;
import pigeon.model.Clock;
import pigeon.model.Constants;
import pigeon.model.Time;
import pigeon.model.ValidationException;

/**
 * Represents the times entered for a clock by listing the ring numbers and the time currently entered.
 * @author pauldoo
 */
public class TimesTableModel extends AbstractTableModel
{
    private static final long serialVersionUID = 42L;
    
    private final Clock clock;
    private final int daysInRace;
    private final boolean editable;

    /** Creates a new instance of TimesTableModel */
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
                return Utilities.TIME_FORMAT.format(new Date(entry.getMemberTime() % Constants.MILLISECONDS_PER_DAY));
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
        Time entry = getEntry(row);
        try {
            switch (column) {
                case 1: {
                    int day = ((Integer)value) - 1;
                    long time = (entry.getMemberTime() % Constants.MILLISECONDS_PER_DAY) + (day * Constants.MILLISECONDS_PER_DAY);
                    entry.setMemberTime(time, daysInRace);
                    fireTableRowsUpdated(row, row);
                }
                break;
                case 2: {
                    try {
                        Date date = Utilities.TIME_FORMAT.parse((String)value);
                        long time = Utilities.startOfDay(entry.getMemberTime()) + date.getTime();
                        entry.setMemberTime(time, daysInRace);
                        fireTableRowsUpdated(row, row);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
                default:
                    throw new IllegalArgumentException();
            }
        } catch (ValidationException e) {
            e.displayErrorDialog(null);
        }
    }
}
