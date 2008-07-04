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

/**
    Represents a racepoint.
*/
public final class Racepoint implements Serializable, Comparable<Racepoint> {

    private static final long serialVersionUID = 5881572526657587494L;

    private String name;

    public Racepoint() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws ValidationException {
        name = name.trim();
        if (name.length() == 0) {
            throw new ValidationException("Racepoint name is empty");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return equals((Racepoint)other);
    }

    public boolean equals(Racepoint other) {
        if (this == other) {
            return true;
        } else {
            return name.equals(other.name);
        }
    }

    @Override
    public int compareTo(Racepoint other) {
        if (this == other) {
            return 0;
        } else {
            return name.compareTo(other.name);
        }
    }

}
