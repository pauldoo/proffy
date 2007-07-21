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

import javax.naming.OperationNotSupportedException;

public final class Nomination extends Competition
{
    private final double[] payouts;
    
    public Nomination(String name, double cost, double clubTake, boolean availableInOpen, double[] payouts)
    {
        super(name, cost, clubTake, availableInOpen);
        this.payouts = payouts;
        
        double totalPayout = 0;
        for (double v: payouts) {
            totalPayout += v;
        }
        if (Math.abs(totalPayout - 1.0) > 1e-6) {
            /**
                We get small errors since binary floating point numbers
                can't precisely represent some finite decimals.
                We must only report an error if we go outside
                some small tollerance.
            */
            throw new IllegalArgumentException("Payouts should total 1.0");
        }
    }
    
    public int maximumNumberOfWinners(int entrants)
    {
        return Math.min(entrants, payouts.length);
    }
    
    public double prize(int place, int entrants)
    {
        checkPlaceIsInRange(place, entrants);
        return payouts[place - 1] * entrants * entryCost * (1.0 - clubTake);
    }
}
