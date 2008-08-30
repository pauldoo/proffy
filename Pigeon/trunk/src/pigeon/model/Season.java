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
import java.util.List;

/**
    Stores information for a single season.

    Contains an Orgainzation object and a collecion of Race objects.
    Organization objects should not be shared between seasons since the
    set of members and racepoints might change from season to season.
*/
public final class Season implements Serializable {

    private static final long serialVersionUID = 2185370002566545845L;

    private final String name;
    private final Organization organization;
    private final List<Race> races;

    private Season(String name, Organization organization, List<Race> races) {
        this.name = name;
        this.organization = organization;
        this.races = Utilities.unmodifiableSortedListCopy(races);
    }
    
    public static Season createEmpty() {
        return new Season("", Organization.createEmpty(), Utilities.createEmptyList(Race.class));
    }

    public Organization getOrganization() {
        return organization;
    }

    public Season repSetOrganization(Organization organization) {
        return new Season(
                this.name,
                organization,
                this.races);
    }

    public String getName() {
        return name;
    }

    public Season repSetName(String name) {
        return new Season(
                name,
                this.organization,
                this.races);
    }

    public Season repAddRace(Race race) throws ValidationException {
        return new Season(
                this.name,
                this.organization,
                Utilities.replicateListAdd(this.races, race));        
    }

    public Season repRemoveRace(Race race) throws ValidationException {
        return new Season(
                this.name,
                this.organization,
                Utilities.replicateListRemove(this.races, race));
    }
    
    public Season repReplaceRace(Race oldRace, Race newRace) throws ValidationException {
        return repRemoveRace(oldRace).repAddRace(newRace);
    }

    public List<Race> getRaces() {
        return this.races;
    }
}
