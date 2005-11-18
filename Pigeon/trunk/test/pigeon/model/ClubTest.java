/*
 * ClubTest.java
 * JUnit based test
 *
 * Created on 29 September 2005, 19:32
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
import java.util.Map;
import java.util.SortedMap;
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

    public void testSerialization() throws IOException, ClassNotFoundException{
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
    
}
