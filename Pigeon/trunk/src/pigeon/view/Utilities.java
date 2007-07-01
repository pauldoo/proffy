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

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import pigeon.competitions.Competition;
import pigeon.model.Clock;
import pigeon.model.Constants;
import pigeon.model.Member;
import pigeon.model.Organization;
import pigeon.model.Race;
import pigeon.model.Season;
import pigeon.model.Time;

/**
    Public static methods that don't have a natural home in any
    of the view classes.
*/
public final class Utilities {

    // Non-Creatable
    private Utilities()
    {
    }

    /**
        The start range for year drop down combo boxes.
    */
    public static final int YEAR_DISPLAY_START = 2005;

    /**
        The end range for year drop down combo boxes.
    */
    public static final int YEAR_DISPLAY_END = YEAR_DISPLAY_START + 10;

    /**
        DateFormat for formatting times that span just a single day.

        Their 'long' representation spans only from 0 to 24 * 60 * 60 * 1000.
        The resulting string will be in 24hr time (hopefully).
    */
    public static final DateFormat TIME_FORMAT_WITHOUT_LOCALE;
    static {
        TIME_FORMAT_WITHOUT_LOCALE = new SimpleDateFormat("HH:mm:ss");
        TIME_FORMAT_WITHOUT_LOCALE.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
        DateFormat for formatting times that occur on a real calendar.

        Their 'long' representation is not confined to spanning just a single day.
        The local time zone is taken into account.
    */
    public static final DateFormat TIME_FORMAT_WITH_LOCALE;
    static {
        TIME_FORMAT_WITH_LOCALE = new SimpleDateFormat("HH:mm:ss");
    }

    /**
        DateFormat for formatting dates that occur on a real calendar.

        Their 'long' representation is not confined to spanning just a single day.
        The local time zone is taken into account.
    */
    public static final DateFormat DATE_FORMAT;
    static {
        DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
    }

    /**
        Given a long value representing a time, returns the beginning of that day.

        Does not take into account timezones or locale information, so should only
        be used for times that are relative and span only a few days.
    */
    public static long startOfDay(long time) {
        return (time / Constants.MILLISECONDS_PER_DAY) * Constants.MILLISECONDS_PER_DAY;
    }

    /**
        Given an Organization, return a list of all the club names mentioned in member profiles.
    */
    public static Collection<String> findClubNames(Organization organization)
    {
        SortedSet<String> result = new TreeSet<String>();
        for (Member m: organization.getMembers()) {
            if (m.getClub() != null && !m.getClub().equals("")) {
                result.add(m.getClub());
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    /**
        Given an Organization, return a list of all the section names mentioned in member profiles.
    */
    public static Collection<String> findSectionNames(Organization organization)
    {
        SortedSet<String> result = new TreeSet<String>();
        for (Member m: organization.getMembers()) {
            if (m.getSection() != null && !m.getSection().equals("")) {
                result.add(m.getSection());
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    /**
        Checks all the times entered in a season and returns all the competitions
        mentioned in the pigeon time entries.

        Used when loading a season to verify that all of the competitions used when
        saving the season are still present in the configuration file.
    */
    private static Collection<String>  getCompetitionNames(Season season)
    {
        // Use a Set here to ensure that duplicates are removed.
        Set<String> result = new TreeSet<String>();
        for (Race r: season.getRaces()) {
            for (Clock c: r.getClocks()) {
                for (Time t: c.getTimes()) {
                    result.addAll(t.getOpenCompetitionsEntered());
                    result.addAll(t.getSectionCompetitionsEntered());
                }
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    /**
        Checks all the birds in a season and returns all of the colours mentioned.
    */
    public static Collection<String>  getBirdColors(Season season)
    {
        Set<String> result = new TreeSet<String>();
        for (Race r: season.getRaces()) {
            for (Clock c: r.getClocks()) {
                for (Time t: c.getTimes()) {
                    final String color = t.getColor();
                    if (color.length() != 0) {
                        result.add(color);
                    }
                }
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    /**
        Checks through a season looking for a given ring number so that
        the bird colour can be guessed from a previous race.
    */
    public static String guessBirdColor(final Season season, final String ringNumber)
    {
        if (ringNumber.length() != 0) {
            for (Race r: season.getRaces()) {
                for (Clock c: r.getClocks()) {
                    for (Time t: c.getTimes()) {
                        if (ringNumber.equals(t.getRingNumber())) {
                            return t.getColor();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
        Returns a list of all the competition names in a configuration.
    */
    public static List<String>  getCompetitionNames(List<Competition> competitions)
    {
        // Don't expect duplicate names in the configuration, so we can use
        // any kind of collection.
        List<String> result = new ArrayList<String>();
        for (Competition c: competitions) {
            result.add(c.getName());
        }
        return Collections.unmodifiableList(result);
    }

    /**
        Verifies that a season is valid with respect to the current
        application version and configuration.

        Checks (for example) that the competition names mentioned
        in the loaded file are still present in the application configuration
        file.
    */
    public static void validateSeason(Component parent, Season season, Configuration configuration) throws UserCancelledException
    {
        Collection<String> competitionsMentionedInFile = getCompetitionNames(season);
        Collection<String> competitionsConfigured = getCompetitionNames(configuration.getCompetitions());
        if (!competitionsConfigured.containsAll(competitionsMentionedInFile)) {
            String message =
                "This season's pools do not match the current pool list.\n" +
                "Some of the pool entry information may not load correctly.\n" +
                "\n" +
                "Continue anyway?";
            int result = JOptionPane.showConfirmDialog(parent, message, "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result != JOptionPane.YES_OPTION) {
                throw new UserCancelledException();
            }
        }
    }

    public static JFileChooser createFileChooser()
    {
        JFileChooser chooser = new JFileChooser();
        FileFilter filter = SimpleFileFilter.createSeasonFileFilter();
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser;
    }

    public static Season loadSeasonFromFile(File file) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(file);
            ObjectInput objectIn = new ObjectInputStream(new GZIPInputStream(new BufferedInputStream(fileIn)));
            Season loaded = (Season)objectIn.readObject();
            objectIn.close();
            return loaded;
        } finally {
            if (fileIn != null) {
                fileIn.close();
            }
        }
    }

    public static void writeSeasonToFile(Season season, File file) throws FileNotFoundException, IOException
    {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(file);
            ObjectOutput objectOut = new ObjectOutputStream(new GZIPOutputStream(new BufferedOutputStream(fileOut)));
            objectOut.writeObject(season);
            objectOut.close();
        } finally {
            if (fileOut != null) {
                fileOut.close();
            }
        }
    }

    public static boolean isMacOsX()
    {
        String lcOSName = System.getProperty("os.name").toLowerCase();
        boolean MAC_OS_X = lcOSName.startsWith("mac os x");
        return MAC_OS_X;
    }
}
