/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.io.CharStreams;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class ResourceUtilsTest {

    @Test
    public void testConstructor() throws Exception {
        Constructor<ResourceUtils> resourceUtils = ResourceUtils.class.getDeclaredConstructor();
        resourceUtils.setAccessible(true);
        resourceUtils.newInstance();
    }

    @Test
    public void getResourceTest() {
        String resource = "test.properties";
        String noResource = "file:/does-not-exist";

        ClassLoader classloaderTest = Thread.currentThread().getContextClassLoader();
        URL url = classloaderTest.getResource(resource);

        //Positive test
        Assert.assertEquals("AssertionError not expected", url, ResourceUtils.getResource(resource));

        //Negative test
        Assert.assertNotEquals(url, ResourceUtils.getResource(noResource));
    }

    @Test
    public void openAsReaderTest() throws IOException {
        final URL urlWithResource = ResourceUtils.getResource("test.properties");
        final URL urlNoResource = ResourceUtils.getResource("file:/does-not-exist");
        Charset[] validCharsetList = new Charset[]{StandardCharsets.UTF_8, StandardCharsets.US_ASCII, StandardCharsets.ISO_8859_1};
        Charset[] invalidCharsetList = new Charset[]{StandardCharsets.UTF_16, StandardCharsets.UTF_16BE, StandardCharsets.UTF_16LE,};
        String expectedString = "###############################################################################\n" +
                "# Copyright (c) 2016, 2022 Red Hat and/or its affiliates and others\n" +
                "#\n" +
                "# This program and the accompanying materials are made\n" +
                "# available under the terms of the Eclipse Public License 2.0\n" +
                "# which is available at https://www.eclipse.org/legal/epl-2.0/\n" +
                "#\n" +
                "# SPDX-License-Identifier: EPL-2.0\n" +
                "#\n" +
                "###############################################################################\n";
        NullPointerException expectedException = new NullPointerException();

        //Positive tests
        for (int i = 0; i < validCharsetList.length; i++) {
            final Reader reader = ResourceUtils.openAsReader(urlWithResource, validCharsetList[i]);
            final String string = CharStreams.toString(reader);
            Assert.assertEquals("ComparisonFailure not expected", expectedString, string);
        }

        //Negative tests
        for (int i = 0; i < invalidCharsetList.length; i++) {
            final Reader reader = ResourceUtils.openAsReader(urlWithResource, invalidCharsetList[i]);
            final String string = CharStreams.toString(reader);
            Assert.assertNotEquals(expectedString, string);
        }

        for (int i = 0; i < validCharsetList.length; i++) {
            try (final Reader reader = ResourceUtils.openAsReader(urlNoResource, validCharsetList[i])) {
                final String string = CharStreams.toString(reader);
                Assert.assertNotNull(string);
                Assert.assertFalse(string.isEmpty());
            } catch (Exception e) {
                Assert.assertEquals(expectedException.toString(), e.toString());
            }
        }

        try (final Reader reader = ResourceUtils.openAsReader(urlWithResource, null)) {
            final String string = CharStreams.toString(reader);
            Assert.assertNotNull(string);
            Assert.assertFalse(string.isEmpty());
        } catch (Exception e) {
            Assert.assertEquals(expectedException.toString(), e.toString());
        }

        try (final Reader reader = ResourceUtils.openAsReader(urlNoResource, null)) {
            final String string = CharStreams.toString(reader);
            Assert.assertNotNull(string);
            Assert.assertFalse(string.isEmpty());
        } catch (Exception e) {
            Assert.assertEquals(expectedException.toString(), e.toString());
        }
    }
}
