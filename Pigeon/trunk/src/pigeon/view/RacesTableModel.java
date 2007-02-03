/*
 * Pigeon: A pigeon club race result management program.
 * Copyright (C) 2005-2007  Paul Richards
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

import java.util.Date;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import pigeon.model.Race;
import pigeon.model.Racepoint;

/**
 * Represents a list of races for use in a JTable, displays liberation date and racepoint name.
 */
class RacesTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 42L;

    private final Vector<Race> races;

    public RacesTableModel(Vector<Race> races) {
        this.races = races;
    }

    public int getRowCount() {
        return races.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return Date.class;
            case 1:
                return Racepoint.class;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Object getValueAt(int row, int column) {
        Race race = races.get(row);
        switch (column) {
            case 0:
                return race.getLiberationDate();
            case 1:
                return race.getRacepoint();
            default:
                throw new IllegalArgumentException();
        }
    }

    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Date";
            case 1:
                return "Racepoint";
            default:
                throw new IllegalArgumentException();
        }
    }
}
