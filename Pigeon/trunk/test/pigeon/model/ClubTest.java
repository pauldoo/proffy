/*
 * Pigeon: A pigeon club race result management program.
 * Copyright (C) 2005-2006  Paul Richards
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package pigeon.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.*;


/**
 *
 * @author Paul
 */
public class ClubTest extends TestCase {
    
    public ClubTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ClubTest.class);
        
        return suite;
    }

    public void testSerialization() throws IOException, ClassNotFoundException, ValidationException {
        Club club = new Club();
        {
            List<Member> members = new ArrayList<Member>();
            List<Racepoint> racepoints = new ArrayList<Racepoint>();

            for (int i = 0; i <= 9; i++) {
                Member m = new Member();
                m.setName("member_" + i);
                members.add( m );
                club.addMember( m );

                Racepoint r = new Racepoint();
                r.setName("racepoint_" + i);
                racepoints.add( r );
                club.addRacepoint( r );
            }
        }
        
        verifyReferences(club);
        
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutput objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject( club );
        club = null;
        objOut.close();

        // Club written out
        
        InputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInput objIn = new ObjectInputStream(byteIn);
        club = (Club)objIn.readObject();
        
        verifyReferences(club);
    }
    
    /**
     * Verifies that the object instances used as keys within the distances
     * structure are the same instances that are present in the member/racepoint
     * lists.
     */
    private void verifyReferences(Club club) {
        for (Member member: club.getMembers()) {
            for (Racepoint racepoint: club.getRacepoints()) {
                DistanceEntry e = club.getDistanceEntry(member, racepoint);
                assertTrue("Member instance in member list is same instance as that in distance list", e.getMember() == member);
                assertTrue("Racepoint instance in racepoint list is same instance as that in distance list", e.getRacepoint() == racepoint);
            }
        }
    }
    
    public void testClashes() throws ValidationException {
        Club club = new Club();
        {
            Member m = new Member();
            m.setName("foo");
            club.addMember(m);
        }
        {
            Member m = new Member();
            m.setName("bar");
            club.addMember(m);
        }
        {
            Member m = new Member();
            m.setName("foo");
            try {
                club.addMember(m);
                assertTrue("Expected exception", false);
            } catch (ValidationException ex) {
                assertEquals("Exception contents as expected", "Member already exists", ex.toString());
            }
        }
    }
    
}
