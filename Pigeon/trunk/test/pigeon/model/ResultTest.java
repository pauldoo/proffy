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

import junit.framework.*;

/**
 *
 * @author Paul
 */
public class ResultTest extends TestCase {
    
    public ResultTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ResultTest.class);
        
        return suite;
    }
    
    public void testEquality() throws ValidationException {
        Member memberFoo = new Member();
        memberFoo.setName("Foo");
        Member memberBar = new Member();
        memberBar.setName("Bar");        
        
        String ringNumberFoo = "foo";
        String ringNumberBar = "bar";
        
        Result result1 = new Result(memberFoo, ringNumberFoo, 1);
        
        {
            Result result2 = new Result(memberFoo, ringNumberFoo, 1);
            assertEquals(result1, result2);
        }
        {
            Result result2 = new Result(memberFoo, ringNumberFoo, 2);
            assertEquals(result1, result2);
        }
        
        {
            Result result2 = new Result(memberFoo, ringNumberBar, 1);
            assertFalse(result1.equals(result2));
        }
        {
            Result result2 = new Result(memberFoo, ringNumberBar, 2);
            assertFalse(result1.equals(result2));
        }
        {
            Result result2 = new Result(memberBar, ringNumberFoo, 1);
            assertFalse(result1.equals(result2));
        }
        {
            Result result2 = new Result(memberBar, ringNumberFoo, 2);
            assertFalse(result1.equals(result2));
        }
        {
            Result result2 = new Result(memberBar, ringNumberBar, 1);
            assertFalse(result1.equals(result2));
        }
        {
            Result result2 = new Result(memberBar, ringNumberBar, 2);
            assertFalse(result1.equals(result2));
        }
    }

}
