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
public final class ClubTest extends TestCase {

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
        Organization club = new Organization();
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

        // Organization written out

        InputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInput objIn = new ObjectInputStream(byteIn);
        club = (Organization)objIn.readObject();

        verifyReferences(club);
    }

    /**
     * Verifies that the object instances used as keys within the distances
     * structure are the same instances that are present in the member/racepoint
     * lists.
     */
    private void verifyReferences(Organization club) {
        for (Member member: club.getMembers()) {
            for (Racepoint racepoint: club.getRacepoints()) {
                DistanceEntry e = club.getDistanceEntry(member, racepoint);
                assertTrue("Member instance in member list is same instance as that in distance list", e.getMember() == member);
                assertTrue("Racepoint instance in racepoint list is same instance as that in distance list", e.getRacepoint() == racepoint);
            }
        }
    }

    public void testClashes() throws ValidationException {
        Organization club = new Organization();
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
