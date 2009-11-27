/*
    Copyright (C) 2009  Paul Richards.

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

package mandelbulb;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pauldoo
 */
public class OctTreeTest {

    public OctTreeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateEmpty() {
        OctTree result = OctTree.createEmpty();
        assertNotNull(result);
    }

    @Test
    public void testBasicHits() {
        {
            OctTree tree = OctTree.createEmpty();
            assert(Double.isNaN(tree.firstHit(-10, 0, 0, 1, 0, 0)));
        }

        {
            OctTree tree = OctTree.createEmpty().repSetRegion(0.0, 0.0, 0.0, 0.5, 0.5, 0.5, true);
            assertEquals(10.0, tree.firstHit(-10, 0.25, 0.25, 1, 0, 0), 0.0);
            assertEquals(9.5, tree.firstHit(10, 0.25, 0.25, -1, 0, 0), 0.0);
            assertEquals(10.0, tree.firstHit(0.25, -10, 0.25, 0, 1, 0), 0.0);
            assertEquals(9.5, tree.firstHit(0.25, 10, 0.25, 0, -1, 0), 0.0);
            assertEquals(10.0, tree.firstHit(0.25, 0.25, -10, 0, 0, 1), 0.0);
            assertEquals(9.5, tree.firstHit(0.25, 0.25, 10, 0, 0, -1), 0.0);
        }
    }
}