/*
    Copyright (C) 2009  Paul Richards.

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

package sknat.model;

/**
    Immutable class storing the core properties (or characteristics) of a player
    controllable unit.  This does not store the live state of the unit (position, etc).

    All fields are from 0.0 to 1.0 inclusive and sum to 1.0.
*/
public final class UnitProperties
{
    /**
        Ability of the unit to accelerate and change direction.
    */
    public final double agility;

    /**
        Ability of the unit to do damage to other units.
    */
    public final double attack;

    /**
        Ability of the unit to resist damage inflicted upon it.
    */
    public final double defense;

    /**
        Top speed of the unit.
    */
    public final double speed;

    /**
        Returns true if the given set of property values
        are legal.
    */
    public static boolean isValid(
            double agility,
            double attack,
            double defense,
            double speed)
    {
        if (agility >= 0.0 && agility <= 1.0 &&
            attack >= 0.0 && attack <= 1.0 &&
            defense >= 0.0 && defense <= 1.0 &&
            speed >= 0.0 && speed <= 1.0 &&
            (agility + attack + defense + speed) <= 1.0) {
            return true;
        } else {
            return false;
        }
    }


    public UnitProperties(
            double agility,
            double attack,
            double defense,
            double speed)
    {
        if (isValid(agility, attack, defense, speed)) {
            this.agility = agility;
            this.attack = attack;
            this.defense = defense;
            this.speed = speed;
        } else {
            throw new IllegalArgumentException("Properties are out of range.");
        }
    }
}
