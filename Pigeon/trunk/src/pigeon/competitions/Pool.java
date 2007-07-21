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

public final class Pool extends Competition
{
    private final int payoutPeriod;
    
    public Pool(String name, double entryCost, double clubTake, boolean availableInOpen, int payoutPeriod)
    {
        super(name, entryCost, clubTake, availableInOpen);
        this.payoutPeriod = payoutPeriod;
    }
    
    public int maximumNumberOfWinners(int entrants)
    {
        double winners = ((1.0 - clubTake) * entrants)  / payoutPeriod;
        return (int)Math.ceil(winners);
    }

    public double prize(int place, int entrants)
    {
        checkPlaceIsInRange(place, entrants);
        final double prize = Math.min(1.0, ((1.0 - clubTake) * entrants / payoutPeriod) - (place - 1)) * entryCost * payoutPeriod;
        return prize;
    }
}
