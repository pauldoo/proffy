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

package pigeon.view;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
    Enum class which handles the different date and time formats
    used within the app.
 
    Some Date instances are real absolute values whose values can
    be treated literally.  Other Date instances are relative, and are
    an offset within a single 24hour day.  This latter kind are stored
    as Date instances whose values are within 24hours of the epoch.
*/
public enum DateTimeDisplayMode
{
    DATE,
    DATE_HOURS_MINUTES,
    DATE_HOURS_MINUTES_SECONDS,
    HOURS_MINUTES,
    HOURS_MINUTES_SECONDS;
    
    public int getDisplayColumns()
    {
        return getFormat().toPattern().length();
    }
    
    /**
        Returns true iff this mode is intended to represent a 24 hour
        time with a range of a single day (within 24hours of the epoch).
    */
    public boolean isIntendedFor24HourRelativeFormat()
    {
        switch (this)
        {
            case DATE:
            case DATE_HOURS_MINUTES:
            case DATE_HOURS_MINUTES_SECONDS:
                return false;
            case HOURS_MINUTES:
            case HOURS_MINUTES_SECONDS:
                return true;
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public SimpleDateFormat getFormat()
    {
        SimpleDateFormat result;
        switch (this)
        {
            case DATE:
                result = new SimpleDateFormat("dd/MM/yy");
                break;
            case DATE_HOURS_MINUTES:
                result = new SimpleDateFormat("dd/MM/yy HH:mm");
                break;
            case DATE_HOURS_MINUTES_SECONDS:
                result = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                break;
            case HOURS_MINUTES:
                result = new SimpleDateFormat("HH:mm");
                break;
            case HOURS_MINUTES_SECONDS:
                result = new SimpleDateFormat("HH:mm:ss");
                break;
            default:
                throw new IllegalArgumentException();
        }
        if (isIntendedFor24HourRelativeFormat()) {
            result.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return result;
    }
}
