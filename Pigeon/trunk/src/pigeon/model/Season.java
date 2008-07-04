/*
    Copyright (C) 2005, 2006, 2007, 2008  Paul Richards.

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

package pigeon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
    Stores information for a single season.

    Contains an Orgainzation object and a collecion of Race objects.
    Organization objects should not be shared between seasons since the
    set of members and racepoints might change from season to season.
*/
public final class Season implements Serializable {

    private static final long serialVersionUID = 2185370002566545845L;

    private String name;
    private Organization organization = new Organization();
    private List<Race> races = new ArrayList<Race>();

    public Season() {
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
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

    public List<Race> getRaces() {
        return Utilities.unmodifiableSortedList(races);
    }

}
