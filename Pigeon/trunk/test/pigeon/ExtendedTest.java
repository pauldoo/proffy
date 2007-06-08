/*
    Copyright (c) 2005-2007, Paul Richards
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

        * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.

        * Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.

        * Neither the name of Paul Richards nor the names of contributors may be
        used to endorse or promote products derived from this software without
        specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.
*/

package pigeon;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import pigeon.competitions.Competition;
import pigeon.model.Clock;
import pigeon.model.Constants;
import pigeon.model.Distance;
import pigeon.model.Member;
import pigeon.model.Organization;
import pigeon.model.Race;
import pigeon.model.Racepoint;
import pigeon.model.Season;
import pigeon.model.Time;
import pigeon.model.ValidationException;
import pigeon.report.CompetitionReporter;
import pigeon.report.DistanceReporter;
import pigeon.report.MembersReporter;
import pigeon.report.RaceReporter;
import pigeon.view.Configuration;
import pigeon.view.Utilities;

/**
    Some extended regression based tests for the app.
*/
public final class ExtendedTest extends TestCase
{
    private static final boolean UPDATE_OK_FILES = false;
    
    final Random random = new Random(0);
    Configuration configuration;
    Season season;

    static final int RACEPOINT_COUNT = 13;
    static final int CLUB_COUNT = 17;
    static final int BIRDS_PER_MEMBER = 19;
    static final int MEMBER_COUNT = 23;
    
    public ExtendedTest(String testName) {
        super(testName);
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite(ExtendedTest.class);

        return suite;
    }
    
    protected void setUp() throws ValidationException, IOException
    {
        BufferedInputStream configIn = null;
        try {
            configIn = new BufferedInputStream(new FileInputStream("regression/configuration.xml"));
            configuration = new Configuration(configIn);
        } finally {
            if (configIn != null) {
                configIn.close();
            }
        }
        
        season = new Season();
        season.setOrganization(createOraganization());
        addRaces();
    }
    
    private void addRaces() throws ValidationException
    {
        for (Racepoint r: season.getOrganization().getRacepoints()) {
            Race race = new Race();
            race.setRacepoint(r);
            final int year = random.nextInt(10) + Utilities.YEAR_DISPLAY_START;
            final int month = random.nextInt(12) + GregorianCalendar.JANUARY;
            final int day = random.nextInt(20) + 1;
            // Released between 6am and 9:59am
            final int hour = random.nextInt(4) + 6;
            final int minute = random.nextInt(60);
            
            long liberation = new GregorianCalendar(year, month, day, hour, minute).getTimeInMillis();
            race.setLiberationDate(new Date(liberation));
            int daysCovered = random.nextInt(3) + 1; // 1, 2, or 3
            race.setDaysCovered(daysCovered);
            
            int darknessBegins = (int)((random.nextDouble() * 6 + 15) * Constants.MILLISECONDS_PER_HOUR);
            int darknessEnds = (int)((random.nextDouble() * 6 + 3) * Constants.MILLISECONDS_PER_HOUR);
            if (daysCovered >= 2) {
                race.setHoursOfDarkness(darknessBegins, darknessEnds);
            }
            
            for (int i = 0; i < season.getOrganization().getNumberOfMembers(); i++) {
                Member m = season.getOrganization().getMembers().get(i);
                Clock clock = new Clock();
                clock.setMember(m);
                
                long setTime = liberation - random.nextInt((int)Constants.MILLISECONDS_PER_DAY);
                clock.setTimeOnMasterWhenSet(new Date(setTime));
                clock.setTimeOnMemberWhenSet(new Date(setTime));
                
                long masterOpenTime = (long)(liberation + (daysCovered + random.nextDouble()) * Constants.MILLISECONDS_PER_DAY);
                clock.setTimeOnMasterWhenOpened(new Date(masterOpenTime));

                long memberOpenTime = (long)(masterOpenTime + (random.nextDouble() - 0.5) * Constants.MILLISECONDS_PER_MINUTE);
                clock.setTimeOnMemberWhenOpened(new Date(memberOpenTime));
                
                for (int j = 0; j < BIRDS_PER_MEMBER; j++) {
                    Time t = new Time();
                    String ringNumber = "M" + i + "B" + j;
                    t.setRingNumber(ringNumber);

                    long clockInTime = (long)(
                        random.nextInt(daysCovered) * Constants.MILLISECONDS_PER_DAY +
                        (random.nextDouble() * 6 + 9) * Constants.MILLISECONDS_PER_HOUR);
                    assert(setTime + clockInTime <= masterOpenTime);
                    t.setMemberTime(clockInTime, daysCovered);
                    
                    for (Competition c: configuration.getCompetitions()) {
                        if (random.nextBoolean()) {
                            Collection<String> set = new ArrayList<String>(t.getOpenCompetitionsEntered());
                            set.add(c.getName());
                            t.setOpenCompetitionsEntered(set);
                        }
                        if (random.nextBoolean()) {
                            Collection<String> set = new ArrayList<String>(t.getSectionCompetitionsEntered());
                            set.add(c.getName());
                            t.setSectionCompetitionsEntered(set);
                        }
                    }
                    
                    clock.addTime(t);
                }
                race.addClock(clock);
            }
            season.addRace(race);
        }
    }
    
    private Organization createOraganization() throws ValidationException
    {
        final Organization org = new Organization();
        org.setName("Test fed");
        
        final String[][] clubs = new String[CLUB_COUNT][];
        for (int i = 0; i < CLUB_COUNT; i++) {
            clubs[i] = new String[2];
            clubs[i][0] = "Club #" + i;
            clubs[i][1] = random.nextBoolean() ? "East" : "West";
        }
        
        for (int i = 0; i < MEMBER_COUNT; i++) {
            Member m = new Member();
            m.setName("Member #" + i);
            StringBuffer address = new StringBuffer();
            address.append("Line 1" + "\n");
            address.append("Line 2" + "\n");
            m.setAddress(address.toString());
            m.setSHUNumber("SHU" + (i * 7));
            m.setTelephone("" + (i * 11));
            int clubIndex = random.nextInt(clubs.length);
            m.setClub(clubs[clubIndex][0]);
            m.setSection(clubs[clubIndex][1]);
            org.addMember(m);
        }
        
        for (int i = 0; i < RACEPOINT_COUNT; i++) {
            Racepoint r = new Racepoint();
            r.setName("Racepoint #" + i);
            org.addRacepoint(r);
        }
        
        for (Member m: org.getMembers()) {
            for (Racepoint r: org.getRacepoints()) {
                int miles = random.nextInt(200) + 600;
                int yards = random.nextInt(Constants.YARDS_PER_MILE);
                Distance d = Distance.createFromImperial(miles, yards);
                org.setDistance(m, r, d);
            }
        }
        
        return org;
    }
    
    protected void tearDown()
    {
    }

    private void checkRegression(final byte[] tmpData, String name) throws IOException
    {
        final File tmpFile = new File("regression/" + name + (UPDATE_OK_FILES ? ".ok" : ".tmp"));
        final File okFile = new File("regression/" + name + ".ok");
        
        FileOutputStream tmpOut = new FileOutputStream(tmpFile);
        try {
            tmpOut.write(tmpData);
        } finally {
            tmpOut.close();
        }
        
        final byte[] okData = new byte[(int)okFile.length()];
        FileInputStream okIn = new FileInputStream(okFile);
        try {
            okIn.read(okData);
        } finally {
            okIn.close();
        }

        assertTrue("Regression failure: '" + name + "'", Arrays.equals(okData, tmpData));
    }
    
    public void testSerialization() throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(out);
        objectOut.writeObject(season);
        objectOut.close();
        
        final byte[] savedOnce = out.toByteArray();
        checkRegression(savedOnce, "Serialization");
        
        OutputStream pcsOut = null;
        try {
            pcsOut = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream("regression/Serialization.pcs")));
            pcsOut.write(savedOnce);
        } finally {
            if (pcsOut != null) {
                pcsOut.close();
            }
        }
        
        ByteArrayInputStream in = new ByteArrayInputStream(savedOnce);
        ObjectInputStream objectIn = new ObjectInputStream(in);
        
        Season reloaded = (Season)objectIn.readObject();

        out = new ByteArrayOutputStream();
        objectOut = new ObjectOutputStream(out);
        objectOut.writeObject(reloaded);
        objectOut.close();
        
        final byte[] savedAgain = out.toByteArray();
        
        assertTrue("Resaving should not break anything", Arrays.equals(savedOnce, savedAgain));
    }
    
    public void testMemberDistanceReports() throws IOException
    {
        for (Member member: season.getOrganization().getMembers()) {
            DistanceReporter<Racepoint> reporter = new DistanceReporter<Racepoint>(
                season.getOrganization().getName(),
                member.toString(), 
                "Racepoint",
                season.getOrganization().getDistancesForMember(member));
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            reporter.write(out);
            out.close();
            
            checkRegression(out.toByteArray(), "Distance_" + member.toString());
        }
    }

    public void testRacepointDistanceReports() throws IOException
    {
        for (Racepoint racepoint: season.getOrganization().getRacepoints()) {
            DistanceReporter<Member> reporter = new DistanceReporter<Member>(
                season.getOrganization().getName(),
                racepoint.toString(), 
                "Member",
                season.getOrganization().getDistancesForRacepoint(racepoint));
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            reporter.write(out);
            out.close();
            
            checkRegression(out.toByteArray(), "Distance_" + racepoint.toString());
        }
    }
    
    public void testRaceReports() throws IOException
    {
        for (Race race: season.getRaces()) {
            RaceReporter reporter = new RaceReporter(season.getOrganization(), race, true);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            reporter.write(out);
            out.close();
            
            checkRegression(out.toByteArray(), "Race_" + race.getRacepoint());
        }
    }
    
    public void testPoolReports() throws IOException
    {
        for (Race race: season.getRaces()) {
            // Need to randomly decide how many birds entered each pool.
            Map<String, Map<String, Integer>> entrantsCount = new TreeMap<String, Map<String, Integer>>();
            String[] sections = new String[]{"Open", "East", "West"};
            for (String section: sections) {
                entrantsCount.put(section, new TreeMap<String, Integer>());
                for (Competition pool: configuration.getCompetitions()) {
                    entrantsCount.get(section).put(pool.getName(), (int)((random.nextDouble()) * MEMBER_COUNT * BIRDS_PER_MEMBER));
                }
            }
            
            CompetitionReporter reporter = new CompetitionReporter(season.getOrganization(), race, true, configuration.getCompetitions(), entrantsCount);            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            reporter.write(out);
            out.close();
            
            checkRegression(out.toByteArray(), "Pools_" + race.getRacepoint());
        }
    }
    
    public void testMembersReport() throws IOException
    {
        MembersReporter reporter = new MembersReporter(
            season.getOrganization().getName(),
            season.getOrganization().getMembers(),
            configuration.getMode()
        );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        reporter.write(out);
        out.close();

        checkRegression(out.toByteArray(), "Members");
    }
}
