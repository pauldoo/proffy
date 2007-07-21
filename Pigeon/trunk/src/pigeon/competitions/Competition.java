/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package pigeon.competitions;

public abstract class Competition
{
    /**
        The textual name of the competition as it
        appears on reports etc.
    */
    private final String name;
    
    /**
        The cost per bird to enter.
    */
    protected final double entryCost;
    
    /**
        The fraction of the total prize fund that is kept by the
        club before prizes are calculated.
    */
    protected final double clubTake;
    
    /**
        Is this competition ran in the Open?
    */
    private final boolean availableInOpen;
    
    public Competition(String name, double entryCost, double clubTake, boolean availableInOpen)
    {
        this.name = name;
        this.entryCost = entryCost;
        this.clubTake = clubTake;
        this.availableInOpen = availableInOpen;
    }
    
    public String getName()
    {
        return name;
    }
    
    protected void checkPlaceIsInRange(int place, int entrants) throws IllegalArgumentException
    {
        if (place < 1 || place > maximumNumberOfWinners(entrants)) {
            throw new IllegalArgumentException("Place expected to be within the range [1, maximumNumberOfWinners(entrants)].");
        }
    }
    
    /**
        Given the number of entrants for this competition,
        calculate the maximum number of winners there could
        be.  The actual number of winners may be smaller if
        not enough birds complete the race.
    */
    public abstract int maximumNumberOfWinners(int entrants);
    
    /**
        Given the position that a bird has come in the competition
        and the total number of entrants, calculate the prize.
     
        @param place Must be in the range [1, maximumNumberOfWinners(entrants)].
    */
    public abstract double prize(int place, int entrants);
    
    public double totalPoolMoney(int entrants)
    {
        return entrants * entryCost;
    }
    
    public double totalClubTake(int entrants)
    {
        return totalPoolMoney(entrants) * clubTake;
    }

    public boolean isAvailableInOpen()
    {
        return availableInOpen;
    }
}
