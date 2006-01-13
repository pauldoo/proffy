/*
 * RaceresultsTableModel.java
 *
 * Created on 06 January 2006, 21:23
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pigeon.view;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import pigeon.model.Race;

/**
 *
 * @author Paul
 */
class RaceresultsTableModel extends AbstractTableModel {
    
    private static final long serialVersionUID = 42L;
        
    private final Vector<Race> races;
    
    /** Creates a new instance of RaceresultsTableModel */
    public RaceresultsTableModel(Vector<Race> races) {
        this.races = races;
    }
    
    public int getRowCount() {
        return races.size();
    }
    
    public int getColumnCount() {
        return 2;
    }
    
    public Object getValueAt(int row, int column) {
        Race race = races.get(row);
        switch (column) {
            case 0:
                return race.getDate();
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
