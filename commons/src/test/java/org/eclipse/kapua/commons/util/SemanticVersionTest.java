/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import org.junit.Assert;
import org.junit.Test;

public class SemanticVersionTest {

    @Test(expected = NullPointerException.class)
    public void semanticVersionNullTest() {
        SemanticVersion semanticVersion = new SemanticVersion(null);
    }

    @Test
    public void semanticVersionEmptyTest() {
        SemanticVersion semanticVersion = new SemanticVersion("");

        Assert.assertEquals("Expected and actual values should be the same.", "", semanticVersion.getVersionString());
        Assert.assertEquals("Expected and actual values should be the same.", "", semanticVersion.getMajorVersion().toString());
        Assert.assertNull("Null expected.", semanticVersion.getMinorVersion());
        Assert.assertNull("Null expected.", semanticVersion.getPatchVersion());
    }

    @Test
    public void semanticVersionMajorVersionTest() {
        SemanticVersion semanticVersion = new SemanticVersion("1");
        SemanticVersion.VersionToken expectedMajorVersionToken = new SemanticVersion.VersionToken("1");

        Assert.assertEquals("Expected and actual values should be the same.", "1", semanticVersion.getVersionString());
        Assert.assertEquals("Expected and actual values should be the same.", expectedMajorVersionToken.versionInteger, semanticVersion.getMajorVersion().versionInteger);
        Assert.assertNull("Null expected.", semanticVersion.getMinorVersion());
        Assert.assertNull("Null expected.", semanticVersion.getPatchVersion());
    }

    @Test
    public void semanticVersionMajorMinorVersionTest() {
        SemanticVersion semanticVersion = new SemanticVersion("1.0");
        SemanticVersion.VersionToken expectedMajorVersionToken = new SemanticVersion.VersionToken("1");
        SemanticVersion.VersionToken expectedMinorVersionToken = new SemanticVersion.VersionToken("0");

        Assert.assertEquals("Expected and actual values should be the same.", "1.0", semanticVersion.getVersionString());
        Assert.assertEquals("Expected and actual values should be the same.", expectedMajorVersionToken.versionInteger, semanticVersion.getMajorVersion().versionInteger);
        Assert.assertEquals("Expected and actual values should be the same.", expectedMinorVersionToken.versionInteger, semanticVersion.getMinorVersion().versionInteger);
        Assert.assertNull("Null expected.", semanticVersion.getPatchVersion());
    }

    @Test
    public void semanticVersionTest() {
        SemanticVersion semanticVersion = new SemanticVersion("1.1.0");
        SemanticVersion.VersionToken expectedMajorVersionToken = new SemanticVersion.VersionToken("1");
        SemanticVersion.VersionToken expectedMinorVersionToken = new SemanticVersion.VersionToken("1");
        SemanticVersion.VersionToken expectedPatchVersionToken = new SemanticVersion.VersionToken("0");

        Assert.assertEquals("Expected and actual values should be the same.", "1.1.0", semanticVersion.getVersionString());
        Assert.assertEquals("Expected and actual values should be the same.", expectedMajorVersionToken.versionInteger, semanticVersion.getMajorVersion().versionInteger);
        Assert.assertEquals("Expected and actual values should be the same.", expectedMinorVersionToken.versionInteger, semanticVersion.getMinorVersion().versionInteger);
        Assert.assertEquals("Expected and actual values should be the same.", expectedPatchVersionToken.versionInteger, semanticVersion.getPatchVersion().versionInteger);
    }

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

        String semanticMajorVersion = "1";
        String semanticMajorVersionToCompare = "1";

        //Positive tests
        for (int i = 0; i < semanticVersionAfterTrue.length; i++) {
            try {
                Assert.assertTrue("True expected.", new SemanticVersion(semanticVersionAfterTrue[i]).after(new SemanticVersion(semanticVersionToCompareTrue[i])));
            } catch (AssertionError e) {
                Assert.fail("AssertionError not expected for " + semanticVersionAfterTrue[i]);
            }
        }

        for (int i = 0; i < semanticVersionLettersAfterTrue.length; i++) {
            try {
                Assert.assertTrue("True expected.", new SemanticVersion(semanticVersionLettersAfterTrue[i]).after(new SemanticVersion(semanticVersionLettersToCompareTrue[i])));
            } catch (AssertionError e) {
                Assert.fail("AssertionError not expected for " + semanticVersionLettersAfterTrue[i]);
            }
        }

        //Negative tests
        for (int i = 0; i < semanticVersionAfterFalse.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionAfterFalse[i]).after(new SemanticVersion(semanticVersionToCompareFalse[i])));
                Assert.fail("AssertionError expected");
            } catch (AssertionError e) {
                //Expected
            }
        }

        for (int i = 0; i < semanticVersionLettersAfterFalse.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionLettersAfterFalse[i]).after(new SemanticVersion(semanticVersionLettersToCompareFalse[i])));
                Assert.fail("AssertionError expected");
            } catch (AssertionError e) {
                //Expected
            }
        }

        try {
            Assert.assertTrue(new SemanticVersion(semanticMajorVersionToCompare).after(new SemanticVersion(semanticMajorVersion)));
            Assert.fail("NullPointerException expected");
        } catch (Exception e) {
            //Expected
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
                Assert.assertTrue(new SemanticVersion(semanticVersionAfterOrMatchesTrue[i]).afterOrMatches(new SemanticVersion(semanticVersionToCompareTrue[i])));
            } catch (AssertionError e) {
                Assert.fail("AssertionError not expected for " + semanticVersionAfterOrMatchesTrue[i]);
            }
        }

        for (int i = 0; i < semanticVersionLettersAfterOrMatchesTrue.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionLettersAfterOrMatchesTrue[i]).afterOrMatches(new SemanticVersion(semanticVersionLettersToCompareTrue[i])));
            } catch (AssertionError e) {
                Assert.fail("AssertionError not expected for " + semanticVersionLettersAfterOrMatchesTrue[i]);
            }
        }

        //Negative tests
        for (int i = 0; i < semanticVersionAfterOrMatchesFalse.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionAfterOrMatchesFalse[i]).afterOrMatches(new SemanticVersion(semanticVersionToCompareFalse[i])));
                Assert.fail("AssertionError expected");
            } catch (AssertionError e) {
                //Expected
            }
        }

        for (int i = 0; i < semanticVersionLettersAfterOrMatchesFalse.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionLettersAfterOrMatchesFalse[i]).afterOrMatches(new SemanticVersion(semanticVersionLettersToCompareFalse[i])));
                Assert.fail("AssertionError expected");
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
                Assert.assertTrue(new SemanticVersion(semanticVersionMatchesTrue[i]).matches(new SemanticVersion(semanticVersionToCompareTrue[i])));
            } catch (AssertionError e) {
                Assert.fail("AssertionError not expected for " + semanticVersionMatchesTrue[i]);
            }
        }

        for (int i = 0; i < semanticVersionLettersMatchesTrue.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionLettersMatchesTrue[i]).matches(new SemanticVersion(semanticVersionLettersToCompareTrue[i])));
            } catch (AssertionError e) {
                Assert.fail("AssertionError not expected for " + semanticVersionLettersMatchesTrue[i]);
            }
        }

        //Negative tests
        for (int i = 0; i < semanticVersionMatchesFalse.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionMatchesFalse[i]).matches(new SemanticVersion(semanticVersionToCompareFalse[i])));
                Assert.fail("AssertionError expected");
            } catch (AssertionError e) {
                //Expected
            }
        }

        for (int i = 0; i < semanticVersionLettersMatchesFalse.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionLettersMatchesFalse[i]).matches(new SemanticVersion(semanticVersionLettersToCompareFalse[i])));
                Assert.fail("NullPointerException expected");
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
                Assert.assertTrue(new SemanticVersion(semanticVersionBeforeOrMatchesTrue[i]).beforeOrMatches(new SemanticVersion(semanticVersionToCompareTrue[i])));
            } catch (AssertionError e) {
                Assert.fail("AssertionError not expected for " + semanticVersionBeforeOrMatchesTrue[i]);
            }
        }

        for (int i = 0; i < semanticVersionLettersBeforeOrMatchesTrue.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionLettersBeforeOrMatchesTrue[i]).beforeOrMatches(new SemanticVersion(semanticVersionLettersToCompareTrue[i])));
            } catch (AssertionError e) {
                Assert.fail("AssertionError not expected for " + semanticVersionLettersBeforeOrMatchesTrue[i]);
            }
        }

        //Negative tests
        for (int i = 0; i < semanticVersionBeforeOrMatchesFalse.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionBeforeOrMatchesFalse[i]).beforeOrMatches(new SemanticVersion(semanticVersionToCompareFalse[i])));
                Assert.fail("AssertionError expected");
            } catch (AssertionError e) {
                //Expected
            }
        }

        for (int i = 0; i < semanticVersionLettersBeforeOrMatchesFalse.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionLettersBeforeOrMatchesFalse[i]).beforeOrMatches(new SemanticVersion(semanticVersionLettersToCompareFalse[i])));
                Assert.fail("AssertionError expected");
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

        String semanticMajorVersion = "1";
        String semanticMajorVersionToCompare = "1";

        //Positive tests
        for (int i = 0; i < semanticVersionBeforeTrue.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionBeforeTrue[i]).before(new SemanticVersion(semanticVersionToCompareTrue[i])));
            } catch (AssertionError e) {
                Assert.fail("AssertionError not expected for " + semanticVersionBeforeTrue[i]);
            }
        }

        for (int i = 0; i < semanticVersionLettersBeforeTrue.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionLettersBeforeTrue[i]).before(new SemanticVersion(semanticVersionLettersToCompareTrue[i])));
            } catch (AssertionError e) {
                Assert.fail("AssertionError not expected for " + semanticVersionLettersBeforeTrue[i]);
            }
        }

        //Negative tests
        for (int i = 0; i < semanticVersionBeforeFalse.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionBeforeFalse[i]).before(new SemanticVersion(semanticVersionToCompareFalse[i])));
                Assert.fail("AssertionError expected");
            } catch (AssertionError e) {
                //Expected
            }
        }

        for (int i = 0; i < semanticVersionLettersBeforeFalse.length; i++) {
            try {
                Assert.assertTrue(new SemanticVersion(semanticVersionLettersBeforeFalse[i]).before(new SemanticVersion(semanticVersionLettersToCompareFalse[i])));
                Assert.fail("AssertionError expected");
            } catch (AssertionError e) {
                //Expected
            }
        }

        try {
            Assert.assertTrue(new SemanticVersion(semanticMajorVersionToCompare).before(new SemanticVersion(semanticMajorVersion)));
            Assert.fail("NullPointerException expected");
        } catch (Exception e) {
            //Expected
        }
    }

    @Test
    public void semanticVersionToStringTest() {
        Assert.assertEquals("1.2.3", new SemanticVersion("1.2.3").toString());
        Assert.assertEquals("1.2.3", new SemanticVersion.VersionToken("1.2.3").toString());
    }

    @Test
    public void versionTokenNullTest() {
        SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(null);
        Assert.assertNull("Null expected.", versionToken.getVersionInteger());
        Assert.assertFalse("False expected.", versionToken.isIntegerComparison());
        Assert.assertNull("Null expected.", versionToken.getVersionString());
    }

    @Test
    public void versionTokenTest() {
        SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken("11");

        Assert.assertEquals("Expected and actual values should be the same.", (Integer) 11, versionToken.getVersionInteger());
        Assert.assertTrue("True expected.", versionToken.isIntegerComparison());
        Assert.assertEquals("Expected and actual values should be the same.", "11", versionToken.getVersionString());
    }

    @Test
    public void versionTokenNumberFormatExceptionTest() {
        SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken("1.1");

        Assert.assertNull("Null expected.", versionToken.getVersionInteger());
        Assert.assertFalse("False expected.", versionToken.isIntegerComparison());
        Assert.assertEquals("Expected and actual values should be the same.", "1.1", versionToken.getVersionString());
    }

    @Test
    public void versionTokenAfterTest() {
        String[] versionTokenStringTrue = {"21", "21", "2.1"};
        String[] versionTokenStringToCompareTrue = {"11", "1.1", "11"};

        String[] versionTokenStringFalse = {"11", "1.1", "11", "11", "1.1", "1.1"};
        String[] versionTokenStringToCompareFalse = {"21", "21", "2.1", "11", "1.1", "11"};

        String[] versionTokenStringLettersTrue = {"ba", "ba", "b.a"};
        String[] versionTokenStringToCompareLettersTrue = {"aa", "a.a", "aa"};

        String[] versionTokenStringLettersFalse = {"aa", "a.a", "aa", "aa", "a.a", "a.a"};
        String[] versionTokenStringToCompareLettersFalse = {"ba", "ba", "b.a", "aa", "a.a", "aa"};

        for (int i = 0; i < versionTokenStringTrue.length; i++) {
            SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(versionTokenStringTrue[i]);
            SemanticVersion.VersionToken versionTokenToCompare = new SemanticVersion.VersionToken(versionTokenStringToCompareTrue[i]);

            Assert.assertTrue("True expected.", versionToken.after(versionTokenToCompare));
        }
        for (int i = 0; i < versionTokenStringTrue.length; i++) {
            SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(versionTokenStringLettersTrue[i]);
            SemanticVersion.VersionToken versionTokenToCompare = new SemanticVersion.VersionToken(versionTokenStringToCompareLettersTrue[i]);

            Assert.assertTrue("True expected.", versionToken.after(versionTokenToCompare));
        }

        for (int i = 0; i < versionTokenStringTrue.length; i++) {
            SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(versionTokenStringFalse[i]);
            SemanticVersion.VersionToken versionTokenToCompare = new SemanticVersion.VersionToken(versionTokenStringToCompareFalse[i]);

            Assert.assertFalse("False expected.", versionToken.after(versionTokenToCompare));
        }

        for (int i = 0; i < versionTokenStringTrue.length; i++) {
            SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(versionTokenStringLettersFalse[i]);
            SemanticVersion.VersionToken versionTokenToCompare = new SemanticVersion.VersionToken(versionTokenStringToCompareLettersFalse[i]);

            Assert.assertFalse("False expected.", versionToken.after(versionTokenToCompare));
        }
    }

    @Test
    public void versionTokenMatchesTest() {
        String[] versionTokenStringTrue = {"11", "1.1", "1.1"};
        String[] versionTokenStringToCompareTrue = {"11", "1.1", "1.1"};

        String[] versionTokenStringFalse = {"21", "21", "2.1", "11", "1.1", "11"};
        String[] versionTokenStringToCompareFalse = {"11", "1.1", "11", "21", "2.1", "1.1"};

        String[] versionTokenStringLettersTrue = {"aa", "a.a", "a.a"};
        String[] versionTokenStringToCompareLettersTrue = {"aa", "a.a", "a.a"};

        String[] versionTokenStringLettersFalse = {"ba", "ba", "b.a", "aa", "a.a", "aa"};
        String[] versionTokenStringToCompareLettersFalse = {"aa", "a.a", "aa", "b.a", "b.a", "a.a"};

        for (int i = 0; i < versionTokenStringTrue.length; i++) {
            SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(versionTokenStringTrue[i]);
            SemanticVersion.VersionToken versionTokenToCompare = new SemanticVersion.VersionToken(versionTokenStringToCompareTrue[i]);

            Assert.assertTrue("True expected.", versionToken.matches(versionTokenToCompare));
        }

        for (int i = 0; i < versionTokenStringTrue.length; i++) {
            SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(versionTokenStringLettersTrue[i]);
            SemanticVersion.VersionToken versionTokenToCompare = new SemanticVersion.VersionToken(versionTokenStringToCompareLettersTrue[i]);

            Assert.assertTrue("True expected.", versionToken.matches(versionTokenToCompare));
        }

        for (int i = 0; i < versionTokenStringTrue.length; i++) {
            SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(versionTokenStringFalse[i]);
            SemanticVersion.VersionToken versionTokenToCompare = new SemanticVersion.VersionToken(versionTokenStringToCompareFalse[i]);

            Assert.assertFalse("False expected.", versionToken.matches(versionTokenToCompare));
        }

        for (int i = 0; i < versionTokenStringTrue.length; i++) {
            SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(versionTokenStringLettersFalse[i]);
            SemanticVersion.VersionToken versionTokenToCompare = new SemanticVersion.VersionToken(versionTokenStringToCompareLettersFalse[i]);

            Assert.assertFalse("False expected.", versionToken.matches(versionTokenToCompare));
        }
    }

    @Test
    public void versionTokenBeforeTest() {
        String[] versionTokenStringTrue = {"11", "1.1", "11"};
        String[] versionTokenStringToCompareTrue = {"21", "21", "2.1"};

        String[] versionTokenStringFalse = {"21", "21", "2.1", "11", "1.1", "11"};
        String[] versionTokenStringToCompareFalse = {"11", "1.1", "11", "11", "1.1", "1.1"};

        String[] versionTokenStringLettersTrue = {"aa", "a.a", "aa"};
        String[] versionTokenStringToCompareLettersTrue = {"ba", "ba", "b.a"};

        String[] versionTokenStringLettersFalse = {"ba", "ba", "b.a", "aa", "a.a", "aa"};
        String[] versionTokenStringToCompareLettersFalse = {"aa", "a.a", "aa", "aa", "a.a", "a.a"};

        for (int i = 0; i < versionTokenStringTrue.length; i++) {
            SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(versionTokenStringTrue[i]);
            SemanticVersion.VersionToken versionTokenToCompare = new SemanticVersion.VersionToken(versionTokenStringToCompareTrue[i]);

            Assert.assertTrue("True expected.", versionToken.before(versionTokenToCompare));
        }

        for (int i = 0; i < versionTokenStringTrue.length; i++) {
            SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(versionTokenStringLettersTrue[i]);
            SemanticVersion.VersionToken versionTokenToCompare = new SemanticVersion.VersionToken(versionTokenStringToCompareLettersTrue[i]);

            Assert.assertTrue("True expected.", versionToken.before(versionTokenToCompare));
        }

        for (int i = 0; i < versionTokenStringTrue.length; i++) {
            SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(versionTokenStringFalse[i]);
            SemanticVersion.VersionToken versionTokenToCompare = new SemanticVersion.VersionToken(versionTokenStringToCompareFalse[i]);

            Assert.assertFalse("False expected.", versionToken.before(versionTokenToCompare));
        }

        for (int i = 0; i < versionTokenStringTrue.length; i++) {
            SemanticVersion.VersionToken versionToken = new SemanticVersion.VersionToken(versionTokenStringLettersFalse[i]);
            SemanticVersion.VersionToken versionTokenToCompare = new SemanticVersion.VersionToken(versionTokenStringToCompareLettersFalse[i]);

            Assert.assertFalse("False expected.", versionToken.before(versionTokenToCompare));
        }
    }
}
