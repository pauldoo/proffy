/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

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

package pigeon.report;

import java.util.Date;
import pigeon.model.Distance;
import pigeon.model.Time;

/**
    Used to represent a bird's position within a result table.

    Used by the RaceReporter and CompetitionReporter classes.
*/
final class BirdResult implements Comparable<BirdResult>
{
    public final double velocityInMetresPerSecond;
    public final Time time;
    public final Date correctedClockTime;
    public final Distance distance;
    public final StringBuffer html = new StringBuffer();

    public BirdResult(double velocityInMetresPerSecond, Time time, Date correctedClockTime, Distance distance)
    {
        this.velocityInMetresPerSecond = velocityInMetresPerSecond;
        this.time = time;
        this.correctedClockTime = correctedClockTime;
        this.distance = distance;
    }

    public boolean equals(Object rhs)
    {
        return equals((BirdResult)rhs);
    }

    public boolean equals(BirdResult rhs)
    {
        return compareTo(rhs) == 0;
    }

    public int compareTo(BirdResult rhs)
    {
        final BirdResult lhs = this;
        if (lhs == rhs) {
            return 0;
        }

        int result = -Double.compare(lhs.velocityInMetresPerSecond, rhs.velocityInMetresPerSecond);
        if (result == 0) {
            result = lhs.time.compareTo(rhs.time);
        }
        return result;
    }
}
