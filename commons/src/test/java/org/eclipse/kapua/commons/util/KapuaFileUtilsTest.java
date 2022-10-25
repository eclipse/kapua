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

import org.eclipse.kapua.commons.setting.KapuaSettingException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;

@Category(JUnitTests.class)
public class KapuaFileUtilsTest extends Assert {

    @Test
    public void kapuaFileUtilsTest() throws Exception {
        Constructor<KapuaFileUtils> fileUtilsConstructor = KapuaFileUtils.class.getDeclaredConstructor();
        fileUtilsConstructor.setAccessible(true);
        fileUtilsConstructor.newInstance();
    }

    @Test(expected = Exception.class)
    public void getAsUrlPositiveAndNegativeTest() throws KapuaSettingException, MalformedURLException {
        String[] permittedFormats = new String[]{"https://www.example3345.com/", "http://www.example.com:1080/docs/resource1.html",
                "https://www.example.com:1080/docs/resource2.html", "file:///c:/EXAMPLE/clock.example"};
        String[] invalidFormats = new String[]{"www.example.com:1080/docs/resource1.html", "/c:/WINDOWS/clock.example",
                "http://www.example.com:10800000000000/docs/resource1.html", "htp://www.example.com:1080/docs/resource1.html"};

        URL[] expectedUrl = new URL[]{new URL("https://www.example3345.com/"), new URL("http://www.example.com:1080/docs/resource1.html"),
                new URL("https://www.example.com:1080/docs/resource2.html"), new URL("file:///c:/EXAMPLE/clock.example")};
        for (int i = 0; i < permittedFormats.length; i++) {
            assertEquals("No exception expected", expectedUrl[i], KapuaFileUtils.getAsURL(permittedFormats[i]));
            assertNotEquals("Inappropriate http scheme.", invalidFormats[i], KapuaFileUtils.getAsURL(invalidFormats[i]));
        }
    }

    @Test
    //this test is adjusted to method implementation getAsFile().
    //therefore, the method was tested in this way without any assertion only with specific output.
    public void getAsFilePositiveAndNegativeTest() throws IOException, KapuaSettingException {
        String[] permittedFormats = new String[]{
                "https://opensource.apple.com/source/cups/cups-218/cups/data/iso-8859-1.txt",
                "http://txt2html.sourceforge.net/sample.txt",
                "https://www.lipsum.com/"};
        String[] invalidFormats = new String[]{"file:/hostname/path/the%20file.txt", "file://", "http://www.example.com:10800000000000/docs/resource1.html",
                "/c:/WINDOWS/clock.example", "https://www.w3.org/TR/PNG/iso_88591.txt", "p://txt2html.sourceforge.net/invalidName.txt"};
        String invalidHttpScheme = "file:///c:/EXAMPLE/clock.example";

        for (String value : permittedFormats) {
            try {
                File file = KapuaFileUtils.getAsFile(value);
                if (file.exists()) {
                    System.out.println("File is successfully created at " + file.getPath() + " location.");
                } else {
                    System.out.println("File has not been created.");
                }
            } catch (Exception ex) {
                fail("Inappropriate http scheme.");
            }
        }

        File fileInvalidHttpScheme = KapuaFileUtils.getAsFile(invalidHttpScheme);
        assertFalse("File should not exist.", fileInvalidHttpScheme.exists());

        for (String value : invalidFormats) {
            try {
                File file = KapuaFileUtils.getAsFile(value);
                if (file.exists()) {
                    System.out.println("File is successfully created at " + file.getPath() + " location.");
                } else {
                    System.out.println("File has not been created.");
                }
                fail("Inappropriate http scheme.");
            } catch (Exception ex) {
                //expected
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void getAsURLNullPathTest() throws KapuaSettingException {
        KapuaFileUtils.getAsURL(null);
    }

    @Test(expected = NullPointerException.class)
    public void getAsFileNullPathTest() throws KapuaSettingException {
        KapuaFileUtils.getAsFile(null);
    }
}
