/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import org.junit.Assert;
import org.junit.Test;

public class SemanticVersionTest extends Assert {

    @Test
    public void semanticVersionAfterTest() {

        String[] semanticVersionAfterTrue = new String[]{"1.1.0", "1.0.1", "1.1.1", "2.0.0", "2.0.0", "2.0.0", "1.0", "1.1", "2.0", "2"};
        String[] semanticVersionToCompareTrue = new String[]{"1.0.0", "1.0.0", "1.0.0", "1.0.0", "1.9.0", "1.9.9", "0.1", "1.0", "1.9", "1"};

        String[] semanticVersionLettersAfterTrue = new String[]{"1.1.e", "1.d.a", "e.2.1", "b.1", "1.b", "a.b", "b.a", "b"};
        String[] semanticVersionLettersToCompareTrue = new String[]{"1.1.b", "1.b.a", "d.2.1", "a.1", "1.a", "a.a", "a.a", "a"};

        String[] semanticVersionAfterFalse = new String[]{"1.0.0", "0.1.0", "0.0.1", "1.0.0", "1.0.0", "1.0.0", "1.0.0", "1.9.0", "1.9.9", "1.0", "0.1", "1.0", "1.0", "1.9", "1"};
        String[] semanticVersionToCompareFalse = new String[]{"1.0.0", "0.1.0", "0.0.1", "1.1.0", "1.0.1", "1.1.1", "2.0.0", "2.0.0", "2.0.0", "1.0", "0.1", "1.1", "2.0", "2.0", "2"};

        String[] semanticVersionLettersAfterFalse = new String[]{"a.1.1", "1.a.1", "1.1.a", "a.b.1", "1.2.d", "a.b.c", "1.e", "e.1", "e"};
        String[] semanticVersionLettersToCompareFalse = new String[]{"a.1.1", "1.a.1", "1.1.a", "a.c.1", "1.2.e", "a.b.d", "1.f", "f.1", "f"};

        //Positive tests
        for (int i = 0; i < semanticVersionAfterTrue.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionAfterTrue[i]).after(new SemanticVersion(semanticVersionToCompareTrue[i])));
            } catch (AssertionError e) {
                fail("AssertionError not expected for " + semanticVersionAfterTrue[i]);
            }
        }

        for (int i = 0; i < semanticVersionLettersAfterTrue.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionLettersAfterTrue[i]).after(new SemanticVersion(semanticVersionLettersToCompareTrue[i])));
            } catch (AssertionError e) {
                fail("AssertionError not expected for " + semanticVersionLettersAfterTrue[i]);
            }
        }

        //Negative tests
        for (int i = 0; i < semanticVersionAfterFalse.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionAfterFalse[i]).after(new SemanticVersion(semanticVersionToCompareFalse[i])));
            } catch (AssertionError e) {
                //Expected
            }
        }

        for (int i = 0; i < semanticVersionLettersAfterFalse.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionLettersAfterFalse[i]).after(new SemanticVersion(semanticVersionLettersToCompareFalse[i])));
            } catch (AssertionError e) {
                //Expected
            }
        }
    }

    @Test
    public void semanticVersionAfterOrMatchesTest() {

        String[] semanticVersionAfterOrMatchesTrue = new String[]{"1.1.0", "1.0.1", "1.1.1", "2.0.0", "2.0.0", "2.0.0", "1.0.0", "0.1.0", "0.0.1", "1.1", "1.0", "2.0", "1.0", "0.1", "2"};
        String[] semanticVersionToCompareTrue = new String[]{"1.0.0", "1.0.0", "1.0.0", "1.0.0", "1.9.0", "1.9.9", "1.0.0", "0.1.0", "0.0.1", "1.0", "0.1", "1.9", "1.0", "0.1", "1"};

        String[] semanticVersionLettersAfterOrMatchesTrue = new String[]{"a.b.c", "1.a.b", "a.1.b", "e.a.b", "a.b.e", "d.d", "1.b", "b.1", "b"};
        String[] semanticVersionLettersToCompareTrue = new String[]{"a.b.c", "1.a.b", "a.1.b", "d.a.b", "a.b.d", "d.d", "1.a", "a.1", "a"};

        String[] semanticVersionAfterOrMatchesFalse = new String[]{"1.0.0", "1.0.0", "1.0.0", "1.0.0", "1.9.0", "1.9.9", "1.0", "1.9", "1"};
        String[] semanticVersionToCompareFalse = new String[]{"1.1.0", "1.0.1", "1.1.1", "2.0.0", "2.0.0", "2.0.0", "1.1", "2.0", "2"};

        String[] semanticVersionLettersAfterOrMatchesFalse = new String[]{"a.1.2", "1.a.2", "1.2.a", "1.e.f", "e.f.1", "a.b.c", "d.d", "d.d", "a"};
        String[] semanticVersionLettersToCompareFalse = new String[]{"b.1.2", "1.b.2", "1.2.b", "1.e.g", "e.g.1", "a.b.d", "d.e", "e.d", "b"};

        //Positive tests
        for (int i = 0; i < semanticVersionAfterOrMatchesTrue.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionAfterOrMatchesTrue[i]).afterOrMatches(new SemanticVersion(semanticVersionToCompareTrue[i])));
            } catch (AssertionError e) {
                fail("AssertionError not expected for " + semanticVersionAfterOrMatchesTrue[i]);
            }
        }

        for (int i = 0; i < semanticVersionLettersAfterOrMatchesTrue.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionLettersAfterOrMatchesTrue[i]).afterOrMatches(new SemanticVersion(semanticVersionLettersToCompareTrue[i])));
            } catch (AssertionError e) {
                fail("AssertionError not expected for " + semanticVersionLettersAfterOrMatchesTrue[i]);
            }
        }

        //Negative tests
        for (int i = 0; i < semanticVersionAfterOrMatchesFalse.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionAfterOrMatchesFalse[i]).afterOrMatches(new SemanticVersion(semanticVersionToCompareFalse[i])));
            } catch (AssertionError e) {
                //Expected
            }
        }

        for (int i = 0; i < semanticVersionLettersAfterOrMatchesFalse.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionLettersAfterOrMatchesFalse[i]).afterOrMatches(new SemanticVersion(semanticVersionLettersToCompareFalse[i])));
            } catch (AssertionError e) {
                //Expected
            }
        }
    }

    @Test
    public void semanticVersionMatchesTest() {

        String[] semanticVersionMatchesTrue = new String[]{"1.0.0", "0.1.0", "0.0.1", "1.0", "0.1", "1"};
        String[] semanticVersionToCompareTrue = new String[]{"1.0.0", "0.1.0", "0.0.1", "1.0", "0.1", "1"};

        String[] semanticVersionLettersMatchesTrue = new String[]{"a.b.c", "1.1.a", "a.1.a", "a.b", "b.a", "a"};
        String[] semanticVersionLettersToCompareTrue = new String[]{"a.b.c", "1.1.a", "a.1.a", "a.b", "b.a", "a"};

        String[] semanticVersionMatchesFalse = new String[]{"1.0.0", "1.0.0", "1.0.0", "1.0.0", "1.9.0", "1.9.9", "1.1.0", "1.0.1", "1.1.1", "2.0.0", "2.0.0", "2.0.0", "1.0", "1.0", "1.1", "2.0", "2"};
        String[] semanticVersionToCompareFalse = new String[]{"1.1.0", "1.0.1", "1.1.1", "2.0.0", "2.0.0", "2.0.0", "1.0.0", "1.0.0", "1.0.0", "1.0.0", "1.9.0", "1.9.9", "1.1", "2.0", "1.0", "1.0", "1"};

        String[] semanticVersionLettersMatchesFalse = new String[]{"1.2.a", "1.a.2", "a.1.2", "a.b.1", "a.1", "1.a", "e.f", "f.e", "e"};
        String[] semanticVersionLettersToCompareFalse = new String[]{"1.2.b", "1.b.2", "b.1.2", "a.c.1", "b.1", "1.b", "e.d", "d.e", "d"};

        //Positive tests
        for (int i = 0; i < semanticVersionMatchesTrue.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionMatchesTrue[i]).matches(new SemanticVersion(semanticVersionToCompareTrue[i])));
            } catch (AssertionError e) {
                fail("AssertionError not expected for " + semanticVersionMatchesTrue[i]);
            }
        }

        for (int i = 0; i < semanticVersionLettersMatchesTrue.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionLettersMatchesTrue[i]).matches(new SemanticVersion(semanticVersionLettersToCompareTrue[i])));
            } catch (AssertionError e) {
                fail("AssertionError not expected for " + semanticVersionLettersMatchesTrue[i]);
            }
        }

        //Negative tests
        for (int i = 0; i < semanticVersionMatchesFalse.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionMatchesFalse[i]).matches(new SemanticVersion(semanticVersionToCompareFalse[i])));
            } catch (AssertionError e) {
                //Expected
            }
        }

        for (int i = 0; i < semanticVersionLettersMatchesFalse.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionLettersMatchesFalse[i]).matches(new SemanticVersion(semanticVersionLettersToCompareFalse[i])));
            } catch (AssertionError e) {
                //Expected
            }
        }
    }

    @Test
    public void semanticVersionBeforeOrMatchesTest() {

        String[] semanticVersionBeforeOrMatchesTrue = new String[]{"1.0.0", "1.0.0", "1.0.0", "1.0.0", "1.9.0", "1.9.9", "1.0.0", "0.1.0", "0.0.1", "1.0", "1.0", "1.9", "1.0", "1"};
        String[] semanticVersionToCompareTrue = new String[]{"1.1.0", "1.0.1", "1.1.1", "2.0.0", "2.0.0", "2.0.0", "1.0.0", "0.1.0", "0.0.1", "1.1", "2.0", "2.0", "1.0", "2"};

        String[] semanticVersionLettersBeforeOrMatchesTrue = new String[]{"d.e.f", "1.c.b", "1.2.a", "d.f.2", "2.1.a", "1.a", "a.a", "a.1", "a"};
        String[] semanticVersionLettersToCompareTrue = new String[]{"d.e.f", "1.e.b", "1.2.b", "e.f.2", "2.1.a", "1.b", "a.a", "b.1", "b"};

        String[] semanticVersionBeforeOrMatchesFalse = new String[]{"1.1.0", "1.0.1", "1.1.1", "2.0.0", "2.0.0", "2.0.0", "1.1", "2.0", "2.0", "2"};
        String[] semanticVersionToCompareFalse = new String[]{"1.0.0", "1.0.0", "1.0.0", "1.0.0", "1.9.0", "1.9.9", "1.0", "1.0", "1.9", "1"};

        String[] semanticVersionLettersBeforeOrMatchesFalse = new String[]{"e.a.b", "a.b.e", "1.2.e", "1.e.2", "e.1.2", "1.b", "b.1", "b"};
        String[] semanticVersionLettersToCompareFalse = new String[]{"d.a.b", "a.b.d", "1.2.d", "1.d.2", "d.1.2", "1.a", "a.1", "a"};

        //Positive tests
        for (int i = 0; i < semanticVersionBeforeOrMatchesTrue.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionBeforeOrMatchesTrue[i]).beforeOrMatches(new SemanticVersion(semanticVersionToCompareTrue[i])));
            } catch (AssertionError e) {
                fail("AssertionError not expected for " + semanticVersionBeforeOrMatchesTrue[i]);
            }
        }

        for (int i = 0; i < semanticVersionLettersBeforeOrMatchesTrue.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionLettersBeforeOrMatchesTrue[i]).beforeOrMatches(new SemanticVersion(semanticVersionLettersToCompareTrue[i])));
            } catch (AssertionError e) {
                fail("AssertionError not expected for " + semanticVersionLettersBeforeOrMatchesTrue[i]);
            }
        }

        //Negative tests
        for (int i = 0; i < semanticVersionBeforeOrMatchesFalse.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionBeforeOrMatchesFalse[i]).beforeOrMatches(new SemanticVersion(semanticVersionToCompareFalse[i])));
            } catch (AssertionError e) {
                //Expected
            }
        }

        for (int i = 0; i < semanticVersionLettersBeforeOrMatchesFalse.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionLettersBeforeOrMatchesFalse[i]).beforeOrMatches(new SemanticVersion(semanticVersionLettersToCompareFalse[i])));
            } catch (AssertionError e) {
                //Expected
            }
        }
    }

    @Test
    public void semanticVersionBeforeTest() {

        String[] semanticVersionBeforeTrue = new String[]{"1.0.0", "1.0.0", "1.0.0", "1.0.0", "1.9.0", "1.9.9", "1.0", "1.9", "1"};
        String[] semanticVersionToCompareTrue = new String[]{"1.1.0", "1.0.1", "1.1.1", "2.0.0", "2.0.0", "2.0.0", "1.1", "2.0", "2"};

        String[] semanticVersionLettersBeforeTrue = new String[]{"1.2.a", "1.c.b", "a.b.2", "a.1", "1.a", "a"};
        String[] semanticVersionLettersToCompareTrue = new String[]{"1.2.b", "1.e.b", "b.b.2", "b.1", "1.b", "b"};

        String[] semanticVersionBeforeFalse = new String[]{"1.0.0", "0.1.0", "0.0.1", "1.1.0", "1.0.1", "1.1.1", "2.0.0", "2.0.0", "2.0.0", "1.0", "0.1", "1.1", "2.0", "2"};
        String[] semanticVersionToCompareFalse = new String[]{"1.0.0", "0.1.0", "0.0.1", "1.0.0", "1.0.0", "1.0.0", "1.0.0", "1.9.0", "1.9.9", "1.0", "0.1", "1.0", "1.9", "1"};

        String[] semanticVersionLettersBeforeFalse = new String[]{"1.1.e", "1.d.a", "e.2.1", "b.1", "1.b", "b"};
        String[] semanticVersionLettersToCompareFalse = new String[]{"1.1.b", "1.b.a", "d.2.1", "a.1", "1.a", "a"};

        //Positive tests
        for (int i = 0; i < semanticVersionBeforeTrue.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionBeforeTrue[i]).before(new SemanticVersion(semanticVersionToCompareTrue[i])));
            } catch (AssertionError e) {
                fail("AssertionError not expected for " + semanticVersionBeforeTrue[i]);
            }
        }

        for (int i = 0; i < semanticVersionLettersBeforeTrue.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionLettersBeforeTrue[i]).before(new SemanticVersion(semanticVersionLettersToCompareTrue[i])));
            } catch (AssertionError e) {
                fail("AssertionError not expected for " + semanticVersionLettersBeforeTrue[i]);
            }
        }

        //Negative tests
        for (int i = 0; i < semanticVersionBeforeFalse.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionBeforeFalse[i]).before(new SemanticVersion(semanticVersionToCompareFalse[i])));
            } catch (AssertionError e) {
                //Expected
            }
        }

        for (int i = 0; i < semanticVersionLettersBeforeFalse.length; i++) {
            try {
                assertTrue(new SemanticVersion(semanticVersionLettersBeforeFalse[i]).before(new SemanticVersion(semanticVersionLettersToCompareFalse[i])));
            } catch (AssertionError e) {
                //Expected
            }
        }
    }

    @Test
    public void semanticVersionToStringTest() {

        assertEquals("1.2.3", new SemanticVersion("1.2.3").toString());
        assertEquals("1.2.3", new SemanticVersion.VersionToken("1.2.3").toString());
    }
}