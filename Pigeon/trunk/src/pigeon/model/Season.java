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

package pigeon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Paul
 */
public class Season implements Serializable {
    
    private static final long serialVersionUID = 42L;
    
    private String name;
    private Club club = new Club();
    private Collection<Race> races = new ArrayList<Race>();
    
    /** Creates a new instance of Season */
    public Season() {
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addRace(Race race) throws ValidationException {
        if (races.contains( race ) || !races.add( race )) {
            throw new ValidationException("Race already exists");
        }
    }

    public void removeRace(Race race) {
        races.remove(race);
    }
    
    public Collection<Race> getRaces() {
        return races;
    }
    
}
